package com.leon.biuvideo.beans.musicBeans;

import java.util.List;

public class MusicInfo {
    public long sid;
    public long uid;
    public String uname;
    public String bvid;
    public long aid;
    public long cid;
    public List<String> authors;
    public String cover;
    public int duration;//长度（秒）
    public long passtime;//上传时间（秒）
    public String title;
    public String intro;//简介
    public String lyric;//歌词地址

    public int coinNum;
    public int collect;//收藏数
    public int comment;//评论数
    public int share;
    public int play;
}
