package com.leon.biuvideo.ui.otherFragments.popularFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.popularAdapters.PopularTopListAdapter;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularTopList;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/25
 * @Desc 全站排行榜子页面
 */
public class PopularTopListSubFragment extends BaseSupportFragment {
    private final List<PopularTopList> popularTopLists = new ArrayList<>();
    private LoadingRecyclerView loadingRecyclerView;

    @Override
    protected int setLayout() {
        return R.layout.popular_top_list_sub_fragment;
    }

    @Override
    protected void initView() {
        loadingRecyclerView = (LoadingRecyclerView) view;

        PopularTopListAdapter popularTopListAdapter = new PopularTopListAdapter(popularTopLists, context);
        popularTopListAdapter.setHasStableIds(true);
        loadingRecyclerView.setRecyclerViewAdapter(popularTopListAdapter);
        loadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {

            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);


    }
}
