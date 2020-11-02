package com.leon.biuvideo.beans.upMasterBean;

/**
 * 用户上传的视频的基本信息
 */
public class UpVideo {
    public long mid;
    public String author;
    public String cover;
    public String bvid;
    public long aid;
    public long play;
    public long create;
    public int isUnionVideo;//是否为合作视频，1：合作；0：单人
    public String length;
    public String title;
    public String description;
}
