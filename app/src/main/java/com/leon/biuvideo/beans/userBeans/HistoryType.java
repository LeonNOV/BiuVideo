package com.leon.biuvideo.beans.userBeans;

/**
 * @Author Leon
 * @Time 2020/12/9
 * @Desc 历史记录类型
 */
public enum HistoryType {
    /**
     * 视频/稿件
     */
    VIDEO("archive"),

    /**
     * 番剧/影视
     */
    BANGUMI("bangumi"),

    /**
     * 文章/文集
     */
    ARTICLE("article"),

    /**
     * 直播
     */
    LIVE("live");

    public String value;

    HistoryType(String value) {
        this.value = value;
    }
}
