package com.leon.biuvideo.values.apis;

/**
 * @Author Leon
 * @Time 2021/5/25
 * @Desc 所有用到的POST操作，POST操作均需要Cookie和csrf
 */
public class BiliBiliPostAPIs {
    /**
     * 添加订阅(有响应数据)
     *
     * 表单数据
     *      season_id:season_id
     *      csrf
     */
    public static final String ADD_ORDER = "https://api.bilibili.com/pgc/web/follow/add";

    /**
     * 删除订阅(有响应数据)
     *
     * 表单数据
     *      season_id:season_id
     *      csrf
     */
    public static final String DEL_ORDER = "https://api.bilibili.com/pgc/web/follow/del";

    /**
     * 点赞
     *
     * 表单数据
     *      aid:视频aid
     *      like:1为点赞，2为取消点赞
     *      csrf
     *
     */
    public static final String VIDEO_LIKE = "https://api.bilibili.com/x/web-interface/archive/like";

    /**
     * 投币操作(有响应数据)
     *
     * 表单数据
     *      aid:视频aid
     *      multiply:硬币个数，只能1和2
     *      select_like:是否同时进行点赞；0为不同时点赞视频，1为同时点赞视频
     *      csrf
     */
    public static final String VIDEO_COIN = "https://api.bilibili.com/x/web-interface/coin/add";

    /**
     * 视频收藏
     *
     * 表单数据
     *      rid: 视频aid
     *      type: 2
     *      add_media_ids: 要添加到的收藏夹ID，用英文逗号隔开
     *      del_media_ids: 要从中删除的收藏夹ID，用英文逗号隔开
     *      csrf
     *      platform: web
     *
     */
    public static final String VIDEO_ADD_FOLDER = "https://api.bilibili.com/x/v3/fav/resource/deal";

    /**
     * 在收藏夹页面中的删除操作
     *
     * 表单数据
     *      resources: 视频aid:2
     *      media_id: 收藏夹ID
     *      platform: web
     *      csrf
     *
     */
    public static final String VIDEO_DEL_FOLDER_WITH_BATCH = "https://api.bilibili.com/x/v3/fav/resource/deal";

    /**
     * 创建收藏夹(有响应数据)
     *
     * 表单数据
     *      title: 标题
     *      privacy: 0；默认为公开
     *      csrf
     */
    public static final String CREATE_FOLDER = "https://api.bilibili.com/x/v3/fav/folder/add";

    /**
     * 删除收藏夹
     *
     * 表单数据
     *      media_ids: 收藏夹ID
     *      platform: web
     *      csrf
     *
     */
    public static final String DEL_FOLDER = "https://api.bilibili.com/x/v3/fav/folder/del";

    /**
     * 编辑收藏夹（有响应数据）
     *
     * 表单数据
     *      title: 标题
     *      intro: 简介
     *      privacy: 1；是否设置为隐私，1为是,0为否
     *      cover:
     *      csrf
     *      media_id: 收藏夹ID
     *
     */
    public static final String EDIT_FOLDER = "https://api.bilibili.com/x/v3/fav/folder/edit";

    /**
     * 与B站用户的关系的操作
     *
     * 表单数据
     *      fid: 用户ID
     *      act: 1:互粉（关注），2:取消互粉（取消关注）
     *      re_src: 11，默认
     *      csrf
     *
     */
    public static final String RELATION_MODIFY = "https://api.bilibili.com/x/relation/modify";

    /**
     * 专栏点赞操作
     *
     * 表单数据
     *      id: 专栏ID
     *      type: 1为点赞，2为取消点赞
     *      csrf
     *
     */
    public static final String ARTICLE_LIKE = "https://api.bilibili.com/x/article/like";

    /**
     * 专栏投币操作
     *
     * 表单数据
     *      aid: 专栏ID
     *      multiply: 1；默认只投一个币
     *      upid: 作者ID
     *      avtype: 2；默认值
     *      csrf
     *
     */
    public static final String ARTICLE_COIN = "https://api.bilibili.com/x/web-interface/coin/add";

    /**
     * 专栏收藏操作
     *
     * 表单数据
     *      id: 专栏ID
     *      csrf
     *
     */
    public static final String ARTICLE_FAV = "https://api.bilibili.com/x/article/favorites/add";

    /**
     * 删除已收藏的专栏
     *
     * 表单数据
     *      id: 专栏ID
     *      csrf
     *
     */
    public static final String ARTICLE_DEL_FAV = "https://api.bilibili.com/x/article/favorites/del";

    /**
     * 删除历史记录
     *
     * 表单数据
     *      kid: kid
     *      csrf
     */
    public static final String HISTORY_DEL = "https://api.bilibili.com/x/v2/history/delete";

    /**
     * 创建频道（有响应数据）
     *
     * 表单数据
     *      name: 标题
     *      intro: 简介
     *      csrf
     *
     */
    public static final String CHANNEL_ADD = "https://api.bilibili.com/x/space/channel/add";

    /**
     * 编辑频道
     *
     * 表单数据
     *      cid: 频道ID
     *      name: 新的标题，如果未改变，则保持不变
     *      intro: 新的简介，如果未改变，则保持不变
     *      csrf
     */
    public static final String CHANNEL_EDIT = "https://api.bilibili.com/x/space/channel/edit";

    /**
     * 删除频道
     *
     * 表单数据
     *      cid: 频道ID
     *      csrf
     *
     */
    public static final String CHANNEL_DEL = "https://api.bilibili.com/x/space/channel/del";

    /**
     * 创建关注分组(有响应数据)
     *
     * 表单数据
     *      tag: 分组名称
     *      csrf
     *
     */
    public static final String RELATION_TAG_CREATE = "https://api.bilibili.com/x/relation/tag/create";

    /**
     * 修改关注分组名称
     *
     * 表单数据
     *      tagid: 分组ID
     *      name: 新的名称
     *      csrf
     *
     */
    public static final String RELATION_TAG_EDIT = "https://api.bilibili.com/x/relation/tag/update";

    /**
     * 删除关注分组
     *
     * 表单数据
     *      tagid: 分组ID
     *      csrf
     *
     */
    public static final String RELATION_TAG_DEL = "https://api.bilibili.com/x/relation/tag/del";
}
