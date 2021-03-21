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
    BANGUMI(1),

    /**
     * 剧集
     */
    SERIES(2),

    /**
     * 标签
     */
    TAG(3);

    public int value;

    OrderType(int value) {
        this.value = value;
    }
}
