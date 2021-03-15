package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2020/11/22
 * @Desc 订阅类型
 */
public enum OrderType {
    /**
     * 番剧
     */
    BANGUMI(0),

    /**
     * 剧集
     */
    SERIES(1),

    /**
     * 标签
     */
    TAG(2);

    public int value;

    OrderType(int value) {
        this.value = value;
    }
}
