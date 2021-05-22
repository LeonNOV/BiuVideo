package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @Author Leon
 * @Time 2021/3/9
 * @Desc 带有上拉加载功能的LoadingRecyclerView
 */
public class SmartRefreshRecyclerView<T> extends SmartRefreshLayout {
    public static final int NO_DATA = 0;
    public static final int LOADING_FINISHING = 1;

    private final Context context;
    public LoadingRecyclerView loadingRecyclerView;

    public SmartRefreshRecyclerView(@NonNull Context context) {
        super(context);
        this.context = context;

        initView();
    }

    public SmartRefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initView();
    }

    public SmartRefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 关闭顶部刷新控件(下拉刷新控件)
        setEnableRefresh(false);

        // 显示下拉高度/手指真实下拉高度=阻尼效果
//        setDragRate(0.3f);

        // 设置不自动刷新，释放之后再刷新
        setEnableAutoLoadMore(false);
        
        addLoadingRecyclerView();

        // 默认关闭加载功能，在第一次加载数据后开启(需要调用setLoadingRecyclerViewStatus)
        setEnableLoadMore(false);
    }

    /**
     * 添加LoadingRecyclerView
     */
    private void addLoadingRecyclerView() {
        loadingRecyclerView = new LoadingRecyclerView(context);
        addView(loadingRecyclerView);

        LayoutParams layoutParams = (LayoutParams) loadingRecyclerView.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;

        loadingRecyclerView.setLayoutParams(layoutParams);
    }

    public void setProgressBarVisibility(@LoadingRecyclerView.Visibility int visibility) {
        this.loadingRecyclerView.progressBar.setVisibility(visibility);
    }

    public void setImageViewVisibility(@LoadingRecyclerView.Visibility int visibility) {
        this.loadingRecyclerView.imageView.setVisibility(visibility);
    }

    public void setRecyclerViewVisibility(@LoadingRecyclerView.Visibility int visibility) {
        this.loadingRecyclerView.recyclerView.setVisibility(visibility);
    }

    public void setRecyclerViewAdapter(BaseAdapter<T> adapter) {
        this.loadingRecyclerView.recyclerView.setAdapter(adapter);
    }

    public void setRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.loadingRecyclerView.recyclerView.setLayoutManager(layoutManager);
    }

    public void setLoadingRecyclerViewStatus(@LoadingRecyclerView.SRRStatus int status) {
        if (status == LoadingRecyclerView.LOADING_FINISH && !isEnableLoadMore()) {
            setEnableLoadMore(true);
        }
        loadingRecyclerView.setLoadingRecyclerViewStatus(status);
    }

    public void setSmartRefreshStatus(int status) {
        if (NO_DATA == status) {
            finishLoadMore(true);
            setEnableLoadMore(false);
            finishLoadMoreWithNoMoreData();
        } else if (LOADING_FINISHING == status) {
            finishLoadMore(true);
        }
    }
}
