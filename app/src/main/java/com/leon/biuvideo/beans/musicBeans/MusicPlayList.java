package com.leon.biuvideo.beans.musicBeans;

import java.io.Serializable;

/**
 * 音频/音乐播放列表
 */
public class MusicPlayList implements Serializable {
    private static final long serialVersionUID = -5406675595566270205L;

    public int id;
    public long sid;
    public String musicName;
    public String author;
    public String bvid;
    public boolean isHaveVideo;
}
