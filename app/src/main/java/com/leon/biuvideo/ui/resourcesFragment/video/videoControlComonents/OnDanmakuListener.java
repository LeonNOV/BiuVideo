package com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents;

/**
 * @Author Leon
 * @Time 2021/4/18
 * @Desc 控制弹幕监听
 */
public interface OnDanmakuListener {
    /**
     * 停止拖动进度条/停止滑动屏幕调整进度
     *
     * @param position  当前视频进度（毫秒值）
     */
    void onSeekTo(long position);
}
