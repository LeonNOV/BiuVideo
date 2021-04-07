package com.leon.biuvideo.beans.mediaBeans.videoBeans;

import java.util.List;

/**
 * @Author Leon
 * @Time 2020/10/18
 * @Desc 视频详细信息数据
 */
public class VideoDetailInfo {
    public String bvid;
    public String aid;
    public int videos;
    public int tagId;
    public String tagName;
    public String cover;
    public String title;
    public long pubTime;
    public String desc;

    public UserInfo userInfo;
    public VideoInfo videoInfo;

    public List<AnthologyInfo> anthologyInfoList;

    public static class UserInfo {
        public String userMid;
        public String userName;
        public String userFace;
    }

    public static class VideoInfo {
        public int view;
        public int danmaku;
        public int comment;
        public int favorite;
        public int like;
        public int coin;
        public int share;
    }

    public static class AnthologyInfo {
        public String mainId;
        public String cid;
        public String part;
        public String badge;
        public String duration;
    }
}