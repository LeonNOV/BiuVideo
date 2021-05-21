package com.leon.biuvideo.beans.resourcesBeans;

import com.leon.biuvideo.values.RecommendType;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc 推荐内容
 */
public class VideoRecommend {
    public RecommendType recommendType;

    public long aid;
    public String bvid;
    public String cover;
    public String title;
    public String desc;
    public long pubdate;

    public String userName;
    public String userFace;
    public String userMid;

    public String duration;

    public int view;
    public int danmaku;
    public int reply;
    public int favorite;
    public int coin;
    public int share;
    public int like;
}
