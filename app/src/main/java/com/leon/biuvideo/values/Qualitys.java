package com.leon.biuvideo.values;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/5
 * @Desc
 */
public class Qualitys {
    private static final List<String[]> QUALITYS = new ArrayList<>();

    static {
        QUALITYS.add(new String[]{"6", "240P 极速"});
        QUALITYS.add(new String[]{"16", "360P 流畅"});
        QUALITYS.add(new String[]{"32", "480P 清晰"});
        QUALITYS.add(new String[]{"64", "720P 高清"});
        QUALITYS.add(new String[]{"74", "720P60 高清"});
        QUALITYS.add(new String[]{"80", "1080P 高清"});
        QUALITYS.add(new String[]{"112","1080P+ 高清"});
        QUALITYS.add(new String[]{"116", "1080P60 高清"});
        QUALITYS.add(new String[]{"120", "4K 超清"});
    }

    public static List<String[]> getQUALITYS() {
        return QUALITYS;
    }
}
