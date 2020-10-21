package com.leon.biuvideo.beans.upMasterBean;

/**
 * 音频具体信息
 */
public class AudioInfo {
    public long sid;//音频id
    public String bvid;//音频mv（视频）
    public String uname;
    public String author;
    public String title;
    public String cover;
    public String lyric;//歌词地址
    public String intro;//介绍
    public long passtime;
    public long ctime;

    //以下为statisticJSON对象中的数据
    public int play;
    public int collect;//收藏数
    public int comment;
    public int share;
    public int coinNum;
}
