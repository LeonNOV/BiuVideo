package com.leon.biuvideo.beans.userBeans;

/**
 * 观看历史分类
 */
public enum HistoryType {
    VIDEO("archive"),
    ARTICLE("article"),
    LIVE("live");

    public String value;

    HistoryType(String value) {
        this.value = value;
    }
}
