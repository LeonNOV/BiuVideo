package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2020/11/14
 * @Desc 图片像素大小值
 */
public enum ImagePixelSize {
    //头像大小
    FACE("@68w_68h.webp"),

    //封面大小
    COVER("@160w_100h.webp"),

    //图片个数为3的倍数
    MORE("@375w_375h_1c.webp"),

    //图片个数为2的倍数
    DOUBLE("@564w_564h_1c.webp"),

    //只有一张图片
    SINGLE("@1125w_1125h_1e.webp"),

    //图片查看器
    VIEWER("@2000w_1e.webp");

    public String value;

    ImagePixelSize(String value) {
        this.value = value;
    }
}
