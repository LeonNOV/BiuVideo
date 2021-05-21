package com.leon.biuvideo.values;

/**
 * @Author Leon
 * @Time 2021/3/3
 * @Desc 设置界面 功能名称
 */
public enum FeaturesName {
    /**
     * 原图模式
     */
    IMG_ORIGINAL_MODEL(0),

    /**
     * 天气模块
     */
    WEATHER_MODEL(1);

    public int value;

    FeaturesName(int value) {
        this.value = value;
    }
}
