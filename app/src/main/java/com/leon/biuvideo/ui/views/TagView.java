package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;

public class TagView extends LinearLayout {
    private String rightValue = null;
    private String leftValue = null;
    private boolean isClick;

    // 1:left,2:right
    private int tagMark;

    private Context context;
    private AttributeSet attrs;

    private TextView rightView;
    private TextView leftView;
    private LinearLayout linearLayout;

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.attrs = attrs;

        initView();
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        this.attrs = attrs;

        initView();
    }

    private OnTagViewClickListener onTagViewClickListener;

    public interface OnTagViewClickListener {
        void onClick();
    }

    public void setOnTagViewClickListener(OnTagViewClickListener onTagViewClickListener) {
        this.onTagViewClickListener = onTagViewClickListener;
    }

    public String getLeftValue() {
        if (leftView != null) {
            return leftView.getText().toString();
        }

        return "";
    }

    public void setLeftValue(String leftValue) {
        if (leftView != null) {
            this.leftView.setText(leftValue);
        }
    }

    public String getRightValue() {
        if (leftView != null) {
            return leftView.getText().toString();
        }

        return "";
    }

    public void setRightValue(String rightValue) {
        if (rightView != null) {
            this.rightView.setText(rightValue);
        }
    }

    private void initView() {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        this.leftValue = typedArray.getString(R.styleable.TagView_leftValue);
        this.rightValue = typedArray.getString(R.styleable.TagView_rightValue);
        this.isClick = typedArray.getBoolean(R.styleable.TagView_isClick, false);
        this.tagMark = typedArray.getInt(R.styleable.TagView_tagMark, 1);

        typedArray.recycle();

        addParent();
        addLeftView();
        addRightView();

        if (tagMark == 1) {
            rightView.setMaxLines(1);
            rightView.setEllipsize(TextUtils.TruncateAt.END);
        } else if (tagMark == 2) {
            leftView.setMaxLines(1);
            leftView.setEllipsize(TextUtils.TruncateAt.START);
        }

        setTagMark();
        setClick();
    }

    /**
     * 添加一个LinearLayout
     */
    private void addParent() {
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        addView(linearLayout);

        LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;

        linearLayout.setLayoutParams(layoutParams);
    }

    /**
     * 添加数值（左边）view
     */
    private void addLeftView() {
        leftView = new TextView(context);
        leftView.setId(R.id.tag_view_value);
        leftView.setTextSize(15);
        leftView.setText(leftValue);
        leftView.setTextColor(getResources().getColor(R.color.gray));

        linearLayout.addView(leftView);

        LayoutParams valueViewLayoutParams = (LayoutParams) leftView.getLayoutParams();
        valueViewLayoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.tagView_valueMarginEnd));
        leftView.setLayoutParams(valueViewLayoutParams);
    }

    /**
     * 添加标签（右边）view
     */
    private void addRightView() {
        rightView = new TextView(context);
        rightView.setId(R.id.tag_view_tagName);
        rightView.setTextSize(15);
        rightView.setText(rightValue);
        rightView.setTextColor(getResources().getColor(R.color.gray));

        linearLayout.addView(rightView);
    }

    private void setClick() {
        if (isClick) {
            linearLayout.setBackgroundResource(R.drawable.ripple_round_corners6dp_bg);
            linearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagViewClickListener != null) {
                        onTagViewClickListener.onClick();
                    }
                }
            });
        }
    }

    private void setTagMark() {
        if (tagMark == 1) {
            leftView.setTypeface(Typeface.DEFAULT_BOLD);
            leftView.setTextColor(Color.BLACK);
        } else if (tagMark == 2) {
            rightView.setTypeface(Typeface.DEFAULT_BOLD);
            rightView.setTextColor(Color.BLACK);
        }
    }
}