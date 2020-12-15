package com.leon.biuvideo.beans.articleBeans;

import java.io.Serializable;

/**
 * 专栏相关数据
 */
public class Article implements Serializable {
    public long mid;
    public String face;
    public long articleId;
    public String title;
    public String summary;
    public String author;
    public String coverUrl;

    public String category;
    public long ctime;//秒
    public long favoriteTime;//收藏时间（秒）

//    public int coin;
    public int view;
//    public int share;
//    public int favorite;
    public int like;
    public int reply;
}
