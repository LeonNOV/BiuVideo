package com.leon.biuvideo.beans.searchBean.bangumi;

import com.leon.biuvideo.beans.videoBean.view.AnthologyInfo;

import java.io.Serializable;
import java.util.ArrayList;
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
    public String bangumiState;

    public List<Ep> eps;   // 所有选集具体信息

    /**
     * 获取AnthologyInfo集合
     *
     * @return  AnthologyInfo集合
     */
    public List<AnthologyInfo> getAnthologyInfoList() {
        List<AnthologyInfo> anthologyInfoList = new ArrayList<>();

        for (int i = 0; i < eps.size(); i++) {
            Ep ep = eps.get(i);
            AnthologyInfo anthologyInfo = new AnthologyInfo();
            anthologyInfo.mainId = String.valueOf(seasonId);
            anthologyInfo.cid = ep.cid;
            anthologyInfo.isVIP = ep.isVIP;
            anthologyInfo.badge = ep.badge;
            anthologyInfo.part = (ep.title == null || ep.title.equals("") ? i : ep.title) + "-" + ep.longTitle;
            anthologyInfo.duration = 0;

            anthologyInfoList.add(anthologyInfo);
        }

        return anthologyInfoList;
    }

    public String angleTitle;
    public float score;
    public int reviewNum;
}
