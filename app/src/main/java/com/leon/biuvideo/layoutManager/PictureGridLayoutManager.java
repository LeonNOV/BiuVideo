package com.leon.biuvideo.layoutManager;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

public class PictureGridLayoutManager extends GridLayoutManager {
    public PictureGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    /**
     * 设置GridLayoutManager为不可滑动
     * 由于官方设置的上传图片数量就为9张，所以这里可以直接设置为false
     *
     * @return  返回false
     */
    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
