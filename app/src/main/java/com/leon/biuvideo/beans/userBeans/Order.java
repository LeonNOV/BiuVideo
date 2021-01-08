package com.leon.biuvideo.beans.userBeans;

import com.leon.biuvideo.values.OrderFollowType;

/**
 * 订阅信息实体类
 */
public class Order {
    public String title;
    public String desc;
    public String cover;
    public String[] areas;      //地区
    public String badgeType;    //独家、出品、会员专享、限时免费
    public long seasonId;     //番剧ID
    public String seasonType;   //订阅类型（番剧、国创、纪录片、电视剧）
    public String shortUrl;     //短链
    public String url;
    public OrderFollowType followStatus;        //跟随状态，0：全部、1：想看、2：在看、3：看过
    public int seasonCount;     //季数
    public String seasonTitle;
    public String total;

    public String progress;

    public int coin;
    public int danmaku;
    public int follow;
    public int likes;
    public int reply;
    public int series_follow;
    public int series_view;
    public int view;
}
