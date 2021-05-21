package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc 推荐分类
 */
public enum RecommendType {
    // 动画
    DOUGA(0),

    // 电视剧
    TELEPLAY(1),

    // 鬼畜
    KICHIKU(2),

    // 舞蹈
    DANCE(3),

    // 动漫
    BANGUMI(4),

    // 时尚
    FASHION(5),

    // 生活
    LIFE(6),

    // 国创
    GUOCHUANG(7),

    // 电影
    MOVIE(8),

    // 音乐
    MUSIC(9),

    // 科技/知识
    TECHNOLOGY(10),

    // 游戏
    GAME(11),

    // 娱乐
    ENT(12),

    // 未分类
    NONE(13);

    public int value;

    RecommendType(int value) {
        this.value = value;
    }

    /**
     * 根据字符串返回RecommendType
     *
     * @param type  字符串类型
     * @return  RecommendType
     */
    public static RecommendType getRecommendType(String type) {
        switch (type) {
            case "douga":
                return DOUGA;
            case "teleplay":
                return TELEPLAY;
            case "kichiku":
                return KICHIKU;
            case "dance":
                return DANCE;
            case "bangumi":
                return BANGUMI;
            case "fashion":
                return FASHION;
            case "life":
                return LIFE;
            case "guochuang":
                return GUOCHUANG;
            case "movie":
                return MOVIE;
            case "technology":
                return TECHNOLOGY;
            case "game":
                return GAME;
            case "ent":
                return ENT;
            default:
                return NONE;
        }
    }
}
