package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc
 */
public class SimpleBottomSheet {
    private final Context context;
    private final View bottomSheetView;
    public BottomSheetDialog bottomSheetDialog;

    public SimpleBottomSheet(Context context, @LayoutRes int layoutId) {
        this.context = context;
        bottomSheetView = View.inflate(context, layoutId, null);
    }

    public View initView() {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);

        return bottomSheetView;
    }
}
