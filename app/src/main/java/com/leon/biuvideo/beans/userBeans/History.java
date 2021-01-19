package com.leon.biuvideo.beans.userBeans;

import java.util.List;

/**
 * 观看历史实体类
 */
public class History {
    public long max;
    public long viewAt;
    public List<InnerHistory> innerHistory;

    public static class InnerHistory {
        public String authorName;
        public long authorMid;
        public String authorFace;
        public String cover;
        public String badge;
        public String newDesc;
        public String showTitle;
        public long viewDate;   //观看日期(秒)

        public HistoryType historyType;     //历史记录类别
        public String bvid;     //视频bvid
        public long cid;

        //只有类别为直播或专栏时，该值才有效
        //专栏需要在前面加上`cv`两个字母，直播啥都不需要
        public long oid;

        public int duration;    //视频总长度（秒）
        public int progress;    //已看进度（秒）
        public boolean isFinish;    //是否已看完，0：未看完；1：已看完
        public boolean liveState;   //直播状态
        public String tagName;      //标签
        public String title;    //主标题
        public String subTitle;     //选集标题
        public int videos;      //选集个数
    }
}