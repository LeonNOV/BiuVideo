package com.leon.biuvideo.beans.userBeans;

import com.leon.biuvideo.values.HistoryPlatformType;

/**
 * @Author Leon
 * @Time 2020/12/9
 * @Desc 观看历史数据
 */
public class History {
    public HistoryPlatformType historyPlatformType;

    public String authorName;
    public long authorMid;
    public String authorFace;
    public String cover;
    public String badge;
    public String newDesc;
    public String showTitle;

    /**
     * 观看日期(秒)
     */
    public long viewDate;

    /**
     * 历史记录类别
     */
    public HistoryType historyType;

    /**
     * 视频bvid
     */
    public String bvid;
    public long cid;

    /**
     * 只有类别为直播或专栏时，该值才有效
     * 专栏需要在前面加上`cv`两个字母，直播啥都不需要
     */
    public long oid;

    /**
     * 视频总长度（秒）
     */
    public int duration;

    /**
     * 已看进度（秒）
     */
    public int progress;

    /**
     * 是否已看完，0：未看完；1：已看完
     */
    public boolean isFinish;

    /**
     * 直播状态
     */
    public boolean liveState;

    /**
     * 标签
     */
    public String tagName;

    /**
     * 主标题
     */
    public String title;

    /**
     * 选集标题
     */
    public String subTitle;

    /**
     * 选集个数
     */
    public int videos;
}