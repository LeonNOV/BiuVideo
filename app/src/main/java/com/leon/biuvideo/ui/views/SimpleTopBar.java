package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.leon.biuvideo.R;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 一个带有返回键、标题和更多按钮的自定义TopBar
 */
public class SimpleTopBar extends FrameLayout implements View.OnClickListener {
    private final Context context;
    private AttributeSet attrs;

    private String topBarTitle = "";
    private Typeface topBarTitleStyle = Typeface.DEFAULT_BOLD;
    private TextView topBarTitleView;
    private LinearLayout linearLayout;

    public SimpleTopBar(Context context) {
        super(context);
        this.context = context;

        initView();
    }

    public SimpleTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;

        initView();
    }

    public SimpleTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;

        initView();
    }

    private OnBackListener onBackListener;

    public interface OnBackListener {
        /**
         * 左边控件点击事件
         */
        void onBack();
    }

    public void setBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView() {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleTopBar);
        this.topBarTitle = typedArray.getString(R.styleable.SimpleTopBar_topBarTitle);;
        this.topBarTitleStyle = typedArray.getInt(R.styleable.SimpleTopBar_topBarTitleStyle, 1) == 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
        typedArray.recycle();

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundResource(R.color.white);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        addView(linearLayout);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.topBar_height);
        linearLayout.setLayoutParams(layoutParams);

        addBackView();
        addTitle();
    }

    /**
     * 添加左侧按键
     */
    private void addBackView() {
        ImageView backView = new ImageView(context);
        backView.setId(R.id.simple_topBar_backView);
        backView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_back, null));
        backView.setBackgroundResource(R.drawable.ripple_round_corners6dp_bg);
        backView.setOnClickListener(this);

        linearLayout.addView(backView);
        LinearLayout.LayoutParams backViewLayoutParams = (LinearLayout.LayoutParams) backView.getLayoutParams();
        backViewLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        backViewLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        backViewLayoutParams.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.topBar_backView_margin_start));
        backView.setLayoutParams(backViewLayoutParams);
    }

    /**
     * 添加标题
     */
    private void addTitle() {
        topBarTitleView = new TextView(context);
        topBarTitleView.setText(topBarTitle);
        topBarTitleView.setTypeface(Typeface.DEFAULT_BOLD);
        topBarTitleView.setTextColor(Color.BLACK);
        topBarTitleView.setTextSize(18);
        topBarTitleView.setMaxLines(1);
        topBarTitleView.setEllipsize(TextUtils.TruncateAt.END);
        topBarTitleView.setTypeface(topBarTitleStyle);

        linearLayout.addView(topBarTitleView);

        LinearLayout.LayoutParams topBarTitleViewParams = (LinearLayout.LayoutParams) topBarTitleView.getLayoutParams();
        topBarTitleViewParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        topBarTitleViewParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        topBarTitleViewParams.setMarginStart(getResources().getDimensionPixelOffset(R.dimen.topBar_title_margin_start));
        topBarTitleViewParams.setMarginEnd(getResources().getDimensionPixelOffset(R.dimen.topBar_title_margin_start));
        topBarTitleView.setLayoutParams(topBarTitleViewParams);
    }

    /**
     * 动态标题
     *
     * @param title 标题
     */
    public SimpleTopBar setTopBarTitle(String title) {
        this.topBarTitleView.setText(title);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (onBackListener != null) {
            onBackListener.onBack();
        }
    }
}
