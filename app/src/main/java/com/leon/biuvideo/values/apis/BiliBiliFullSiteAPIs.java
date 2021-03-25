package com.leon.biuvideo.values.apis;

/**
 * @Author Leon
 * @Time 2021/3/25
 * @Desc B站全站排行榜接口<br/>
 *      接口使用示例：<br/>
 *      RANKING_V2:https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=all<br/>
 *      WEB_LIST:https://api.bilibili.com/pgc/season/rank/web/list?day=3&season_type=4<br/>
 *      BANGUMI:该API直接调用即可，不用其他参数<br/>
 *      具体使用可看项目根目录下的`LeaderboardInterface.md`文件
 */
public class BiliBiliFullSiteAPIs {

    /**
     * 和RANKING_V2接口配合使用
     */
    public enum FullSiteRids {
        /**
         * 全站
         */
        ALL(0),

        /**
         * 国创相关
         */
        GUOCHUANG(168),

        /**
         * 动画
         */
        DOUGA(1),

        /**
         * 音乐
         */
        MUSIC(3),

        /**
         * 舞蹈
         */
        DANCE(129),

        /**
         * 游戏
         */
        GAME(4),

        /**
         * 知识
         */
        TECHNOLOGY(36),

        /**
         * 数码
         */
        DIGITAL(188),

        /**
         * 生活
         */
        LIFE(160),

        /**
         * 美食
         */
        FOOD(211),

        /**
         * 动物圈
         */
        ANIMAL(217),

        /**
         * 鬼畜
         */
        KICHIKU(119),

        /**
         * 时尚
         */
        FASHION(155),

        /**
         * 娱乐
         */
        ENT(5),

        /**
         * 影视
         */
        CINEPHILE(181);

        public int value;

        FullSiteRids(int value) {
            this.value = value;
        }
    }

    /**
     * 和RANKING_V2接口配合使用
     */
    public enum RANKING_V2_TYPE {
        /**
         * 配合RANKING_V2和FullSiteRids使用
         */
        ALL("all"),

        /**
         * 使用该接口时，rid的值为0
         */
        ORIGIN("origin"),

        /**
         * 使用该接口时，rid的值为0
         */
        ROOKIE("rookie");

        public String value;

        RANKING_V2_TYPE(String value) {
            this.value = value;
        }
    }

    /**
     * 配合WEB_LIST使用
     */
    public enum SeasonType {
        /**
         * 国产动画
         */
        GUOCHAN(4),

        /**
         * 纪录片
         */
        DOCUMENTARY(3),

        /**
         * 电影
         */
        MOVIE(2),

        /**
         * 电视剧
         */
        TELEPLAY(5);

        public int value;

        SeasonType(int value) {
            this.value = value;
        }
    }

    /**
     * 国创相关、动画、音乐、舞蹈、游戏、知识、数码、生活、美食、动物圈、鬼畜、时尚、娱乐、影视、原创、新人
     */
    public static final String RANKING_V2 = "https://api.bilibili.com/x/web-interface/ranking/v2";

    /**
     * 国产动画、纪录片、电影、电视剧
     */
    public static final String WEB_LIST = "https://api.bilibili.com/pgc/season/rank/web/list";

    /**
     * 番剧
     */
    public static final String BANGUMI = "https://api.bilibili.com/pgc/web/rank/list?day=3&season_type=1";
}
