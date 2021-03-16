package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2021/3/16
 * @Desc 用户类型
 */
public enum Role {
    /**
     * 未认证
     */
    NONE(0),

    /**
     * 个人认证
     */
    PERSON(1),

    /**
     * 机构/官方认证
     */
    OFFICIAL(2);

    public int value;

    Role(int value) {
        this.value = value;
    }
}
