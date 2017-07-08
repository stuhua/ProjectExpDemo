package carnetapp.ipod.model.socket;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;
import carnetapp.ipod.IPODConstants;
import carnetapp.ipod.IPODConstants.IPOD_InfoActionID;
import carnetapp.ipod.IPODConstants.IPOD_StateActionID;
import carnetapp.ipod.IPODConstants.IpodSource;
import carnetapp.ipod.model.bean.ConnectedBean;
import carnetapp.ipod.model.bean.IPODAudioBean;
import carnetapp.ipod.model.bean.MusicInfo;
import carnetapp.ipod.model.bean.MusicPlayListBean;
import carnetapp.ipod.model.bean.MusicPlayingBean;
import carnetapp.ipod.model.bean.PlayListGroup;
import carnetapp.ipod.model.bean.ProgressBean;
import carnetapp.ipod.presenter.EventBusManager;
import carnetapp.ipod.presenter.IPODManager;
import carnetapp.ipod.util.ByteUtils;
import carnetapp.ipod.util.LogUtils;

public class IPODHandlerOSMsg {
    public static int MUSICLIST_START = 6;
    private static IPODHandlerOSMsg sInstance;
    // 歌曲列表
    private List<IPODAudioBean> mMusicLists = new ArrayList<IPODAudioBean>();
    private List<MusicPlayListBean> mMusicPlayLists = new ArrayList<MusicPlayListBean>();
    private List<PlayListGroup> playlists = IPODManager.getInstance()
            .getPlaylists();

    public static IPODHandlerOSMsg getInstance() {
        if (sInstance == null) {
            synchronized (IPODHandlerOSMsg.class) {
                if (sInstance == null) {
                    sInstance = new IPODHandlerOSMsg();
                }
            }
        }
        return sInstance;
    }

    private IPODHandlerOSMsg() {

    }

    /**
     *
     * @param datas
     */
    public void handleIPODStateMessage(byte[] datas) {
        switch (datas[6]) {
            case IPOD_StateActionID.CONNECT_STATUS:
                // 连接的状态
                getIpodConnectStatus(datas[7]);
                break;
            case IPOD_StateActionID.PLAYER_STATUS:
                // 播放器状态
                getIpodPlayerStatus(datas[7], datas);
                break;
        }
        LogUtils.print("更新界面1，状态...");
        EventBusManager.getInstance().getEvent()
                .post(MusicPlayingBean.getInstance());
    }

    private void getIpodConnectStatus(int ipodConnectStatus2) {
        LogUtils.print("0404...llh...setIpodConnectStatus = "
                + ipodConnectStatus2);
        switch (ipodConnectStatus2) {
            // 0x00: disconnect断开状态
            case 0:
                ConnectedBean.getInstance().setConnected(false);
                break;
            // 0x01: connect连接上的状态
            case 1:
                ConnectedBean.getInstance().setConnected(true);
                break;
            default:
                break;
        }
        EventBusManager.getInstance().getEvent()
                .post(ConnectedBean.getInstance());
    }

    private void getIpodPlayerStatus(int playState, byte[] datas) {
        LogUtils.print("playState=" + playState);
        switch (playState) {
            case IPODConstants.PlayState.EVENT_PLAY_CURRENT_TIME:
                // 当前时间
                MusicPlayingBean.getInstance().setCurrentTime(
                        handleUpdatePlayTime(datas));
                break;
            case IPODConstants.PlayState.EVENT_PLAY_LAST:
            case IPODConstants.PlayState.EVENT_PLAY_NEXT:
                break;
            case IPODConstants.PlayState.EVENT_PLAY:
                LogUtils.print("播放...");
                MusicPlayingBean.getInstance().setPlay(true);
                break;
            case IPODConstants.PlayState.EVENT_STOP:
            case IPODConstants.PlayState.EVENT_PAUSE:
                LogUtils.print("暂停...");
                MusicPlayingBean.getInstance().setPlay(false);
                break;
            case IPODConstants.PlayState.EVENT_REPEAT_ALL:
                MusicPlayingBean.getInstance().setPlayMode(
                        IPODConstants.PlayState.EVENT_REPEAT_ALL);
                break;
            case IPODConstants.PlayState.EVENT_REPEAT_ONE:
                MusicPlayingBean.getInstance().setPlayMode(
                        IPODConstants.PlayState.EVENT_REPEAT_ONE);
                break;
            case IPODConstants.PlayState.EVENT_SHUFFLE_SONGS:
                MusicPlayingBean.getInstance().setPlayMode(
                        IPODConstants.PlayState.EVENT_SHUFFLE_SONGS);
                break;

        }

    }

    private long handleUpdatePlayTime(byte[] datas) {
        long playTime = 0;
        try {
            byte[] bytes = new byte[4];
            System.arraycopy(datas, 8, bytes, 0, bytes.length);
            playTime = ByteUtils.bytesToInt2(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.print("handleUpdatePlayTime exception = " + e);
        }
        LogUtils.print("handleUpdatePlayTime playTime= " + playTime);
        return playTime;
    }

    private long getDataTime(byte[] datas) {
        long data = 0;
        try {
            byte[] b = new byte[4];
            b[0] = datas[7];
            b[1] = datas[8];
            b[2] = datas[9];
            b[3] = datas[10];
            data = ByteUtils.bytesToInt2(b);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.print("getDataTime exception = " + e);
        }
        LogUtils.print("IPODInfo data = " + data);
        return data;
    }

    /**
     * 当前播发的歌曲 包含的歌曲信息(歌手，专辑，时长)
     *
     * @param datas
     */
    public void handleIPODInfoMessage(byte[] datas) {
        LogUtils.print("IPODInfo handleIPODInfoMessage datas = "
                + ByteUtils.byte2hex(datas));
        switch (datas[6]) {
            case IPOD_InfoActionID.MP3Title:
                LogUtils.print("MP3Title...getData(datas)=" + getData(datas));
                MusicPlayingBean.getInstance().setName(getData(datas));
                // IPODManager.getInstance().resetPlayInfo();
                break;
            case IPOD_InfoActionID.Author:
                MusicPlayingBean.getInstance().setAuthor(getData(datas));
                break;
            case IPOD_InfoActionID.Singer:
                MusicPlayingBean.getInstance().setArtist(getData(datas));
                break;
            case IPOD_InfoActionID.Album:
                MusicPlayingBean.getInstance().setAlbum(getData(datas));
                break;
            case IPOD_InfoActionID.Time:// 歌曲总时长：单位毫秒值
                LogUtils.print("总时长...getData(datas)=" + getDataTime(datas));
                MusicPlayingBean.getInstance().setAllTime(getDataTime(datas));
                break;
            case IPOD_InfoActionID.Path:
                MusicPlayingBean.getInstance().setFilePath(getData(datas));
                break;
        }
        EventBusManager.getInstance().getEvent()
                .post(MusicPlayingBean.getInstance());
    }

    private String getData(byte[] datas) {
        String data = "未知";
        try {
            byte len = datas[7];
            byte[] b = new byte[len];
            System.arraycopy(datas, 8, b, 0, len);
            data = new String(b, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.print("getData exception = " + e);
        }
        LogUtils.print("IPODInfo getData  data = " + data);
        return data;
    }

    /**
     * 获取播放列表
     *
     * @param datas
     */
    public void handleIPODSourceMsg(byte[] datas) {
        LogUtils.print("datas[6]=" + datas[6]);
        switch (datas[6]) {
            // MediaPlaylist(播放列表)
            case IpodSource.MEDIAPLAY_LIST:
                getPlayList(datas);
                break;
            // 数据加载进度
            case IpodSource.UpdateProgress:
                getProgress(datas);
                break;
            // 文件长度 0x01
            case IpodSource.FileTransferLen:
                getFileTransferLen(datas);
                break;
            // 文件内容 0x02
            case IpodSource.FileTransferContent:
                LogUtils.print("3690...datas=" + ByteUtils.byte2hex(datas));
                getFileTransferContent(datas);
                break;
            // 媒体信息
            case IpodSource.MEDIAITEM_INFO:
                getMusicList(datas);
                break;

            default:
                break;
        }

    }

    /**
     * 获取播放列表
     *
     * @param datas
     */

    private void getPlayList(byte[] datas) {
        try {
            int len = 6;
            int IDLen = 8;
            int arrayLen = 2;
            MusicPlayListBean bean = new MusicPlayListBean();
            PlayListGroup playGroup = new PlayListGroup();
            playGroup.setMusicData(new ArrayList<MusicInfo>());

            byte[] ID = new byte[IDLen];
            System.arraycopy(datas, len + 1, ID, 0, ID.length);
            bean.setPlayListID(ByteUtils.byteToLong(ID, 0));
            playGroup.setID(ByteUtils.byteToLong(ID, 0));
            len += ID.length;

            byte[] nameArray = new byte[arrayLen];
            System.arraycopy(datas, len + 1, nameArray, 0, nameArray.length);
            int titleLen = ByteUtils.bytesToInt3(nameArray);
            len += nameArray.length;
            byte[] title = new byte[titleLen];
            System.arraycopy(datas, len + 1, title, 0, title.length);
            String name = new String(title, "utf-8");
            bean.setMusicName(name);
            playGroup.setName(name);
            len += title.length;

            byte[] parentID = new byte[IDLen];
            System.arraycopy(datas, len + 1, parentID, 0, parentID.length);
            bean.setParentID(ByteUtils.byteToLong(parentID, 0) + "");
            playGroup.setParentID(ByteUtils.byteToLong(parentID, 0));
            len += parentID.length;

            byte[] isPlaylistIsFolderArray = new byte[1];
            System.arraycopy(datas, len + 1, isPlaylistIsFolderArray, 0,
                    isPlaylistIsFolderArray.length);
            String isPlaylistIsFolder = new String(isPlaylistIsFolderArray,
                    "utf-8");
            if (TextUtils.isEmpty(isPlaylistIsFolder)) {
                isPlaylistIsFolder = true + "";
            }
            bean.setPlaylistIsFolder(true);
            playGroup.setIsFolder(1);
            len += isPlaylistIsFolderArray.length;

            byte[] identifierArray = new byte[1];
            System.arraycopy(datas, len + 1, identifierArray, 0,
                    identifierArray.length);
            int FTI = ByteUtils.bytes2Int(identifierArray);
            playGroup.setFTI(FTI);
            len += identifierArray.length;

            LogUtils.print("playList0...ID=" + ByteUtils.byteToLong(ID, 0)
                    + "...titleLen=" + titleLen + "...name=" + name
                    + "...parentID=" + ByteUtils.byteToLong(parentID, 0)
                    + "lll...PlaylistIsFolder=" + isPlaylistIsFolder
                    + "...FTI=" + FTI);
            LogUtils.print("2580...FTI=" + FTI);

            EventBusManager.getInstance().getEvent().post(bean);

            for (PlayListGroup pg : playlists) {
                if (pg.getFTI() == FTI) {
                    pg.setID(playGroup.getID()).setIsFolder(1)
                            .setName(playGroup.getName())
                            .setParentID(playGroup.getParentID());
                    return;
                }
            }
            playlists.add(playGroup);

        } catch (Exception e) {
            LogUtils.print("playList0===handleGetMediaPlayList===Exception======>>>"
                    + e);
        }
    }

    /**
     * 数据加载进度
     *
     * @param datas
     */

    private void getProgress(byte[] datas) {
        int progress = ByteUtils.byteToInt(datas[7]);
        LogUtils.print("0001...加载进度=" + progress);
        ProgressBean.getInstance().setProgress(progress);
        EventBusManager.getInstance().getEvent()
                .post(ProgressBean.getInstance());
    }

    /**
     * 媒体信息
     *
     * @param datas
     */
    private void getMusicList(byte[] datas) {
        LogUtils.print("665662...歌曲列表原始数据datas=" + datas.length + "..."
                + ByteUtils.byte2hex(datas) + "\n");
        IPODAudioBean bean = new IPODAudioBean();
        int startLen = 6;// 从第6个字节开始
        int itemIDLen = 8;
        int arrayLen = 2;

        byte[] itemIDArray = new byte[itemIDLen];
        System.arraycopy(datas, startLen + 1, itemIDArray, 0,
                itemIDArray.length);
        long itemID = ByteUtils.byteToLong(itemIDArray, 0);
        bean.setItemID(itemID);
        startLen += itemIDArray.length;

        if (itemID != 0) {
            byte[] titleArray = new byte[arrayLen];
            System.arraycopy(datas, startLen + 1, titleArray, 0,
                    titleArray.length);
            int titleLen = ByteUtils.bytesToInt3(titleArray);
            startLen += titleArray.length;
            byte[] title = new byte[titleLen];
            System.arraycopy(datas, startLen + 1, title, 0, title.length);
            String name;
            try {
                name = new String(title, "utf-8");
                bean.setName(name);
                startLen += title.length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        byte[] albumIDArray = new byte[itemIDLen];
        System.arraycopy(datas, startLen + 1, albumIDArray, 0,
                albumIDArray.length);
        long albumID = ByteUtils.byteToLong(albumIDArray, 0);
        bean.setAlbumID(albumID);
        startLen += albumIDArray.length;
        if (albumID != 0) {

            byte[] albumLenArray = new byte[arrayLen];
            System.arraycopy(datas, startLen + 1, albumLenArray, 0,
                    albumLenArray.length);
            int albumLen = ByteUtils.bytesToInt3(albumLenArray);
            startLen += albumLenArray.length;
            byte[] album = new byte[albumLen];
            System.arraycopy(datas, startLen + 1, album, 0, album.length);
            String albumStr;
            try {
                albumStr = new String(album, "utf-8");
                bean.setAlbum(albumStr);
                startLen += album.length;
                LogUtils.print("070811...albumStr=" + albumStr+"len="+albumStr.length());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        byte[] artistIDArray = new byte[itemIDLen];
        System.arraycopy(datas, startLen + 1, artistIDArray, 0,
                artistIDArray.length);
        long artistID = ByteUtils.byteToLong(artistIDArray, 0);
        bean.setArtistID(artistID);
        startLen += artistIDArray.length;

        if (artistID != 0) {
            byte[] artistLenArray = new byte[arrayLen];
            System.arraycopy(datas, startLen + 1, artistLenArray, 0,
                    artistLenArray.length);
            int artistLen = ByteUtils.bytesToInt3(artistLenArray);
            startLen += artistLenArray.length;
            byte[] artist = new byte[artistLen];
            System.arraycopy(datas, startLen + 1, artist, 0, artist.length);
            String artistStr;
            try {
                artistStr = new String(artist, "utf-8");
                bean.setArtist(artistStr);
                startLen += artist.length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        byte[] genreIDArray = new byte[itemIDLen];
        System.arraycopy(datas, startLen + 1, genreIDArray, 0,
                genreIDArray.length);
        long genreID = ByteUtils.byteToLong(genreIDArray, 0);
        bean.setGenreID(genreID);
        startLen += genreIDArray.length;

        if (genreID != 0) {
            byte[] genreLenArray = new byte[arrayLen];
            System.arraycopy(datas, startLen + 1, genreLenArray, 0,
                    genreLenArray.length);
            int genreLen = ByteUtils.bytesToInt3(genreLenArray);
            startLen += genreLenArray.length;
            byte[] genre = new byte[genreLen];
            System.arraycopy(datas, startLen + 1, genre, 0, genre.length);
            String genreStr;
            try {
                genreStr = new String(genre, "utf-8");
                bean.setGenre(genreStr);
                startLen += genre.length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        byte[] composerIDArray = new byte[itemIDLen];
        System.arraycopy(datas, startLen + 1, composerIDArray, 0,
                composerIDArray.length);
        long composerID = ByteUtils.byteToLong(composerIDArray, 0);
        bean.setComposerID(composerID);
        startLen += genreIDArray.length;

        if (composerID != 0) {
            byte[] composerLenArray = new byte[arrayLen];
            System.arraycopy(datas, startLen + 1, composerLenArray, 0,
                    composerLenArray.length);
            int composerLen = ByteUtils.bytesToInt3(composerLenArray);
            startLen += composerLenArray.length;
            byte[] composer = new byte[composerLen];
            System.arraycopy(datas, startLen + 1, composer, 0, composer.length);
            try {
                String composerStr = new String(composer, "utf-8");
                bean.setComposer(composerStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Log.d("getMusicList", "66662...歌曲名=" + bean.getName());
        EventBusManager.getInstance().getEvent().post(bean);
    }

    /**
     * 获取某一组播放列表数据的总长度： 数据解析协议：datas为byte[]数组，前7位为头文件，从第8位开始为数据：
     * FTI:data[0]+数据总长度:data[1]---- data[8]
     *
     * @param datas
     */
    private void getFileTransferLen(byte[] datas) {
        try {
            playlists = IPODManager.getInstance().getPlaylists();
            int startPosition = 7;
            byte[] FTIArray = new byte[1];
            System.arraycopy(datas, startPosition, FTIArray, 0, FTIArray.length);

            int FTI = ByteUtils.bytes2Int(FTIArray);
            startPosition += FTIArray.length;
            byte[] lenArray = new byte[8];
            System.arraycopy(datas, startPosition, lenArray, 0, lenArray.length);

            long len = ByteUtils.byteToLong(lenArray, 0);

            for (int i = 0; i < playlists.size(); i++) {

                if (FTI == (playlists.get(i).getFTI())) {
                    playlists.get(i).setMusicDataLen(len + "");

                    return;
                }
            }
            PlayListGroup pg = new PlayListGroup();
            pg.setFTI(FTI);
            pg.setMusicDataLen(len + "");
            pg.setMusicData(new ArrayList<MusicInfo>());
            playlists.add(pg);

            LogUtils.print("2580...FTI=" + FTI + "...len=" + len);
        } catch (Exception e) {
            LogUtils.print("===handleGetFileTransferLen===Exception======>>>"
                    + e);
        }
    }

    /**
     * 获取某一组播放列表全部数据，第一个数据为FTI标识，后面为8bytes表示歌曲的ID
     * 数据解析协议：datas为byte[]数组，前7位为头文件，从第8位开始为数据：FTI:data[0]+每首歌曲的ID,每个ID 8 Bytes
     *
     * @param datas
     */
    private void getFileTransferContent(byte[] datas) {
        try {
            int startPosition = 7;
            byte[] FTIArray = new byte[1];
            System.arraycopy(datas, startPosition, FTIArray, 0, FTIArray.length);

            int FTI = ByteUtils.bytes2Int(FTIArray);
            startPosition += FTIArray.length;

            byte[] dataTypeArray = new byte[1];
            System.arraycopy(datas, startPosition, dataTypeArray, 0,
                    dataTypeArray.length);

            int dataType = ByteUtils.bytes2Int(dataTypeArray);
            startPosition += dataTypeArray.length;

            for (PlayListGroup pg : playlists) {
                if ((FTI != pg.getFTI())
                        || TextUtils.isEmpty(pg.getMusicDataLen())) {
                    continue;
                }
                int dataLen = Integer.parseInt(pg.getMusicDataLen());

                for (int i = 0; i < dataLen;) {
                    if (datas.length - startPosition < 8) {
                        break;
                    }
                    i++;
                    byte[] idArray = new byte[8];
                    System.arraycopy(datas, startPosition, idArray, 0,
                            idArray.length);

                    long itemID = ByteUtils.byteToLong(idArray, 0);
                    MusicInfo music = new MusicInfo();
                    music.setItemID(itemID);
                    pg.getMusicData().add(music);
                    startPosition += idArray.length;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
