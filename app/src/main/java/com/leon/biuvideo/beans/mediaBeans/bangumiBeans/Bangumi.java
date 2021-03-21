package com.leon.biuvideo.beans.mediaBeans.bangumiBeans;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/21
 * @Desc 番剧数据
 */
public class Bangumi {
    public String mediaId;
    public String seasonId;
    public String seasonTitle;
    public String cover;
    public String desc;

    public String newEpDesc;
    public boolean newEpIsNew;

    /**
     * 是否已完结
     */
    public boolean isFinished;

    /**
     * 是否已发布
     */
    public boolean isStarted;

    public String pubTime;
    public String pubTimeShow;

    public int ratingCount;
    public double ratingScore;

    /**
     * 单集信息
     */
    public List<BangumiEp> bangumiEpList;

    /**
     * 其他季数
     */
    public List<BangumiSeason> bangumiSeasonList;

    /**
     * 番剧相关PV
     */
    public List<BangumiSection> bangumiSectionList;

    public String seriesId;
    public String seriesTitle;

    public int coins;
    public int danmakus;
    public int favorites;
    public int likes;
    public int reply;
    public int share;
    public int views;

    public String subtitle;
    public String title;
}
