package com.leon.biuvideo.ui.mainFragments;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * @Author Leon
 * @Time 2021/3/3
 * @Desc 主页模块接口
 */
public interface HomeModel {
    /**
     * 初始化模块
     */
    void onInitialize(View view, Context context);

    /**
     * 刷新模块信息
     */
    void onRefresh(Serializable serializable);

    /**
     * 设置模块为显示或隐藏
     */
    void setDisplayState(boolean isDisplay);
}
