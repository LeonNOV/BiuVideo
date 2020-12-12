package com.leon.biuvideo.beans.userBeans;

public enum HistoryType {
    VIDEO("archive"),
    ARTICLE("article"),
    LIVE("live");

    public String value;

    HistoryType(String value) {
        this.value = value;
    }
}
