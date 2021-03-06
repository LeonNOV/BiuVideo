package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leon.biuvideo.R;

public class SimpleTopBar extends RelativeLayout implements View.OnClickListener {
    private final Context context;

    private RelativeLayout relativeLayout;
    private ImageView leftView;
    private ImageView rightView;

    private Drawable leftSrc;
    private Drawable rightSrc;
    private String topBarTitle;

    public SimpleTopBar(Context context) {
        super(context);
        this.context = context;
    }

    public SimpleTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.initView(attrs);
    }

    public SimpleTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.initView(attrs);
    }

    private OnSimpleTopBarListener onSimpleTopBarListener;

    public interface OnSimpleTopBarListener {
        void onLeft();
        void onRight();
    }

    public void setOnSimpleTopBarListener(OnSimpleTopBarListener onSimpleTopBarListener) {
        this.onSimpleTopBarListener = onSimpleTopBarListener;
    }

    public void setLeftSrc(Drawable leftSrc) {
        this.leftSrc = leftSrc;
    }

    public void setRightSrc(Drawable rightSrc) {
        this.rightSrc = rightSrc;
    }

    public void setTopBarTitle(String topBarTitle) {
        this.topBarTitle = topBarTitle;
    }

    private void initView(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleTopBar);
        this.topBarTitle = typedArray.getString(R.styleable.SimpleTopBar_topBarTitle);;
        this.leftSrc = typedArray.getDrawable(R.styleable.SimpleTopBar_leftSrc);
        this.rightSrc = typedArray.getDrawable(R.styleable.SimpleTopBar_rightSrc);
        typedArray.recycle();

        if (leftSrc == null) {
            this.leftSrc = getResources().getDrawable(R.drawable.ic_back);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        addParent();
        addLeftView();

        if (topBarTitle != null) {
            addTitle();
        }

        if (rightSrc != null) {
            addRightView();
        }
    }

    /**
     * 添加父布局
     */
    private void addParent() {
        relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(RelativeLayout.CENTER_VERTICAL);
        relativeLayout.setPadding(30, 0, 30, 0);
        relativeLayout.setBackgroundResource(R.color.white);

        addView(relativeLayout);

        LayoutParams relativeLayoutParams = (LayoutParams) relativeLayout.getLayoutParams();
        relativeLayoutParams.width = LayoutParams.MATCH_PARENT;
        relativeLayoutParams.height = getResources().getDimensionPixelOffset(R.dimen.topBar_height);
        relativeLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        relativeLayout.setLayoutParams(relativeLayoutParams);
    }

    /**
     * 添加左侧按键
     */
    private void addLeftView() {
        leftView = new ImageView(context);
        leftView.setId(R.id.simple_topBar_leftView);
        leftView.setImageDrawable(leftSrc);
        leftView.setBackgroundResource(R.drawable.ripple_round_corners6dp_bg);
        leftView.setOnClickListener(this);

        relativeLayout.addView(leftView);

        LayoutParams backViewLayoutParams = (LayoutParams) leftView.getLayoutParams();
        backViewLayoutParams.width = LayoutParams.WRAP_CONTENT;
        backViewLayoutParams.height = LayoutParams.WRAP_CONTENT;
        backViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        backViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        leftView.setLayoutParams(backViewLayoutParams);
    }

    /**
     * 添加标题
     */
    private void addTitle() {
        TextView topBarTitleView = new TextView(context);
        topBarTitleView.setText(topBarTitle);
        topBarTitleView.setTypeface(Typeface.DEFAULT_BOLD);
        topBarTitleView.setTextColor(Color.BLACK);
        topBarTitleView.setTextSize(18);

        relativeLayout.addView(topBarTitleView);

        LayoutParams topBarTitleViewParams = (LayoutParams) topBarTitleView.getLayoutParams();
        topBarTitleViewParams.width = LayoutParams.WRAP_CONTENT;
        topBarTitleViewParams.height = LayoutParams.WRAP_CONTENT;
        topBarTitleViewParams.addRule(RelativeLayout.END_OF, R.id.simple_topBar_leftView);
        topBarTitleViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        topBarTitleViewParams.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.topBar_title_margin_start));

        topBarTitleView.setLayoutParams(topBarTitleViewParams);
    }

    /**
     * 添加右侧按键
     */
    private void addRightView() {
        rightView = new ImageView(context);
        rightView.setId(R.id.simple_topBar_rightView);
        rightView.setImageDrawable(rightSrc);
        rightView.setBackgroundResource(R.drawable.ripple_round_corners6dp_bg);
        rightView.setOnClickListener(this);

        relativeLayout.addView(rightView);

        LayoutParams moreViewLayoutParams = (LayoutParams) rightView.getLayoutParams();
        moreViewLayoutParams.width = LayoutParams.WRAP_CONTENT;
        moreViewLayoutParams.height = LayoutParams.WRAP_CONTENT;
        moreViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        moreViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        rightView.setLayoutParams(moreViewLayoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simple_topBar_leftView:
                if (onSimpleTopBarListener != null) {
                    onSimpleTopBarListener.onLeft();
                }
                break;
            case R.id.simple_topBar_rightView:
                if (onSimpleTopBarListener != null) {
                    onSimpleTopBarListener.onRight();
                }
                break;
            default:
                break;
        }
    }
}
