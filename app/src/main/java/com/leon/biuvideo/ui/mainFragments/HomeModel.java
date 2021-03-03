package com.leon.biuvideo.ui.mainFragments;

import android.view.View;

import java.io.Serializable;

public interface HomeModel {
    /**
     * 初始化模块
     */
    void onInitialize(View view);

    /**
     * 刷新模块信息
     */
    void onRefresh(Serializable serializable);

    /**
     * 设置模块为显示或隐藏
     */
    void setDisplayState(boolean isDisplay);
}
