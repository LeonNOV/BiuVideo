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

    //图片个数为3的倍数
    public static String picture_more = "@375w_375h_1c.webp";

    //图片个数为2的倍数
    public static String picture_normal = "@564w_564h_1c.webp";

    //只有一张图片
    public static String picture_single = "@1125w_1125h_1e.webp";
}
