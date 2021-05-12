package com.leon.biuvideo.ui.discovery.searchResultFragments;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters.searchResultBnagumiAdapters.SearchResultBangumiAdapter;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBangumi;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.SearchResultBangumiParser;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 番剧搜索结果
 */
public class SearchResultBangumiFragment extends BaseLazySupportFragment {
    private final String keyword;

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
        searchResultBangumiDataLoader = new DataLoader<>(new SearchResultBangumiParser(keyword),
                R.id.search_result_bangumi_data,
                new SearchResultBangumiAdapter(context),
                this);
    }

    @Override
    protected void onLazyLoad() {
        searchResultBangumiDataLoader.insertData(true);
    }
}
