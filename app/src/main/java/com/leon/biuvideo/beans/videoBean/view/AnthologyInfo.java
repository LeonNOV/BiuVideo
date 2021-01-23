package com.leon.biuvideo.beans.videoBean.view;

import java.io.Serializable;

/**
 * 单个视频选集信息
 */
public class AnthologyInfo implements Serializable {
    public String mainId;
    public long cid;
    public String part;
    public boolean isDownloaded;
    public String badge;
    public boolean isVIP;
    public int duration;//视频长度
}
