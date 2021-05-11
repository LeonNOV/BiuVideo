package com.leon.biuvideo.ui.discovery.searchResultFragments;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters.searchResultBnagumiAdapters.SearchResultBangumiAdapter;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBangumi;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.SearchResultBangumiParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 番剧搜索结果
 */
public class SearchResultBangumiFragment extends BaseLazySupportFragment {
    private final String keyword;

    private final List<SearchResultBangumi> searchResultBangumiList = new ArrayList<>();
    private SmartRefreshRecyclerView<SearchResultBangumi> searchResultBangumiData;
    private DataLoader<SearchResultBangumi> searchResultBangumiDataLoader;

    public SearchResultBangumiFragment(String keyword) {
        this.keyword = keyword;
    }

    @Override
    protected int setLayout() {
        return R.layout.search_ressult_bangumi_fragment;
    }

    @Override
    protected void initView() {
        searchResultBangumiData = findView(R.id.search_result_bangumi_data);
        searchResultBangumiData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                searchResultBangumiDataLoader.insertData(false);
            }
        });
        SearchResultBangumiAdapter searchResultBangumiAdapter = new SearchResultBangumiAdapter(searchResultBangumiList, context);
        searchResultBangumiAdapter.setHasStableIds(true);
        searchResultBangumiData.setRecyclerViewAdapter(searchResultBangumiAdapter);
        searchResultBangumiData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        searchResultBangumiDataLoader = new DataLoader<>(new SearchResultBangumiParser(keyword), searchResultBangumiData, searchResultBangumiAdapter, this);
    }

    @Override
    protected void onLazyLoad() {
        searchResultBangumiData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        searchResultBangumiDataLoader.insertData(true);
    }
}
