package com.leon.biuvideo.values;

public enum SearchType {
    VIDEO("video"),
    ARTICLE("article"),
    BILI_USER("bili_user"),
    BANGUMI("media_bangumi");

    public String value;

    SearchType(String value) {
        this.value = value;
    }
}
