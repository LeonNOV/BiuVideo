package com.leon.biuvideo.ui.home;

import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.GridLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.RecommendAdapter;
import com.leon.biuvideo.beans.resourcesBeans.VideoRecommend;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.PreferenceUtils;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 推荐页面
 */
public class RecommendFragment extends BaseSupportFragment {
    private LoadingRecyclerView recommendLoadingRecyclerView;
    private final List<VideoRecommend> videoRecommendList;

    public RecommendFragment(List<VideoRecommend> videoRecommendList) {
        this.videoRecommendList = videoRecommendList;
    }

    @Override
    protected int setLayout() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.recommend_topBar);

        recommendLoadingRecyclerView = findView(R.id.recommend_loadingRecyclerView);

        initValues();
    }

    private void initValues() {
        // 设置数据
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                recommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

                int recommendColumns = PreferenceUtils.getRecommendColumns();

                recommendLoadingRecyclerView.setRecyclerViewLayoutManager(new GridLayoutManager(context, recommendColumns));
                recommendLoadingRecyclerView.setRecyclerViewAdapter(new RecommendAdapter(videoRecommendList, recommendColumns == 1 ? RecommendAdapter.SINGLE_COLUMN : RecommendAdapter.DOUBLE_COLUMN, context));

                // 设置状态为已完成加载数据
                recommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
            }
        });
    }
}
