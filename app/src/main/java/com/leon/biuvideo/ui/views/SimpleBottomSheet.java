package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc 一个简单的底部对话框
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

        // 设置底部透明
        FrameLayout bottom = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottom != null) {
            bottom.setBackgroundResource(android.R.color.transparent);
        }
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        return bottomSheetView;
    }
}
