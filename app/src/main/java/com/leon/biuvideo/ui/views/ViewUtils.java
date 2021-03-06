package com.leon.biuvideo.ui.views;

import android.view.View;

public class ViewUtils {
    public static int getSize(int defaultSize, int measureSpec) {
        int mSize = defaultSize;

        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);

        switch (mode) {
            //如果没有指定大小，就设置为默认大小
            case View.MeasureSpec.UNSPECIFIED:
                mSize = defaultSize;
                break;

            // 如果测量模式是最大取值为size
            case View.MeasureSpec.AT_MOST:
            // 如果是固定的大小，就返回原值
            case View.MeasureSpec.EXACTLY:
                mSize = size;
                break;
            default:
                mSize = defaultSize;
        }
        return mSize;
    }
}
