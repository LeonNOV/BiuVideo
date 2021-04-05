package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2020/12/30
 * @Desc 视频画质
 */
public enum Quality {
    /**
     * 240P 极速（仅mp4方式）
     */
    Q6(6),

    /**
     * 360P 流畅
     */
    Q16(16),

    /**
     * 480P 清晰
     */
    Q32(32),

    /**
     * 720P 高清
     */
    Q64(64),

    /**
     * 720P60 高清
     */
    Q74(74),

    /**
     * 1080P 高清
     */
    Q80(80),

    /**
     * 1080P+ 高清
     */
    Q112(112),

    /**
     * 1080P60 高清
     */
    Q116(116),

    /**
     * 4K 超清
     */
    Q120(120);

    public int value;

    Quality(int value) {
        this.value = value;
    }
}
