package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;

/**
 * 自定义BottomSheet（从底部弹出的Dialog）使用的顶部栏
 */
public class BottomSheetTopBar extends LinearLayout {
    private Context context;
    private AttributeSet attrs;

    private String title = "";
    private Drawable closeSrc = getResources().getDrawable(R.drawable.ic_arrow_down);
    private LinearLayout parentLayout;

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

    private OnCloseListener onCloseListener;

    public interface OnCloseListener {
        /**
         * 关闭该弹窗
         */
        void onClose();
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    private void initView() {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetTopBar);
        this.title = typedArray.getString(R.styleable.BottomSheetTopBar_bottomSheetTitle);
        Drawable drawable = typedArray.getDrawable(R.styleable.BottomSheetTopBar_bottomSheetCloseSrc);
        if (drawable != null) {
            this.closeSrc = drawable;
        }
        typedArray.recycle();

        addParent();
        addTopView();
        addCloseView();
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
        topViewLayoutParams.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.bottomSheetTopViewMarginTop), 0, 0);
        topViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        topView.setLayoutParams(topViewLayoutParams);
    }

    private void addCloseView() {
        ImageView closeView = new ImageView(context);
        closeView.setImageDrawable(closeSrc);
        closeView.setBackgroundResource(R.drawable.ripple_round_corners6dp_bg);
        closeView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bg)));
        closeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCloseListener != null) {
                    onCloseListener.onClose();
                }
            }
        });

        parentLayout.addView(closeView);

        LayoutParams closeViewLayoutParams = (LayoutParams) closeView.getLayoutParams();
        closeViewLayoutParams.width = getResources().getDimensionPixelOffset(R.dimen.bottomSheetCloseViewWH);
        closeViewLayoutParams.height = getResources().getDimensionPixelOffset(R.dimen.bottomSheetCloseViewWH);
        closeViewLayoutParams.gravity = Gravity.END;

        closeView.setLayoutParams(closeViewLayoutParams);
    }

    private void addTitle() {
        TextView titleView = new TextView(context);
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


}
