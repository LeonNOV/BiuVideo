package com.leon.biuvideo.wraps;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc 切换视频画质事件
 */
public class VideoQualityWrap {
    public final int qualityId;

    public static VideoQualityWrap getInstance(int qualityId) {
        return new VideoQualityWrap(qualityId);
    }

    public VideoQualityWrap(int qualityId) {
        this.qualityId = qualityId;
    }
}
