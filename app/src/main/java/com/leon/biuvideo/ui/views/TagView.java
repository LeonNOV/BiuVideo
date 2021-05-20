package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc 标签View，可动态设置标签名称和值
 */
public class TagView extends LinearLayout {
    private String rightValue = null;
    private String leftValue = null;

    // 1:left,2:right
    private int tagMark;

    private Context context;
    private AttributeSet attrs;

    private TextView rightView;
    private TextView leftView;

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
        this.tagMark = typedArray.getInt(R.styleable.TagView_tagMark, 1);

        typedArray.recycle();

        setBackgroundResource(R.drawable.ripple_round_corners6dp_bg);

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

        addView(leftView);

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

        addView(rightView);
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

    public TagView clearBg () {
        setBackground(null);

        return this;
    }
}