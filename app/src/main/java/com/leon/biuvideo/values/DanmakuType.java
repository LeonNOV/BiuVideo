package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2021/4/17
 * @Desc 弹幕类型，暂不支持高级弹幕、代码弹幕、BAS弹幕
 */
public enum  DanmakuType {
    /**
     * 普通弹幕
     */
    NORMAL_DANMAKU(0),

    /**
     * 底部弹幕
     */
    BOTTOM_DANMAKU(1),

    /**
     * 顶部弹幕
     */
    TOP_DANMAKU(2),

    /**
     * 逆向弹幕
     */
    REVERSE_DANMAKU(3);

    public int value;

    DanmakuType(int value) {
        this.value = value;
    }
}
