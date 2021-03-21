package com.leon.biuvideo.beans.homeBeans.favoriteBeans;

import java.util.List;

/**
 * @Author Leon
 * @Time 2020/12/15
 * @Desc 用户收藏文件夹中的具体数据实体类
 */
public class FavoriteVideoFolderDetail {
    public long id;
    public long addTime;
    public int count;
    public String cover;
    public String title;

    public String userFace;
    public long userMid;
    public String userName;

    public List<Media> medias;

    public static class Media {
        public String bvid;
        public String cover;
        public long addTime;
        public int duration;
        public String title;
        public String desc;
        public String link;

        public int collect;
        public int danmaku;
        public int play;

        public long mid;
        public String name;

        /**
         * 是否已失效
         */
        public boolean isFailed;
    }
}
