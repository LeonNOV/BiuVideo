package com.leon.biuvideo.beans.upMasterBean;

import java.io.Serializable;

/**
 * 视频播放列表
 */
public class VideoPlayList implements Serializable {
    private static final long serialVersionUID = -6230080046652673122L;

    public String bvid;
    public String uname;
    public String title;
    public String coverUrl;
    public int length;
    public long play;
    public long danmaku;
}
