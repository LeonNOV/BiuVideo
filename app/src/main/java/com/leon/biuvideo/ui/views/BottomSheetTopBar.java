package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 自定义BottomSheet（从底部弹出的Dialog）使用的顶部栏
 */
public class BottomSheetTopBar extends LinearLayout {
    private Context context;
    private AttributeSet attrs;

    private String title = "";
    private LinearLayout parentLayout;
    private TextView titleView;

    public BottomSheetTopBar(Context context) {
        super(context);
        this.context = context;

        initView();
    }

    public BottomSheetTopBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;

        initView();
    }

    public BottomSheetTopBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;

        initView();
    }

    private void initView() {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetTopBar);
        this.title = typedArray.getString(R.styleable.BottomSheetTopBar_bottomSheetTitle);
        typedArray.recycle();

        addParent();
        addTopView();
        addTitle();
    }

    private void addParent() {
        parentLayout = new LinearLayout(context);
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        addView(parentLayout);

        LayoutParams parentLayoutParams = (LayoutParams) parentLayout.getLayoutParams();
        parentLayoutParams.width = LayoutParams.MATCH_PARENT;
        parentLayoutParams.height = LayoutParams.WRAP_CONTENT;
        parentLayout.setLayoutParams(parentLayoutParams);
    }

    private void addTopView() {
        View topView = new View(context);
        topView.setBackgroundResource(R.drawable.round_corners6dp_bg);
        topView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bottomSheet_topView_bg)));

        parentLayout.addView(topView);

        LayoutParams topViewLayoutParams = (LayoutParams) topView.getLayoutParams();
        topViewLayoutParams.width = getResources().getDimensionPixelOffset(R.dimen.bottomSheet_topView_width);
        topViewLayoutParams.height = getResources().getDimensionPixelOffset(R.dimen.bottomSheet_topView_height);
        topViewLayoutParams.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.bottomSheetTopViewMarginTop), 0, getResources().getDimensionPixelOffset(R.dimen.cardMargin));
        topViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        topView.setLayoutParams(topViewLayoutParams);
    }

    private void addTitle() {
        titleView = new TextView(context);
        titleView.setText(title);
        titleView.setTextSize(17);
        titleView.setTextColor(getResources().getColor(R.color.black));
        titleView.setTypeface(Typeface.DEFAULT_BOLD);

        parentLayout.addView(titleView);

        LayoutParams titleLayoutParams = (LayoutParams) titleView.getLayoutParams();
        titleLayoutParams.width = LayoutParams.WRAP_CONTENT;
        titleLayoutParams.height = LayoutParams.WRAP_CONTENT;
        titleLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        titleView.setLayoutParams(titleLayoutParams);
    }

    public void setTitle (String title) {
        titleView.setText(title);
    }
}
