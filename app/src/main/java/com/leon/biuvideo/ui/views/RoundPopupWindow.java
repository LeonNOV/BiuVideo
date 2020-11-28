package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.core.widget.PopupWindowCompat;

public abstract class RoundPopupWindow extends PopupWindow {
    public final static int SHOW_AS_DROP_DOWN = 1;
    public final static int SHOW_AS_DROP_LEFT = 2;

    private final Context context;

    private View popupView;
    private PopupWindow popupWindow;

    private View anchor;
    private int offsetX = 0;
    private int offsetY = 0;

    public RoundPopupWindow(Context context) {
        super(context);
        this.context = context;

        initView();
    }

    public abstract int setLayout();

    private void initView() {
        popupView = LayoutInflater.from(context).inflate(setLayout(), null);

        popupWindow = new PopupWindow(popupView, ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    public RoundPopupWindow setOnClickListener (int viewId, View.OnClickListener onClickListener) {
        popupView.findViewById(viewId).setOnClickListener(onClickListener);

        return this;
    }

    public RoundPopupWindow setLocation(View anchor, int location) {
        this.anchor = anchor;
        setPopupWindowLocation(location);

        return this;
    }

    private void setPopupWindowLocation (int location) {
        if (anchor != null) {
            View contentView = popupWindow.getContentView();

            //测量popupWindow的宽高
            contentView.measure(makeDropDownMeasureSpec(popupWindow.getWidth()), makeDropDownMeasureSpec(popupWindow.getHeight()));

            if (location == SHOW_AS_DROP_LEFT) {
                this.offsetX = -popupWindow.getContentView().getMeasuredWidth();
                this.offsetY = -(popupWindow.getContentView().getMeasuredHeight() + popupWindow.getHeight());
            } else {
                this.offsetX = -popupWindow.getContentView().getMeasuredWidth();
                this.offsetY = 0;
            }

            PopupWindowCompat.showAsDropDown(popupWindow, anchor, this.offsetX, this.offsetY, Gravity.START);
//            PopupWindowCompat.showAsDropDown(popupWindow, anchor, this.offsetX, this.offsetY, Gravity.END);
        }
    }

    /**
     * 获取popupWindow的实际大小
     *
     * @param measureSpec   测量规格
     * @return  返回popupWindow的大小
     */
    private int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }
}
