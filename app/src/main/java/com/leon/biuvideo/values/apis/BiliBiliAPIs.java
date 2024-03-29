package com.leon.biuvideo.values.apis;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 该项目所用到的部分B站开放API，该类中的部分API参考自[https://github.com/SocialSisterYi/bilibili-API-collect]
 */
public class BiliBiliAPIs {

    /**
     * 获取视频基本信息和所有选集信息<br/>
     *      参数：
     *          bvid：视频ID，必须
     */
    public static final String VIDEO_DETAIL_INFO = "https://api.bilibili.com/x/web-interface/view";

    /**
     * 获取VIP状态
     *
     * 只需Cookie
     */
    public static final String USER_VIP_STAT = "https://api.bilibili.com/x/vip/web/user/info";

    /**
     * 获取视频tag(频道)
     * 参数
     *      aid：aid/bvid任选其一
     *      bvid：aid/bvid任选其一
     */
    public static final String VIDEO_DETAIL_TAGS = "https://api.bilibili.com/x/web-interface/view/detail/tag";

    /**
     * 获取当前视频与已登录账户的关系（需要Cookie）
     * 参数
     *      bvid/avid
     *
     */
    public static final String VIDEO_STATUS = "https://api.bilibili.com/x/web-interface/archive/relation";

    /**
     * 获取推荐视频数据
     * 参数
     *      aid/bvid
     *
     */
    public static final String VIDEO_RECOMMENDS = "http://api.bilibili.com/x/web-interface/archive/related";

    /**
     * 获取番剧推荐数据
     * 参数
     *      seasonId
     */
    public static final String BANGUMI_RECOMMENDS = "https://api.bilibili.com/pgc/season/web/related/recommend?season_id=36167";

    /**
     * 获取选集信息，返回视频和音频原链接<br/>
     *      参数：
     *          aid：aid，可选
     *          cid：cid，必须
     *          bvid：视频id，可选
     *          其他参数默认即可
     */
    public static final String VIDEO_STREAM_INFO = "https://api.bilibili.com/x/player/playurl";

    /**
     * 获取番剧选集信息，返回番剧视频和音频原链接<br/>
     *      参数：
     *          cid：cid，必须
     *          其他参数默认即可
     */
    public static final String BANGUMI_STREAM_INFO = "https://api.bilibili.com/pgc/player/web/playurl";

    /**
     * 获取视频的弹幕数据
     * 参数
     *      oid：视频CID
     */
    public static final String DANMAKU = "http://api.bilibili.com/x/v1/dm/list.so";

    /**
     * 获取B站用户信息<br/>
     * 参数
     *      mid
     */
    public static final String BILI_USER_INFO = "https://api.bilibili.com/x/space/acc/info";

    /**
     * 获取B站用户获赞数、播放数和阅读数（需要Cookie）
     * 参数
     *      mid
     *
     */
    public static final String BILI_USER_UP_STATUS = "http://api.bilibili.com/x/space/upstat";

    /**
     * 获取B站用户关注数和粉丝数，参数同上（不需要Cookie）
     */
    public static final String BILI_USER_STATUS = "https://api.bilibili.com/x/relation/stat";

    /**
     * 获取评论数据
     * 参数
     *      type:评论区类型代码
     *          代码	    评论区类型	    oid的意义
     *          1	    视频稿件	        稿件avID
     *          2	    话题	            话题ID
     *          4	    活动	            活动ID
     *          5	    小视频	        小视频ID
     *          6	    小黑屋封禁信息	    封禁公示ID
     *          7	    公告信息	        公告ID
     *          8	    直播活动	        直播间ID
     *          9	    活动稿件
     *          10	    直播公告
     *          11	    相簿（图片动态）	相簿ID
     *          12	    专栏	            专栏cvID
     *          13	    票务
     *          14	    音频	            音频auID
     *          15	    风纪委员会	    众裁项目ID
     *          16	    点评
     *          17	    动态（纯文字动态&分享）	动态ID
     *          18	    播单
     *          19	    音乐播单
     *          20	    漫画
     *          21	    漫画
     *          22	    漫画	            漫画mcID
     *          33	    课程	            课程epID
     *      oid:目标ID
     *          视频aid/专栏id/相册id等
     *      pn:页码，从1开始
     *      ps:单页条目数，默认20
     *      sort:排序方式
     *          0：按时间
     *          1：按点赞数
     *          2：按回复数
     *      nohot:是否显示热捧，默认不显示
     *
     */
    public static final String COMMENT = "http://api.bilibili.com/x/v2/reply/main";

    /**
     * 获取一级评论下的所有回复数据
     * 参数
     *      type：同上
     *      oid:同上
     *      root：一级评论ID/rpid
     *      pn/ps：同上
     *
     */
    public static final String COMMENT_DETAIL = "https://api.bilibili.com/x/v2/reply/reply";

    /**
     * 获取指定up的所有视频<br/>
     *     参数：
     *       mid：up主id，必须
     *       ps：视频条目，建议30，必须
     *       pn：视频页码，从1开始，必须
     *       order：排序方式，默认为pubdate，可选
     *          最新发布：pubdate
     *          最多播放：click
     *          最多收藏：stow
     */
    public static final String BILI_USER_VIDEO = "https://api.bilibili.com/x/space/arc/search";

    /**
     * 专栏列表接口<br/>
     *      参数：
     *          mid：用户id，必须
     *          pn：页码，必须
     *          ps: 条目数，默认12
     */
    public static final String BILI_USER_ARTICLE = "https://api.bilibili.com/x/space/article";

    /**
     * 获取up音频接口<br/>
     *      参数：
     *          uid：up主id
     *          pn：页码，从1开始
     *          ps：条目数，默认30
     *          order：排序，默认为1
     */
    public static final String BILI_USER_AUDIO = "https://api.bilibili.com/audio/music-service/web/song/upper";

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
    public static final String BILI_USER_PICTURE = "https://api.vc.bilibili.com/link_draw/v1/doc/doc_list";

    /**
     * 获取相簿总数<br/>
     *      参数
     *          uid：up主id
     */
    public static final String BILI_USER_PICTURE_TOTAL = "https://api.vc.bilibili.com/link_draw/v1/doc/upload_count";

    /**
     * 获取<strong>单个专栏<strong/>的信息
     *
     *      参数：
     *          id：专栏ID
     */
    public static final String ARTICLE_INFO = "https://api.bilibili.com/x/article/viewinfo";

    /**
     * 获取相簿详细信息
     * 参数
     *      rid：相簿ID
     *      type：默认为2
     */
    public static final String BILI_USER_PICTURE_DETAIL = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/get_dynamic_detail";

    /**
     * 歌曲基本信息接口<br/>
     *      参数：
     *          sid：音频id
     */
    public static final String AUDIO_INFO = "https://www.bilibili.com/audio/music-service-c/web/song/info";

    /**
     *  获取歌曲链接<br/>
     *      参数：
     *          sid：音频sid，必须
     */
    public static final String AUDIO_STREAM_URL = "https://www.bilibili.com/audio/music-service-c/web/url";

    /**
     * 专栏页面链接<br/>
     *      参数
     *          后跟专栏ID（CVxxxxxxx）
     *          from:默认值:category_0,可选
     */
    public static final String ARTICLE_PAGE_PATH = "https://www.bilibili.com/read/mobile/";

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
    public static final String SEARCH_WITH_TYPE = "https://api.bilibili.com/x/web-interface/search/type";

    /**
     * 获取专栏具体信息的接口<br/>
     *      参数
     *          ids：专栏id，多个参数，需要用URL编码格式的‘,’来分隔(%2C)
     *
     */
    public static final String METAS = "https://api.bilibili.com/x/article/metas";

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
     *              2：剧集
     *          follow_status：筛选方式，默认为0，可选
     *              0：全部
     *              1：想看
     *              2：在看
     *              3：看过
     *          pn:页码，从1开始，必须
     *          ps：条目数量，默认为15，必须
     *          vmid：用户ID，必须
     */
    public static final String ORDER_LIST = "https://api.bilibili.com/x/space/bangumi/follow/list";

    /**
     * 获取订阅的标签内容（需要Cookie）
     * 参数：
     *      vmid：用户ID
     */
    public static final String ORDER_TAG = "https://api.bilibili.com/x/space/tag/sub/list";

    /**
     * 获取番剧状态信息
     * 参数
     *      后跟番剧MID(MediaId)
     */
    public static final String BANGUMI_STATE_WHITE_MID = "https://www.bilibili.com/bangumi/media/md";

    /**
     * 获取番剧各选集cid
     *
     * 参数
     *      season_id：seasonId/sid，必须
     *
     */
    public static final String BANGUMI_EP_CID = "https://api.bilibili.com/pgc/web/season/section";

    /**
     * 获取 番剧/电影/电视剧/纪录片/综艺 详细信息
     * 参数（二选一）
     *      season_id：season_id与ep_id任选其一
     *      ep_id：season_id与ep_id任选其一
     *
     */
    public static final String BANGUMI_DETAIL = "http://api.bilibili.com/pgc/view/web/season";

    /**
     * 获取用户与番剧的状态，如：点赞状态、投币状态等（用户状态数需要cookie，否则默认为0）
     * <br/><strong>该接口是与番剧/电视剧等的每一集对应的（每切换一集都需要调用该接口）</strong>
     * 参数：
     *      ep_id：ep_id
     */
    public static final String BANGUMI_SERIES_STAT = "https://api.bilibili.com/pgc/season/episode/web/info";

    /**
     * 获取用户追番的状态（需要Cookie和Referer（默认值即可））
     * 参数
     *      season_id:seasonId
     */
    public static final String BANGUMI_STATUS = "https://api.bilibili.com/pgc/view/web/season/user/status";

    /**
     * 获取分区的子分区数据
     * 参数
     *      main_ver：默认v3
     *      search_type：默认video
     *      view_type：默认为hot_rank
     *      cate_id：子分区ID
     *      copy_right：是否为原创；-1为全部，1为原创
     *      order：排序方式
     *          click：播放数
     *          scores：评论数
     *          stow：收藏数
     *          coin：硬币数
     *          dm：弹幕数
     *      page：页码，从1开始
     *      pagesize：单页条目数，20
     *      time_from：日期起始时间，20210316
     *      time_to：日期结束时间，20210323
     *          time_from和time_to，默认相差7天
     *      keyword：关键字，需要进行URL编码（可选）
     */
    public static final String PARTITION = "https://s.search.bilibili.com/cate/search";

    /**
     *
     *
     */
    public static final String HOT_LIST = "https://api.bilibili.com/x/web-interface/popular";

    /**
     * 获取每周必看往期目录数据，该接口不需要任何参数
     */
    public static final String POPULAR_WEEKLY_SERIES = "https://api.bilibili.com/x/web-interface/popular/series/list";

    /**
     * 获取每周必看数据
     * 参数
     *      number：期数
     *
     */
    public static final String POPULAR_WEEKLY_DATA = "https://api.bilibili.com/x/web-interface/popular/series/one";

    /**
     * 获取入站必刷数据
     * 参数
     *      page_size：默认100（可选）
     *      page：从1开始（可选）
     *
     */
    public static final String POPULAR_PRECIOUS = "https://api.bilibili.com/x/web-interface/popular/precious";

    //========================以下接口数据的获取需要在请求头中添加Cookie========================

    /**
     * 获取用户信息1
     * 不需要任何参数
     * 必须要设置Cookie信息
     */
    public static final String USER_BASE_INFO = "http://api.bilibili.com/x/space/myinfo";

    /**
     * 获取用户数据2
     * 参数
     *      mid：用户ID
     *      photo：是否含有顶部横幅图片
     */
    public static final String USER_CARD = "http://api.bilibili.com/x/web-interface/card";

    /**
     * B币、贝壳查询接口（需要Cookie）
     * 使用默认参数
     */
    public static final String USER_WALLET = "https://pay.bilibili.com/payplatform/getUserWalletInfo?platformType=3";

    /**
     * 获取用户粉丝接口
     * 参数：
     *      vmid：用户ID
     *      pn：页码，从1开始
     *      ps：条目数，默认20
     *
     */
    public static final String USER_FOLLOWERS = "https://api.bilibili.com/x/relation/followers";

    /**
     * 获取Ta的关注列表接口<br/>
     *      参数：
     *          vmid：用户ID，必须
     *          pn：页码，从1开始，必须
     *          ps：获取数量，建议20，必须
     *          order：排序方式，默认DESC，可选
     * 请求信息
     *  Referer：https://space.bilibili.com/
     *
     */
    public static final String USER_FOLLOWINGS = "https://api.bilibili.com/x/relation/followings";

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
    public static String HISTORY = "https://api.bilibili.com/x/web-interface/history/cursor";

    /**
     * 获取用户收藏的所有专栏<br/>
     * 该接口数据只能通过用户Cookie获取
     * 参数：
     *      pn：页码，从1开始，必须
     *      ps：条目数，默认为16条，可选
     *
     */
    public static final String USER_ARTICLE = "https://api.bilibili.com/x/article/favorites/list/all";

    /**
     * 获取用户所有收藏夹<br/>
     * 在没有Cookie的情况下，需要保证隐私设置中的‘我的收藏’处于公开状态<br/>
     * 有Cookie的情况下，只需添加请求头即可<br/>
     *  参数：
     *      up_mid：用户ID，必须
     */
    public static String USER_FAV_FOLDER = "https://api.bilibili.com/x/v3/fav/folder/created/list-all";

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
    public static String USER_FAV_FOLDER_DETAIL = "https://api.bilibili.com/x/v3/fav/resource/list";

    /**
     * 内容推荐接口
     * 如果没有登录账户的话只能获取系统推荐的内容
     * 如果登录上的话，需要在请求头加入Cookie数据，即可获取个人推荐数据
     *
     */
    public static final String RECOMMEND = "https://www.bilibili.com/index/ding.json";

    /**
     * 热搜榜接口
     *
     * 参数：
     *     build：可为0
     *     limit：默认为10，且只能为10
     *
     */
    public static final String HOT_SEARCH_LIST = "https://app.bilibili.com/x/v2/search/square";

    /**
     * 稍后观看接口（需要Cookie）
     */
    public static final String WATCH_LATER = "https://api.bilibili.com/x/v2/history/toview/web";
}
