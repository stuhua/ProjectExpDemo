package carnetapp.ipod.model.socket;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import carnetapp.ipod.IPODConstants.FactoryID;
import carnetapp.ipod.IPODConstants.GroupID;
import carnetapp.ipod.IPODConstants.IPOD_StateActionID;
import carnetapp.ipod.model.CommandManager;
import carnetapp.ipod.model.ICommandFactory;
import carnetapp.ipod.util.ByteUtils;
import carnetapp.ipod.util.LogUtils;

/**
 *
 * @ClassName: SocketClient
 * @Description: 与OS层进行通讯的socket
 * @author Deson
 * @date 2016年8月12日 下午4:19:16
 *
 */
public class SocketClient {
    private final String TAG = SocketClient.class.getSimpleName();
    private static SocketClient mySocketClient;

    private SocketClient() {

    }

    public static SocketClient getInstance() {
        if (mySocketClient == null) {
            synchronized (SocketClient.class) {
                if (mySocketClient == null)
                    mySocketClient = new SocketClient();
            }
        }
        return mySocketClient;
    }

    /**
     * Socket mSocket=null;
     */
    static OutputStream mOut = null;
    static InputStream mIn = null;
    static LocalSocket localSocket = null;
    static LocalSocketAddress l;

    /**
     * 读取线程，专用于读取OS层IPOD数据。
     */
    private Thread read_Thread;
    /**
     * 发送线程，专用于向OS层发送IPOD数据。
     */
    private Thread write_Thread;

    public void InterruptedClient() {
        if (mIn != null) {
            try {
                mIn.close();
            } catch (IOException e) {
            }
        }
        if (mOut != null) {
            try {
                mOut.close();
            } catch (IOException e) {
            }

        }
        if (localSocket != null) {
            try {
                localSocket.close();
            } catch (Exception e) {
            }

            localSocket = null;
        }
        // write_Thread.interrupt();
        // read_Thread.interrupt();
    }

    /**
     * 接通与OS进行数据通讯的socket,打通socket
     *
     * @throws IOException
     */
    public synchronized void socketClientInit() {
        if (localSocket != null) {
            try {
                localSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.print(Log.ERROR, e.toString());
            }

            localSocket = null;
        }
        localSocket = new LocalSocket();
        try {
            l = new LocalSocketAddress("iap2_core",
                    LocalSocketAddress.Namespace.RESERVED);
            localSocket.connect(l);
            mIn = localSocket.getInputStream();
            mOut = localSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mIn == null || mOut == null || localSocket == null) {
            return;
        }

        // 创建读写线程
        read_Thread = new Thread(read_Runnable);
        read_Thread.start();
        write_Thread = new Thread(write_Runnable);
        write_Thread.start();

        LogUtils.print("socketClientInit...isConnected="
                + localSocket.isConnected());
        // inqureIPODState();

    }

    public static byte[] getCountMsgFromInputStream(InputStream ins, int len)
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

    /**
     * 数据读取线程，专用于读取OS层IPOD数据。。
     */
    Runnable read_Runnable = new Runnable() {
        byte buffer[] = new byte[4 * 1024];

        @Override
        public void run() {
            android.os.Process
                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_LOWEST);
            LogUtils.print("0706...Thread.getPriority()="
                    + Thread.currentThread().getPriority());
            DataInputStream din = null;
            int needLenth = 0;
            byte[] head;
            byte[] lenthData;
            byte[] mData;
            try {
                // 获得DataInputStream对象
                din = new DataInputStream(localSocket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.print(Log.ERROR, e.toString());
            }
            try {
                while (mIn != null) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    head = getCountMsgFromInputStream(mIn, 2);
                    if (head[0] == (byte) 0xaa && head[1] == (byte) 0x55) {
                        lenthData = getCountMsgFromInputStream(mIn, 2);
                        needLenth = ByteUtils.bytesToInt3(lenthData);

                        mData = getCountMsgFromInputStream(mIn, needLenth - 2);
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
                LogUtils.print(Log.ERROR, e.toString());
            }
        }
    };

    /**
     * 将缓存区里面的字节数据，按照长度len进行数据拷贝
     *
     * @param data
     * @param len
     */
    private void putData(byte[] data, int len) {
        byte[] buf = new byte[len];
        System.arraycopy(data, 0, buf, 0, len);
        if (buf != null) {
            // 测试代码start
            StringBuffer sb = new StringBuffer();
            for (int h = 0; h < len; h++)
                sb.append(" " + buf[h]);
        }
        List<byte[]> inMsg = unpacking(buf);
        // unpacking1(buf);

        for (byte[] bytes : inMsg) {
            IPODMessageDispatcher.getInstance().handleOSGroupID(bytes);
        }
    }

    private List<Byte> mByteList;
    Queue<Byte> mByteQueue = new LinkedList<Byte>();
    private byte[] mByteDatas;
    private int startPosition = -1;
    private int length = 0;// 协议数据长度
    private byte aa = (byte) 0xaa;
    private byte bb = (byte) 0x55;

    private byte[] unpacking1(byte[] datas) {
        int datasLen = datas.length;
        for (int i = 0; i < datas.length; i++) {
            mByteQueue.offer(datas[i]);
            if (datas[i] == aa && datas[i + 1] == bb) {
                // add()和remove()方法在失败的时候会抛出异常(不推荐)
            }

        }
        return null;
    }

	/*
	 * private byte[] unpacking2(byte[] datas) { List<Byte> mByteList = new
	 * ArrayList<Byte>(); byte[] mByteDatas; int startPosition = -1; int length
	 * = 0;// 协议数据长度
	 *
	 * // 一帧数据传过来的长度 int datasLength = datas.length; byte aa = (byte) 0xaa; byte
	 * bb = (byte) 0x55; if (datas[0] == aa && datas[1] == bb) { byte
	 * lengthArray[] = new byte[2]; lengthArray[0] = datas[2]; lengthArray[1] =
	 * datas[3]; length = ByteUtils.bytesToInt(lengthArray); if (length + 2 ==
	 * datas.length) { // 未拆包的数据， return datas; } }
	 *
	 * // 已经拆分的包 for (int i = 0; i < datasLength; i++) { if (datas[i] == aa &&
	 * datas[i + 1] == bb) { if (startPosition != -1) { // 记录结尾 } // 记录开头 byte
	 * lengthArray[] = new byte[2]; lengthArray[0] = datas[i + 2];
	 * lengthArray[1] = datas[i + 3]; length =
	 * ByteUtils.bytesToInt(lengthArray); LogUtils.print("length=" + length);
	 * mByteDatas = new byte[length + 2]; startPosition = i; continue; } if
	 * (datasLength - startPosition - 1 < length) {
	 * System.arraycopy(datasLength, startPosition, mByteDatas, 0, datasLength -
	 * startPosition + 1); return null; } } return null; }
	 */

    private List<byte[]> unpacking(byte[] datas) {
        LogUtils.print("source os data =====> " + ByteUtils.ByteToString(datas));
        byte aa = -86;
        byte bb = 85;
        List<byte[]> allData = new ArrayList<byte[]>();
        List<Byte> data = null;
        int startPosition = 0;
        for (int i = 0; i < datas.length; i++) {
            if (datas[i] == aa && datas[i + 1] == bb) {
                if (data != null && data.size() > 0) {
                    byte[] bt = new byte[data.size()];
                    System.arraycopy(datas, startPosition, bt, 0, bt.length);
                    startPosition += bt.length;
                    allData.add(bt);
                }
                data = new ArrayList<Byte>();
            }
            if (data == null)
                continue;
            data.add(datas[i]);
            if (i == datas.length - 1) {
                if (data != null && data.size() > 0) {
                    byte[] bt = new byte[data.size()];
                    System.arraycopy(datas, startPosition, bt, 0, bt.length);
                    startPosition += bt.length;
                    allData.add(bt);
                }
            }
        }
        return allData;
    }

    /**
     * 数据发送线程。。向OS层发送IPOD数据。
     */
    Runnable write_Runnable = new Runnable() {
        @Override
        public void run() {
            android.os.Process
                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_LOWEST);
            LogUtils.print("0706...Thread.getPriority()="
                    + Thread.currentThread().getPriority());
            try {
                while (true) {
                    Thread.sleep(30);
                    IPODMessage msg = mcuMessageQueue.next(); // might block
                    if (msg != null) {
                        // 在这处理
                        byte[] vBuffer = msg.getDatas();
                        if (vBuffer != null && vBuffer.length > 0) {
                            if (mOut != null && localSocket.isConnected()) {
                                LogUtils.print("writeData...vBuffer="
                                        + ByteUtils.byte2hex(vBuffer));
                                mOut.write(vBuffer);
                                LogUtils.print(localSocket.isConnected() + "");
                                mOut.flush();

                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.print(Log.ERROR, e.toString());
                socketClientInit();
            } catch (InterruptedException e) {
                LogUtils.print(Log.ERROR, e.toString());
                e.printStackTrace();
            }
        }
    };

    IPODMessageQueue mcuMessageQueue = new IPODMessageQueue();

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
            IPODMessage msg = new IPODMessage();
            msg.setDatas(toByte(data));
            mcuMessageQueue.enqueueMessage(msg);
        }

    }

    /**
     * 高优先级发送消息 声音切换消息优先级高
     *
     * @param vBuffer
     * @param vLength
     */
    public synchronized void clientSendDataImmediately(byte vBuffer[],
                                                       int vLength) {
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
            IPODMessage msg = new IPODMessage();
            msg.setDatas(toByte(data));
            mcuMessageQueue.enqueueMessageToHead(msg);// 将消息插队
        }
    }

    /**
     * 为发送数据添加报文
     *
     * @param data
     * @return
     */
    byte[] toByte(byte[] data) {
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

    byte[] rebuf = new byte[1024];

    /**
     * 向OS发送询问IPOD状态的信息
     */
    public void inqureIPODState() {
        try {
            ICommandFactory factory = CommandManager.getInstance()
                    .createFactory(FactoryID.IPOD_STATE);
            byte[] comm = factory.createCommand(GroupID.GID_REQ,
                    IPOD_StateActionID.CONNECT_STATUS, null);
            if (comm != null)
                SocketClient.getInstance().clientSendData(comm, comm.length);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.print(Log.ERROR, e.toString());
        }
    }

}
