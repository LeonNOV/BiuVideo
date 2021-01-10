package com.leon.biuvideo.beans.searchBean.bangumi;

import java.io.Serializable;
import java.util.List;

/**
 * 番剧实体类
 */
public class Bangumi implements Serializable {
    public long mediaId;
    public long seasonId;

    public String title;    // 标题
    public String originalTitle;     //原名
    public String cover;
    public String styles;    // 风格
    public String area;     // 地区
    public long playTime;   // 开播时间，秒
    public String voiceActors;      // 声优
    public String otherInfo;    // 其他信息
    public String desc;     //简介
    public int epSize;  // 番剧选集数

    public List<Ep>  eps;   // 所有选集具体信息

    public String angleTitle;
    public float score;
    public int reviewNum;
}
