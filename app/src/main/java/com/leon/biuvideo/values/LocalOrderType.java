package com.leon.biuvideo.values;

public enum LocalOrderType {
    VIDEO(0),
    BANGUMI(1),
    AUDIO(2),
    ARTICLE(3);

    public int value;

    LocalOrderType(int value) {
        this.value = value;
    }
}
