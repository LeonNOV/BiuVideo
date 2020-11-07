package com.leon.biuvideo.beans.articleBeans;

import java.io.Serializable;

public class Article implements Serializable {

    public long articleID;
    public String title;
    public String summary;
    public String author;
    public String coverUrl;

    public String category;
    public long ctime;//秒

    public int coin;
    public int view;
    public int share;
    public int favorite;
    public int like;
    public int replay;
}
