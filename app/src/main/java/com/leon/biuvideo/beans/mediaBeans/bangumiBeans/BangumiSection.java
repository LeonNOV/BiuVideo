package com.leon.biuvideo.beans.mediaBeans.bangumiBeans;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/21
 * @Desc 番剧相关PV数据
 */
public class BangumiSection {
    public int id;
    public String title;
    public List<Episode> episodeList;

    public static class Episode {
        public String aid;
        public String bvid;
        public String badge;
        public String cid;
        public String cover;

        public String title;
        public String longTitle;
        public long pubTime;
        public String shortLink;

        public int coin;
        public int danmakus;
        public int likes;
        public int play;
        public int reply;

        public String subTitle;
    }
}
