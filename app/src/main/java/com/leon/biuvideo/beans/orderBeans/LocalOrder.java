package com.leon.biuvideo.beans.orderBeans;

public class LocalOrder {
    public String title;
    public String desc;     // 适用于Bangumi
    public String area;     // 适用于Bangumi
    public String progress;     // 适用于Bangumi
    public int count;       // 适用于Bangumi
    public String mainId;   // bangumi类型，该处为seasonId，音频为sid
    public String subId;    // 视频、bangumi的cid
    public int orderType;   // 见LocalOrderType类
    public long adder;
    public long addTime;
    public String folderName;
}
