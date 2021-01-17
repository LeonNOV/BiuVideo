package com.leon.biuvideo.beans.orderBeans;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.values.LocalOrderType;

public class LocalOrder {
    public int id;
    public String mainId;   // bangumi类型，该处为seasonId，音频为sid
    public String subId;    // 视频、bangumi的cid
    public JSONObject jsonObject;
    public long addTime;
    public long adder;
    public LocalOrderType orderType;   // 见LocalOrderType类
    public String folderName;
}
