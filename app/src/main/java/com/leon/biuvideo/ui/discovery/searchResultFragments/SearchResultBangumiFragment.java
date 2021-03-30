package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.os.Message;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 番剧搜索结果
 */
public class SearchResultBangumiFragment extends BaseLazySupportFragment {
    @Override
    protected int setLayout() {
        return R.layout.search_ressult_bangumi_fragment;
    }

    @Override
    protected void initView() {
        SmartRefreshRecyclerView searchResultBangumiData = findView(R.id.search_result_bangumi_data);
        searchResultBangumiData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {

            }
        });
    }

    @Override
    protected void onLazyLoad() {

    }
}
