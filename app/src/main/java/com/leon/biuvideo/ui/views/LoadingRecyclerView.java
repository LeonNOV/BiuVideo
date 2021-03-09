package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author Leon
 * @Time 2021/3/9
 * @Desc
 */
public class LoadingRecyclerView extends FrameLayout {
    private final Context context;
    private FrameLayout parentLayout;
    private ProgressBar progressBar;
    private ImageView imageView;
    private RecyclerView recyclerView;

    public LoadingRecyclerView(@NonNull Context context) {
        super(context);
        this.context = context;

        initView();
    }

    public LoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initView();
    }

    public LoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initView();

    }

    private void initView() {
        addParent();
        addProgressBar();
        addRecyclerView();
        addNoDataWarn();
    }

    private void addParent() {
        parentLayout = new FrameLayout(context);
        addView(parentLayout);

        LayoutParams layoutParams = (LayoutParams) parentLayout.getLayoutParams();
        layoutParams.height = LayoutParams.MATCH_PARENT;
        layoutParams.width = LayoutParams.MATCH_PARENT;
        parentLayout.setLayoutParams(layoutParams);
    }

    private void addProgressBar() {
        progressBar = new ProgressBar(context);
        progressBar.setIndeterminateTintList(ColorStateList.valueOf(ResourcesCompat.getColor(getResources(), R.color.blue, null)));
        progressBar.setVisibility(View.GONE);

        parentLayout.addView(progressBar);

        LayoutParams layoutParams = (LayoutParams) progressBar.getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.loadingRecyclerViewProgressBarHW);
        layoutParams.width = getResources().getDimensionPixelOffset(R.dimen.loadingRecyclerViewProgressBarHW);
        layoutParams.gravity = Gravity.CENTER;

        progressBar.setLayoutParams(layoutParams);
    }

    private void addRecyclerView() {
        recyclerView = new RecyclerView(context);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        parentLayout.addView(recyclerView);

        LayoutParams layoutParams = (LayoutParams) recyclerView.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        recyclerView.setLayoutParams(layoutParams);
    }

    private void addNoDataWarn() {
        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_no_data);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        parentLayout.addView(imageView);

        LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
    }

    public void setProgressBarVisibility(@Visibility int visibility) {
        this.progressBar.setVisibility(visibility);
    }

    public void setImageViewVisibility(@Visibility int visibility) {
        this.imageView.setVisibility(visibility);
    }

    public void setRecyclerViewVisibility(@Visibility int visibility) {
        this.recyclerView.setVisibility(visibility);
    }

    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        this.recyclerView.setAdapter(adapter);
    }

    public void setRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.recyclerView.setLayoutManager(layoutManager);
    }

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Visibility {}
}
