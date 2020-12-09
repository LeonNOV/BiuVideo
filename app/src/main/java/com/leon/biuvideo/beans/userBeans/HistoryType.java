package com.leon.biuvideo.beans.userBeans;

public enum HistoryType {
    ARCHIVE("archive"),
    ARTICLE("article"),
    LIVE("live");

    public String value;

    HistoryType(String value) {
        this.value = value;
    }
}
