package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/9
 * @Desc
 */
public class SmartRefreshRecyclerView<T> extends SmartRefreshLayout {
    /**
     * 无数据状态
     */
    public static final int NO_DATA = 0;

    /**
     * 加载数据中
     */
    public static final int LOADING = 1;

    /**
     * 数据加载完毕
     */
    public static final int LOADING_FINISH = 2;

    private final Context context;
    private ClassicsFooter classicsFooter;
    private LoadingRecyclerView loadingRecyclerView;
    private BaseAdapter<T> adapter;

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
        addClassicsFooter();
    }

    /**
     * 添加底部刷新控件
     */
    private void addClassicsFooter() {
        classicsFooter = new ClassicsFooter(context);
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放加载更多";
        addView(classicsFooter);

        SmartRefreshLayout.LayoutParams layoutParams = (SmartRefreshLayout.LayoutParams) classicsFooter.getLayoutParams();
        layoutParams.width = SmartRefreshLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = SmartRefreshLayout.LayoutParams.WRAP_CONTENT;
        classicsFooter.setLayoutParams(layoutParams);
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

    @IntDef({NO_DATA, LOADING, LOADING_FINISH})
    @interface SRRStatus {}

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
        this.adapter = adapter;
        this.loadingRecyclerView.recyclerView.setAdapter(adapter);
    }

    public void setRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.loadingRecyclerView.recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 改变状态
     *
     * @param status    NoData:{@value NO_DATA}, Loading:{@value LOADING}, LoadingFinish:{@value LOADING_FINISH}
     */
    public void setStatus(@SRRStatus int status) {
        switch (status) {
            case NO_DATA:
                setRecyclerViewVisibility(GONE);
                setProgressBarVisibility(GONE);
                setImageViewVisibility(VISIBLE);
                break;
            case LOADING:
                setRecyclerViewVisibility(GONE);
                setProgressBarVisibility(VISIBLE);
                setImageViewVisibility(GONE);
                break;
            case LOADING_FINISH:
                setRecyclerViewVisibility(VISIBLE);
                setProgressBarVisibility(GONE);
                setImageViewVisibility(GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 追加数据
     *
     * @param appends  追加的数据
     */
    public void append(List<T> appends) {
        if (adapter != null) {
            adapter.append(appends);
        }
    }
}
