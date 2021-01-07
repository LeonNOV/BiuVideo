package com.leon.biuvideo.beans.searchBean.bangumi;

import java.util.List;

/**
 * 番剧实体类
 */
public class Bangumi {
    public long mediaId;
    public long seasonId;

    public String title;    // 标题
    public String styles;    // 风格
    public String area;     // 地区
    public long playTime;   // 开播时间
    public String voiceActors;      // 声优
    public String desc;
    public int epSize;  // 番剧选集数
    public List<Ep>  eps;   // 所有选集具体信息

    public String angleTitle;
    public double score;
    public int reviewNum;
}
