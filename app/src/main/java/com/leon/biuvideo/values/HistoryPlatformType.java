package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2021/3/28
 * @Desc
 */
public enum HistoryPlatformType {
    /**
     * 手机端
     */
    PHOTO(0),

    /**
     * PC/Web端
     */
    PC(1),

    /**
     * Pad端
     */
    PAD(2),

    /**
     * TV端
     */
    TV(3),

    /**
     * 其他平台
     */
    OTHER(4);

    public int value;

    HistoryPlatformType(int value) {
        this.value = value;
    }
}
