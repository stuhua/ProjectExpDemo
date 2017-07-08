package carnetapp.ipod.model.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import carnetapp.ipod.util.ByteUtils;
import carnetapp.ipod.util.LogUtils;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;

/**
 *
 * @ClassName: SocketClient
 * @Description: 重写与底层通讯的Socket
 * @author: liulh
 * @date: 2017-7-8 下午4:17:45
 */
public class SocketClient {
    private static SocketClient sInstance;
    public boolean pThreadFlag = false;

    private Thread mReadThread, mWriteThread;

    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private LocalSocket mLocalSocket;
    private LocalSocketAddress mLocalSocketAddress;

    private SocketClient() {
    }

    public static SocketClient getInstance() {
        if (sInstance == null) {
            synchronized (SocketClient.class) {
                if (sInstance == null) {
                    sInstance = new SocketClient();
                }
            }
        }
        return sInstance;
    }

    public void socketClientInit() {
        LogUtils.print("初始化成功...");
        try {
            pThreadFlag = true;
            mLocalSocket = new LocalSocket();
            mLocalSocketAddress = new LocalSocketAddress("iap2_core",
                    LocalSocketAddress.Namespace.RESERVED);
            mLocalSocket.connect(mLocalSocketAddress);
            mInputStream = mLocalSocket.getInputStream();
            mOutputStream = mLocalSocket.getOutputStream();

            mReadThread = new readThread();
            mReadThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.print("e=" + e.toString());
        }
    }

    public void rebootSocket() {
        try {
            pThreadFlag = false;
            if (mLocalSocket != null) {
                mLocalSocket.close();
                mLocalSocket = null;
            }
            if (mOutputStream != null) {
                mOutputStream.close();
            }
            if (mInputStream != null) {
                mInputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean ispThreadFlag() {
        return pThreadFlag;
    }

    public void setpThreadFlag(boolean pThreadFlag) {
        this.pThreadFlag = pThreadFlag;
    }

    /**
     * 向OS层，通过socket传送数据，最终传给MCU
     *
     * @param vBuffer
     * @param vLength
     */
    public synchronized void clientSendData(byte vBuffer[], int vLength) {
        // 改为任务队列方式发送数据。
        byte[] data = null;
        if (vBuffer != null && vBuffer.length == vLength) {
            data = vBuffer;
        } else if (vBuffer != null && vBuffer.length > vLength) {
            data = new byte[vLength];
            for (int i = 0; i < vLength; i++) {
                data[i] = vBuffer[i];
            }
        }

        if (data != null && data.length > 0) {
            mWriteThread = new writeThread(toByte(data));
            mWriteThread.start();
        }

    }

    /**
     * 为发送数据添加报文
     *
     * @param data
     * @return
     */
    private byte[] toByte(byte[] data) {
        byte[] aa = new byte[data.length + 4];

        aa[0] = (byte) 0xAA; // 数据头2byte
        aa[1] = (byte) 0x55;

        byte[] lenBytes = ByteUtils.int2Bytes(data.length + 2, 2);

        aa[2] = lenBytes[0]; // 长度为2byte
        aa[3] = lenBytes[1];

        for (int i = 4; i < aa.length; i++)
            aa[i] = data[i - 4];
        return aa;
    }

    public void sendMsg(byte[] buffer) {
        try {
            mOutputStream = mLocalSocket.getOutputStream();
            mOutputStream.write(buffer);
            mOutputStream.flush();

            mOutputStream.close();
        } catch (IOException e) {
            pThreadFlag = false;
            e.printStackTrace();
        }
    }

    private byte[] getCountMsgFromInputStream(InputStream ins, int len)
            throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int tmp = 0;
        while (len > 0) {

            if (len > buf.length)

                tmp = ins.read(buf, 0, buf.length);

            else
                tmp = ins.read(buf, 0, len);
            if (tmp >= 0) {
                outStream.write(buf, 0, tmp);
                len -= tmp;
            }

        }
        return outStream.toByteArray();
    }

    private class readThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                int needLenth = 0;
                byte[] head;
                byte[] lenthData;
                byte[] mData;
                while (pThreadFlag) {
                    Thread.sleep(30);
                    head = getCountMsgFromInputStream(mInputStream, 2);
                    if (head[0] == (byte) 0xaa && head[1] == (byte) 0x55) {
                        lenthData = getCountMsgFromInputStream(mInputStream, 2);
                        needLenth = ByteUtils.bytesToInt3(lenthData);

                        mData = getCountMsgFromInputStream(mInputStream,
                                needLenth - 2);
                        byte[] realFrameData = new byte[lenthData.length
                                + mData.length + head.length];

                        System.arraycopy(head, 0, realFrameData, 0, head.length);
                        System.arraycopy(lenthData, 0, realFrameData,
                                head.length, lenthData.length);
                        System.arraycopy(mData, 0, realFrameData, head.length
                                + lenthData.length, mData.length);
                        LogUtils.print("readData...realFrameData="
                                + ByteUtils.byte2hex(realFrameData));
                        IPODMessageDispatcher.getInstance().handleOSGroupID(
                                realFrameData);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                rebootSocket();
            } catch (InterruptedException e) {
                e.printStackTrace();
                rebootSocket();
            }
        }
    }

    private class writeThread extends Thread {
        private byte[] buffer;

        public writeThread(byte[] buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            super.run();
            sendMsg(buffer);
        }
    }

}
