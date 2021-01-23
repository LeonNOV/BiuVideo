package com.leon.biuvideo.values;

public enum OrderType {
    VIDEO(0),
    BANGUMI(1),
    SERIES(2),
    ARTICLE(3);

    public int value;

    OrderType(int value) {
        this.value = value;
    }
}
