package com.leon.biuvideo.ui.otherFragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.popularAdapters.PopularAdapter;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularPreciousFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularTopListFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularWeeklyFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers.PopularHotListParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 综合热门页面
 */
public class PopularFragment extends BaseSupportFragment {
    private final List<PopularVideo> popularVideoList = new ArrayList<>();
    private SmartRefreshRecyclerView<PopularVideo> popularHotList;
    private DataLoader<PopularVideo> popularVideoDataLoader;

    public static PopularFragment getInstance() {
        return new PopularFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.popular_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar popularTopBar = view.findViewById(R.id.popular_topBar);
        popularTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        PopularAdapter popularAdapter = new PopularAdapter(popularVideoList, context, PopularAdapter.HOT_VIDEO);
        popularAdapter.setOnClickFirstItemListener(new PopularAdapter.OnClickFirstItemListener() {
            @Override
            public void onClickTopList() {
                start(new PopularTopListFragment());
            }

            @Override
            public void onClickWeekly() {
                start(new PopularWeeklyFragment());
            }

            @Override
            public void onClickPrecious() {
                start(new PopularPreciousFragment());
            }
        });
        popularAdapter.setHasStableIds(true);
        popularHotList = findView(R.id.popular_hot_list);
        popularHotList.setRecyclerViewAdapter(popularAdapter);
        popularHotList.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        popularHotList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                popularVideoDataLoader.insertData(false);
            }
        });

        popularVideoDataLoader = new DataLoader<>(new PopularHotListParser(), popularHotList, popularAdapter, this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        popularHotList.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        popularVideoDataLoader.insertData(true);
    }
}