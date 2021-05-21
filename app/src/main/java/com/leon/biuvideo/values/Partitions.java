package com.leon.biuvideo.values;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.Partition;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 分区
 */
public enum Partitions {

    /**
     * 动物圈
     */
    ANIMAL("animal"),

    /**
     * 动画
     */
    DOUGA("douga"),

    /**
     * 国创
     */
    GUOCHUANG("guochuang"),

    /**
     * 鬼畜
     */
    KICHIKU("kichiku"),

    /**
     * 音乐
     */
    MUSIC("music"),

    /**
     * 时尚
     */
    FASHION("fashion"),

    /**
     * 资讯
     */
    INFORMATION("information"),

    /**
     * 游戏
     */
    GAME("game"),

    /**
     * 娱乐
     */
    ENT("ent"),

    /**
     * 知识
     */
    TECHNOLOGY("technology"),

    /**
     * 影视
     */
    CINEPHILE("cinephile"),

    /**
     * 数码
     */
    DIGITAL("digital"),

    /**
     * 生活
     */
    LIFE("life"),

    /**
     * 番剧
     */
    ANIME("anime"),

    /**
     * 纪录片
     */
    DOCUMENTARY("documentary"),

    /**
     * 电影
     */
    MOVIE("movie"),

    /**
     * 电视剧
     */
    TELEPLAY("teleplay"),

    /**
     * 专栏
     */
    ARTICLE("article"),

    /**
     * 舞蹈
     */
    DANCE("dance"),

    /**
     * 美食
     */
    FOOD("food");

    public String value;

    Partitions(String value) {
        this.value = value;
    }

    /**
     * 获取分区中文名称
     *
     * @param partitions    partitions
     * @return  中文名称
     */
    public static String getName(Partitions partitions) {
        switch (partitions) {
            case ANIMAL:
                return "动物圈";

            case DOUGA:
                return "动画";

            case GUOCHUANG:
                return "国创";

            case KICHIKU:
                return "鬼畜";

            case MUSIC:
                return "音乐";

            case FASHION:
                return "时尚";

            case INFORMATION:
                return "资讯";

            case GAME:
                return "游戏";

            case ENT:
                return "娱乐";

            case TECHNOLOGY:
                return "知识";

            case CINEPHILE:
                return "影视";

            case DIGITAL:
                return "数码";

            case LIFE:
                return "生活";

            case ANIME:
                return "番剧";

            case DOCUMENTARY:
                return "纪录片";

            case MOVIE:
                return "电影";

            case TELEPLAY:
                return "电视剧";

            case ARTICLE:
                return "专栏";

            case FOOD:
                return "美食";

            case DANCE:
                return "舞蹈";

            default:
                return "错误分区";
        }
    }

    public static JSONObject PARTITION;

    /**
     * 获取分区详细数据
     *
     * @param partitions    partitions
     * @return  Partition集合
     */
    public static List<Partition> getSubPartition(Partitions partitions) {
        JSONArray jsonArray = PARTITION.getJSONArray(partitions.value);
        return jsonArray.toJavaList(Partition.class);
    }
}
