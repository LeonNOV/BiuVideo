package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2021/4/13
 * @Desc Fragment类型，用于启动，“公共”Fragment
 */
public enum  FragmentType {
    /**
     * 用户页面
     */
    BILI_USER(0),

    /**
     * 视频页面
     */
    VIDEO(1),

    /**
     * 番剧页面
     */
    BANGUMI(2),

    /**
     * 音频页面
     */
    AUDIO(3),

    /**
     * 专栏页面
     */
    ARTICLE(4),

    /**
     * 相簿页面
     */
    PICTURE(5);

    public int value;

    FragmentType(int value) {
        this.value = value;
    }
}
