package com.leon.biuvideo.ui.resourcesFragment.video;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc
 */
public interface OnVideoAnthologyListener {

    /**
     * 切换选集
     *
     * @param cid   选集ICD
     * @param title 选集标题
     */
    void onAnthology(String cid, String title);
}
