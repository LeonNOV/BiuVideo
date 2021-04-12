package com.leon.biuvideo.beans.resourcesBeans.videoBeans;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/5
 * @Desc 单视频数据（flv方式）
 */
public class VideoWithFlv {
    public int currentQualityId;
    public LinkedHashMap<Integer, String> qualityMap;

    /**
     * 分段视频信息
     */
    public List<VideoStreamInfo> videoStreamInfoList;

    public static class VideoStreamInfo {
        /**
         * 视频分段序号
         */
        public int order;
        public String size;

        /**
         * 默认视频流
         */
        public String url;

        /**
         * 备用视频流
         */
        public String[] backupUrl;
    }

}
