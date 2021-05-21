package com.leon.biuvideo.beans.orderBeans;

/**
 * @Author Leon
 * @Time 2020/12/14
 * @Desc 订阅信息实体类
 */
public class Order {
    public String title;
    public String desc;
    public String cover;

    /**
     * 地区
     */
    public String[] areas;

    /**
     * 独家、出品、会员专享、限时免费
     */
    public String badgeType;

    /**
     * 番剧ID
     */
    public long seasonId;
    public long mediaId;

    /**
     * 订阅类型（番剧、国创、纪录片、电视剧）
     */
    public String seasonType;

    /**
     * 短链
     */
    public String shortUrl;
    public String url;

    /**
     * 跟随状态，0：全部、1：想看、2：在看、3：看过
     */
    public int followStatus;

    /**
     * 季数
     */
    public int seasonCount;
    public String seasonTitle;
    public String total;

    public String progress;

    public int coin;
    public int danmaku;
    public int follow;
    public int likes;
    public int reply;
    public int seriesFollow;
    public int seriesView;
    public int view;
}
