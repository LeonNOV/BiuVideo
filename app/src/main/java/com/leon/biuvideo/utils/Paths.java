package com.leon.biuvideo.utils;

/**
 * 接口请求地址
 */
public class Paths {
    /**
     * 获取视频基本信息和所有选集信息
     *      参数：
     *          bvid：视频ID，必须
     */
    public static String view = "https://api.bilibili.com/x/web-interface/view";

    /**
     * 获取选集信息，返回视频和音频原链接
     *      参数：
     *          aid：aid，必须
     *          cid：cid，必须
     *          bvid：视频id，必须
     *          其他参数默认即可
     */
    public static String playUrl = "https://api.bilibili.com/x/player/playurl";

    /**
     * 视频播放请求地址
     * 参数：
     *      aid
     *      cid
     */
    public static String videoBaeUrl = "https://www.bilibili.com/blackboard/html5mobileplayer.html?";

    /**
     * UP主基本信息接口
     * 参数：
     *  mid：up主id
     */
    public static String info = "https://api.bilibili.com/x/space/acc/info";

    /**
     * 获取指定up的所有视频
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
     * 该接口需要知道用户所发动态的类型，百度找不到，所以，弃坑。。。
     * 获取相簿列表
     *      参数：
     *          visitor_uid：查看者id，默认为0
     *          host_uid：up主id
     *          offset_dynamic_id：偏移id，第一页为0，后面调用需要获取最后一个条目的dynamic_id_str的值
     *          need_top：默认为0
     */
    public static String space = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history";

    /**
     * 获取up音频接口
     *      参数：
     *          uid：up主id
     *          pn：页码，从1开始
     *          ps：条目数，默认30
     *          order：排序，默认为1
     */
    public static String music = "https://api.bilibili.com/audio/music-service/web/song/upper";

    /**
     * 获取up相簿列表
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
     * 获取相簿总数
     *      参数
     *          uid：up主id
     */
    public static String pictureCount = "https://api.vc.bilibili.com/link_draw/v1/doc/upload_count";

    /**
     * 歌曲基本信息接口
     *      参数：
     *          sid：音频id
     */
    public static String musicInfo = "https://www.bilibili.com/audio/music-service-c/web/song/info";

    /**
     *  获取歌曲链接
     *      参数：
     *          sid：音频sid，必须
     */
    public static String musicUrl = "https://www.bilibili.com/audio/music-service-c/web/url";

    /**
     * 专栏页面链接
     *      参数
     *          后跟专栏ID（CVxxxxxxx）
     *          from:默认值:category_0,可选
     */
    public static String articleWebPage = "https://www.bilibili.com/read/mobile/";
//    public static String article = "https://www.bilibili.com/read/cv";

    /**
     * 专栏列表接口
     *      参数：
     *          mid：用户id，必须
     *          pn：页码，必须
     */
    public static String article = "https://api.bilibili.com/x/space/article";
}
