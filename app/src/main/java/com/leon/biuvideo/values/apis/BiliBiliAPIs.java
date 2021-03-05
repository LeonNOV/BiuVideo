package com.leon.biuvideo.values.apis;

/**
 * 接口请求地址
 */
public class BiliBiliAPIs {

    /**
     * 获取视频基本信息和所有选集信息<br/>
     *      参数：
     *          bvid：视频ID，必须
     */
    public static String view = "https://api.bilibili.com/x/web-interface/view";

    /**
     * 获取选集信息，返回视频和音频原链接<br/>
     *      参数：
     *          aid：aid，可选
     *          cid：cid，必须
     *          bvid：视频id，可选
     *          其他参数默认即可
     */
    public static String playUrl = "https://api.bilibili.com/x/player/playurl";

    /**
     * 获取番剧选集信息，返回番剧视频和音频原链接<br/>
     *      参数：
     *          cid：cid，必须
     *          其他参数默认即可
     */
    public static String playUrlForBangumi = "https://api.bilibili.com/pgc/player/web/playurl";

    /**
     * 视频播放请求地址<br/>
     * 参数：
     *      aid
     *      cid
     */
    public static String videoBaeUrl = "https://www.bilibili.com/blackboard/html5mobileplayer.html?";

    /**
     * UP主基本信息接口<br/>
     * 参数：
     *  mid：up主id
     */
    public static String info = "https://api.bilibili.com/x/space/acc/info";

    /**
     * 获取指定up的所有视频<br/>
     *     参数：
     *       mid：up主id，必须
     *       ps：视频条目，建议30，必须
     *       pn：视频页码，从1开始，必须
     *       tid：视频类型，0为全部，可选
     *       order：排序方式，pubdate，可选
     *       jsonp：返回类型，可选
     */
    public static String videos = "https://api.bilibili.com/x/space/arc/search";

    /**
     * 获取up音频接口<br/>
     *      参数：
     *          uid：up主id
     *          pn：页码，从1开始
     *          ps：条目数，默认30
     *          order：排序，默认为1
     */
    public static String music = "https://api.bilibili.com/audio/music-service/web/song/upper";

    /**
     * 获取up相簿列表<br/>
     *  uid：up主id
     *  page_num：页码，从0开始，必须
     *  page_size：每页个数，建议30，必须
     *  biz：获取类型，默认为all，必须
     *  	类型：
     *  	  photo（摄影）
     *  	  daily（日常）
     *  	  all（所有）
     */
    public static String picture = "https://api.vc.bilibili.com/link_draw/v1/doc/doc_list";

    /**
     * 相簿原链接<br/>
     *      参数：后跟相簿ID
     */
    public static String pictureWebPage = "https://t.bilibili.com/h5/dynamic/detail/";


    /**
     * 获取相簿总数<br/>
     *      参数
     *          uid：up主id
     */
    public static String pictureCount = "https://api.vc.bilibili.com/link_draw/v1/doc/upload_count";

    /**
     * 歌曲基本信息接口<br/>
     *      参数：
     *          sid：音频id
     */
    public static String musicInfo = "https://www.bilibili.com/audio/music-service-c/web/song/info";

    /**
     *  获取歌曲链接<br/>
     *      参数：
     *          sid：音频sid，必须
     */
    public static String musicUrl = "https://www.bilibili.com/audio/music-service-c/web/url";

    /**
     * 专栏页面链接<br/>
     *      参数
     *          后跟专栏ID（CVxxxxxxx）
     *          from:默认值:category_0,可选
     */
    public static String articleWebPage = "https://www.bilibili.com/read/mobile/";

    /**
     * 专栏列表接口<br/>
     *      参数：
     *          mid：用户id，必须
     *          pn：页码，必须
     *          ps: 条目数，默认12
     */
    public static String article = "https://api.bilibili.com/x/space/article";

    /**
     * 获取Ta的关注列表接口<br/>
     *      参数：
     *          vmid：用户ID，必须
     *          pn：页码，从1开始，必须
     *          ps：获取数量，建议20，必须
     *          order：排序方式，默认DESC，可选
     *
     */
    public static String follow = "https://api.bilibili.com/x/relation/followings";

    /**
     * 搜索接口<br/>
     *      参数：
     *          keyword：关键字，必须进行URLencoding
     *          search_type:搜索类型
     *              video：视频
     *              article：专栏
     *              bili_user：用户
     *          page：页码，从1开始
     *          order：排序方式
     *              default/totalrank：综合排序
     *              pubdate：按发布日期倒序排序
     *              senddate：按修改日期倒序排序
     *              id：按投稿ID倒序排序
     *              ranklevel：按相关度排序
     *              click：按点击从高至低排序
     *              scores：按评论数从高至低排序
     *              damku/dm：按弹幕数从高至低排序
     *              stow：按收藏数从高至低排序
     * 需要在请求头中添加referer，默认为`https://search.bilibili.com`
     */
    public static String search = "https://api.bilibili.com/x/web-interface/search/type";

    /**
     * 获取专栏具体信息的接口<br/>
     *      参数
     *          ids：专栏id，多个参数，需要用URL编码格式的‘,’来分隔(%2C)
     *
     */
    public static String metas = "https://api.bilibili.com/x/article/metas";

    /**
     * 获取‘我的’订阅数据接口<br/>
     * <strong>
     *     该接口是通过开放接口获取数据，如果用户在隐私设置中将'追番追剧'设置为隐藏的话该接口将无效<br/>
     *      将会返回以下数据:
     *           <p>
     *           "code": 53013,
     *           "message": "用户隐私设置未公开",
     *           "ttl": 1
     *           <p/>
     *
     *     如果该接口的返回码为‘53013’，即'追番追剧'设置为隐藏状态，可在请求中加入用户Cookie即可
     * </strong>
     *      参数：
     *          type:获取订阅类型，必须
     *              1：番剧
     *              2：其他剧，不包括标签（纪录片、电视剧等）
     *          follow_status：筛选方式，默认为0，可选
     *              0：全部
     *              1：想看
     *              2：在看
     *              3：看过
     *          pn:页码，从1开始，必须
     *          ps：条目数量，默认为15，必须
     *          vmid：用户ID，必须
     */
    public static String bangumi = "https://api.bilibili.com/x/space/bangumi/follow/list";

    /**
     * 获取番剧观看数、投币数、弹幕数等信息<br/>
     *
     * 参数
     *      season_id:seasonId，必须
     *
     */
    public static String bangumiState = "https://api.bilibili.com/pgc/web/season/stat";

    /**
     * 获取番剧状态信息
     * 参数
     *      后跟番剧MID(MediaId)
     */
    public static String bangumiStateWhiteMid = "https://www.bilibili.com/bangumi/media/md";

    /**
     * 获取番剧各选集cid
     *
     * 参数
     *      season_id：seasonId/sid，必须
     *
     */
    public static String bangumiEpCid = "https://api.bilibili.com/pgc/web/season/section";

    //========================以下接口数据的获取需要在请求头中添加Cookie========================

    /**
     * 获取用户信息
     * 不需要任何参数
     * 必须要设置Cookie信息
     */
    public static String nav = "https://api.bilibili.com/x/web-interface/nav";

    /**
     * 获取用户历史记录,默认为20条
     *      参数：
     *          max：第一页为0.后面几页则需要第一页中JSONArray(list)中最后一条kid的值
     *          view_at：第一页为0，后面几页需要第一页中JSONArray(list)其中一条的view_at的值(默认为最后一条)
     *          type：分类，下面三个中其中一个值即可，不添加则默认为视频
     *              archive：视频
     *              live：直播
     *              article：专栏
     */
    public static String history = "https://api.bilibili.com/x/web-interface/history/cursor";

    /**
     * 获取<strong>单个专栏<strong/>的信息
     * Cookie可有可无
     *
     *      参数：
     *          id：专栏ID
     */
    public static String articleInfo = "https://api.bilibili.com/x/article/viewinfo";

    /**
     * 获取用户收藏的所有专栏<br/>
     * 该接口数据只能通过用户Cookie获取
     * 参数：
     *      pn：页码，从1开始，必须
     *      ps：条目数，默认为16条，可选
     *
     */
    public static String userArticle = "https://api.bilibili.com/x/article/favorites/list/all";

    /**
     * 获取用户所有收藏夹<br/>
     * 在没有Cookie的情况下，需要保证隐私设置中的‘我的收藏’处于公开状态<br/>
     * 有Cookie的情况下，只需添加请求头即可<br/>
     *  参数：
     *      up_mid：用户ID，必须
     */
    public static String userAllFolder = "https://api.bilibili.com/x/v3/fav/folder/created/list-all";

    /**
     * 获取收藏夹中的所有数据
     * 使用方式和‘userAllFolder’一样
     * 参数：
     *      media_id：收藏夹ID，必须
     *      pn：页码，从1开始，必须
     *      ps：条目数，默认20，必须
     *      order：排序方式
     *          view：最多播放
     *          mtime：最近收藏
     *          pubtime：最新投稿
     *      type：还不知道是啥，默认为0
     *      tid：分区ID，该值默认为0
     */
    public static String userFolderData = "https://api.bilibili.com/x/v3/fav/resource/list";
}