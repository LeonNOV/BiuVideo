package com.leon.biuvideo.utils;

/**
 * 需要获取低精度图片时使用
 * 默认都为低精度
 */
public class WebpSizes {
    //头像大小
    public static String face = "@68w_68h.webp";

    //封面大小
    //@468w_292h_1c.png
    public static String cover = "@160w_100h.webp";

    public enum PicturePixelSize {
        //图片个数为3的倍数
        MORE("@375w_375h_1c.webp"),

        //图片个数为2的倍数
        DOUBLE("@564w_564h_1c.webp"),

        //只有一张图片
        SINGLE("@1125w_1125h_1e.webp"),

        //图片查看器
        VIEWER("@2000w_1e.webp");

        public String value;

        PicturePixelSize(String value) {
            this.value = value;
        }
    }

}
