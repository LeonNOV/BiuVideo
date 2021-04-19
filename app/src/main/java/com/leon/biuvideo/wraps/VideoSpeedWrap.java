package com.leon.biuvideo.wraps;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc 视频倍速事件
 */
public class VideoSpeedWrap {
    public final float speed;

    public static VideoSpeedWrap getInstance(float speed) {
        return new VideoSpeedWrap(speed);
    }

    public VideoSpeedWrap(float speed) {
        this.speed = speed;
    }
}
