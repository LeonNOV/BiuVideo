package com.leon.biuvideo.beans.upMasterBean;

/**
 * 相簿
 */
public class UpPicture {
    public long docId;
    public long posterUid;
    public String title;
    public String description;
    public int count;//图片数量
    public long view;
    public long like;
//    public long ctime;
    public String pictureUrl;//只获取第一张图片的地址
}
