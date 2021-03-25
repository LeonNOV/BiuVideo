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
* 全站
**/
public static final String ALL = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=all";

/**
* 番剧
**/
public static final String BANGUMI = "https://api.bilibili.com/pgc/web/rank/list?day=3&season_type=1";

/**
* 国产动画
**/
public static final String GUOCHAN = "https://api.bilibili.com/pgc/season/rank/web/list?day=3&season_type=4";

/**
* 国创相关
**/
public static final String GUOCHUANG = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=168&type=all";

/**
* 纪录片
**/
public static final String DOCUMENTARY = "https://api.bilibili.com/pgc/season/rank/web/list?day=3&season_type=3";

/**
* 动画
**/
public static final String DOUGA = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=1&type=all";

/**
* 音乐
**/
public static final String MUSIC = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=3&type=all";

/**
* 舞蹈
**/
public static final String DANCE = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=129&type=all";

/**
* 游戏
**/
public static final String GAME = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=4&type=all";

/**
* 知识
**/
public static final String TECHNOLOGY = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=36&type=all";

/**
* 数码
**/
public static final String DIGITAL = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=188&type=all";

/**
* 生活
**/
public static final String LIFE = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=160&type=all";

/**
* 美食
**/
public static final String FOOD = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=211&type=all";

/**
* 动物圈
**/
public static final String ANIMAL = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=217&type=all";

/**
* 鬼畜
**/
public static final String KICHIKU = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=119&type=all";

/**
* 时尚
**/
public static final String FASHION = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=155&type=all";

/**
* 娱乐
**/
public static final String ENT = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=5&type=all";

/**
* 影视
**/
public static final String CINEPHILE = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=181&type=all";

/**
* 电影
**/
public static final String MOVIE = "https://api.bilibili.com/pgc/season/rank/web/list?day=3&season_type=2";

/**
* 电视剧
**/
public static final String TELEPLAY = "https://api.bilibili.com/pgc/season/rank/web/list?day=3&season_type=5";

/**
* 原创
**/
public static final String ORIGIN = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=origin";

/**
* 新人
**/
public static final String ROOKIE = "https://api.bilibili.com/x/web-interface/ranking/v2?rid=0&type=rookie";
}
