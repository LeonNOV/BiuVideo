package com.leon.biuvideo.values;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/5
 * @Desc
 */
public class Qualitys {
    private static final List<Integer> QUALITY_CODES = new ArrayList<>();
    private static final List<String> QUALITY_STR = new ArrayList<>();

    static {
        QUALITY_CODES.add(6);
        QUALITY_CODES.add(16);
        QUALITY_CODES.add(32);
        QUALITY_CODES.add(64);
        QUALITY_CODES.add(74);
        QUALITY_CODES.add(80);
        QUALITY_CODES.add(112);
        QUALITY_CODES.add(116);
        QUALITY_CODES.add(120);

        QUALITY_STR.add("240P 极速");
        QUALITY_STR.add("360P 流畅");
        QUALITY_STR.add("480P 清晰");
        QUALITY_STR.add("720P 高清");
        QUALITY_STR.add("720P60 高清");
        QUALITY_STR.add("1080P 高清");
        QUALITY_STR.add("1080P+ 高清");
        QUALITY_STR.add( "1080P60 高清");
        QUALITY_STR.add( "4K 超清");
    }

    public static List<Integer> getQualityCodes() {
        return QUALITY_CODES;
    }

    public static List<String> getQualityStr() {
        return QUALITY_STR;
    }
}
