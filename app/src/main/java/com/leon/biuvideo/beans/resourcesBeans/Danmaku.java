package com.leon.biuvideo.beans.resourcesBeans;

import com.leon.biuvideo.values.DanmakuType;

/**
 * @Author Leon
 * @Time 2021/4/17
 * @Desc 弹幕数据
 */
public class Danmaku {
    /**
     * 在视频中出现的位置
     */
    public float showIndex;
    public String content;
    public DanmakuType danmakuType;
    public int danmakuSize;
    public int danmakuColor;
    public long danmakuTimestamp;

    /**
     * 弹幕池类型
     * 0：普通池
     * 1：字幕池
     * 2：特殊池（代码/BAS弹幕）
     */
    public int danmakuPoolType;

    @Override
    public String toString() {
        return "Danmaku{" +
                "showIndex=" + showIndex +
                ", content='" + content + '\'' +
                ", danmakuType=" + danmakuType.value +
                ", danmakuSize=" + danmakuSize +
                ", danmakuColor=" + danmakuColor +
                ", danmakuTimestamp=" + danmakuTimestamp +
                ", danmakuPoolType=" + danmakuPoolType +
                '}';
    }
}
