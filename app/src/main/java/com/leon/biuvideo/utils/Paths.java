package com.leon.biuvideo.utils;

public class Paths {
    public static String view = "https://api.bilibili.com/x/web-interface/view";
    public static String playUrl = "https://api.bilibili.com/x/player/playurl";

    //视频播放请求地址
    public static String videoBaeUrl = "https://www.bilibili.com/blackboard/html5mobileplayer.html?";

    //UP主基本信息接口
    //参数：
    // mid：up主id
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
     * 该接口需要知道动态的类型，百度找不到，所以，弃坑。。。
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
}
