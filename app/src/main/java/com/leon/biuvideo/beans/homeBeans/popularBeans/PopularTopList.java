package com.leon.biuvideo.beans.homeBeans.popularBeans;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/25
 * @Desc 排行榜数据
 */
public class PopularTopList {
    public String aid;
    public String bvid;
    public String seasonId;

    public String title;
    public String cover;
    public String badge;
    public String extra1;
    public String extra2;

    public String duration;
    public String score;

    public List<OtherVideo> otherVideoList;

    /**
     * 同UP主上榜的视频
     */
    public static class OtherVideo {
        public String aid;
        public String bvid;
        public String title;
        public String score;
    }
}
