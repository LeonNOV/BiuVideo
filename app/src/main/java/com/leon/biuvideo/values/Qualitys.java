package com.leon.biuvideo.values;

import androidx.annotation.NonNull;

public class Qualitys {
    public static final int F16 = 16;   //流畅 360P
    public static final int F32 = 32;   //清晰 480P
    public static final int F64 = 64;   //高清 720P/720P60
    public static final int F80 = 80;   //高清 1080P
    public static final int F112 = 112; //高清 1080P+/1080P高码率
    public static final int F116 = 116; //高清 1080P60
    public static final int F120 = 120; //超清 4K

    /**
     * 获取对应ID的清晰度字符串
     *
     * @param id    清晰度ID
     * @param frameRate 帧率
     * @return  返回清晰度字符串
     */
    public static String getQualityStr(int id, String frameRate) {
        switch (id) {
            case F16:
                return "流畅 360P";
            case F32:
                return "清晰 480P";
            case F64:
                String quality = "高清 720P";

                // 判断是否为720P 60帧
                if (frameRate != null) {
                    String[] split = frameRate.split("/");
                    int var1 = Integer.parseInt(split[0]);
                    int var2 = Integer.parseInt(split[1]);

                    if (var1 / var2 > 60) {
                        quality = "高清 720P60";
                    }
                }
                return quality;
            case F80:
                return "高清 1080P";
            case F112:
                return "高清 1080P高码率";
            case F116:
                return "高清 1080P60";
            case F120:
                return "超清 4K";
            default:
                return null;
        }
    }
}
