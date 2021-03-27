package com.leon.biuvideo.ui.home;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.RecommendAdapter;
import com.leon.biuvideo.beans.homeBeans.Recommend;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 推荐页面
 */
public class RecommendFragment extends BaseSupportFragment {
    private LoadingRecyclerView recommendLoadingRecyclerView;
    private final List<Recommend> recommendList;

    public RecommendFragment(List<Recommend> recommendList) {
        this.recommendList = recommendList;
    }

    @Override
    protected int setLayout() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initView() {
        ViewUtils.setStatusBar(getActivity(), true);

        SimpleTopBar recommendTopBar = view.findViewById(R.id.recommend_topBar);
        recommendTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
                ViewUtils.setStatusBar(getActivity(), false);
            }

            @Override
            public void onRight() {
                Toast.makeText(context, "点击了more", Toast.LENGTH_SHORT).show();
            }
        });

        recommendLoadingRecyclerView = view.findViewById(R.id.recommend_loadingRecyclerView);

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
                recommendLoadingRecyclerView.setRecyclerViewAdapter(new RecommendAdapter(recommendList, recommendColumns == 1 ? RecommendAdapter.SINGLE_COLUMN : RecommendAdapter.DOUBLE_COLUMN, context));

                // 设置状态为已完成加载数据
                recommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
            }
        });
    }
}
