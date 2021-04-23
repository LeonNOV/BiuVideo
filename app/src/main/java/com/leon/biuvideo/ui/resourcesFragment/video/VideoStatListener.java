package com.leon.biuvideo.ui.resourcesFragment.video;

import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc
 */
public interface VideoStatListener {
    /**
     * 播放视频
     *
     * @param title 选集标题
     * @param videoWithFlv  单集视频信息
     * @param videoStreamIndex    分段视频索引，默认只播放第一个（该值为0）
     */
    void playVideo (String title, VideoWithFlv videoWithFlv, int videoStreamIndex);

    /**
     * 错误事件
     */
    void onError();
}
