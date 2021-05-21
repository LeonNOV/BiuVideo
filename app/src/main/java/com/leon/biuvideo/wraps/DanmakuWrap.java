package com.leon.biuvideo.wraps;

/**
 * @Author Leon
 * @Time 2021/4/18
 * @Desc 弹幕事件，主要管理弹幕的开关状态
 */
public class DanmakuWrap {
    public final boolean danmakuState;

    public static DanmakuWrap getInstance(boolean danmakuState) {
        return new DanmakuWrap(danmakuState);
    }

    public DanmakuWrap(boolean danmakuState) {
        this.danmakuState = danmakuState;
    }
}
