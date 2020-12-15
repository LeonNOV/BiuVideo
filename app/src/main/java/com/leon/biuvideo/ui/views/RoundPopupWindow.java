package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

public class RoundPopupWindow extends PopupWindow {
    public final static int SHOW_AS_DROP_DOWN = 1;
    public final static int SHOW_AS_LEFT = 2;

    private final Context context;
    private final View anchor;

    private View popupView;

    private int offsetX = 0;
    private int offsetY = 0;
    private int gravity = Gravity.END;

    public RoundPopupWindow(Context context, View anchor) {
        super(context);
        this.context = context;
        this.anchor = anchor;
    }

    /**
     * 设置popupWindow显示视图
     * <br/>
     * <strong>该方法必须在创建完RoundPopupWindow后进行调用，否则将出现`NullPointerException`</strong>
     *
     * @param layoutId  layout ID
     * @return  返回this
     */
    public RoundPopupWindow setContentView(int layoutId) {
        popupView = LayoutInflater.from(context).inflate(layoutId, null);

        return this;
    }

    /**
     * 设置控件的监听事件
     * <br/>
     * <strong>只需传入对应的控件的ID，覆盖View.OnClickListener即可<strong/>
     *
     * @param viewId    监听的控件ID
     * @param onClickListener   需要覆盖View.OnClickListener
     * @return  返回this
     */
    public RoundPopupWindow setOnClickListener (int viewId, View.OnClickListener onClickListener) {
        popupView.findViewById(viewId).setOnClickListener(onClickListener);

        return this;
    }

    public RoundPopupWindow setText(int viewId, String text) {
        TextView textView = popupView.findViewById(viewId);
        textView.setText(text);

        return this;
    }

    /**
     * 设置popupWindow显示的位置
     * 不调用该方法的话则按默认位置显示
     *
     * @param location  该类中的常量
     * @return  返回this
     */
    public RoundPopupWindow setLocation(int location) {
        //测量popupWindow的宽高
        popupView.measure(makeDropDownMeasureSpec(this.getWidth()), makeDropDownMeasureSpec(this.getHeight()));

        if (location == SHOW_AS_DROP_DOWN) {
            this.offsetX = -popupView.getMeasuredWidth();
            this.offsetY = anchor.getHeight() / 3;
            this.gravity = Gravity.END;
        } else {
            this.offsetX = -popupView.getMeasuredWidth();
            this.offsetY = -anchor.getHeight();
            this.gravity = Gravity.START;
        }

        return this;
    }

    /**
     * 创建RoundPopupWindow对象
     * <br/>
     * <strong>该方法必须放到在最后进行调用</strong>
     */
    public void create() {
        this.setContentView(popupView);
        this.setHeight(ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        this.showAsDropDown(anchor, offsetX, offsetY, gravity);
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
