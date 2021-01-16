package com.leon.biuvideo.beans.orderBeans;

import com.leon.biuvideo.values.LocalOrderType;

public class LocalOrder {
    public int id;
    public String cover;
    public String title;
    public String desc;     // 适用于Bangumi、Audio(作者名称)
    public String area;     // 适用于Bangumi
    public int duration;        // 视频/音频长度
    public int count;       // 适用于Bangumi
    public String mainId;   // bangumi类型，该处为seasonId，音频为sid
    public String subId;    // 视频、bangumi的cid
    public LocalOrderType orderType;   // 见LocalOrderType类
    public long adder;
    public long addTime;
    public String folderName;
}
