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

    public static LocalOrderType getType(int typeNum) {
        LocalOrderType localOrderType = null;
        switch (typeNum) {
            case 0:
                localOrderType = LocalOrderType.VIDEO;
                break;
            case 1:
                localOrderType = LocalOrderType.BANGUMI;
                break;
            case 2:
                localOrderType = LocalOrderType.AUDIO;
                break;
            case 3:
                localOrderType = LocalOrderType.ARTICLE;
                break;
            default:
                break;
        }

        return localOrderType;
    }
}
