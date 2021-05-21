package com.leon.biuvideo.beans.homeBeans.favoriteBeans;

import java.io.Serializable;

/**
 * @Author Leon
 * @Time 2020/11/8
 * @Desc 收藏的专栏数据
 */
public class FavoriteArticle implements Serializable {
    public long mid;
    public String face;
    public long articleId;
    public String title;
    public String summary;
    public String author;
    public String coverUrl;

    public String category;

    /**
     * 秒
     */
    public long ctime;

    /**
     * 收藏时间（秒）
     */
    public long favoriteTime;

//    public int coin;
    public int view;
//    public int share;
//    public int favorite;
    public int like;
    public int reply;
}
