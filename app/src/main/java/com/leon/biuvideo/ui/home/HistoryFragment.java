package com.leon.biuvideo.ui.home;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.HistoryAdapter;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.HistoryParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/5
 * @Desc 历史记录页面
 */
public class HistoryFragment extends BaseSupportFragment {
    private final List<History> historyList = new ArrayList<>();
    private DataLoader<History> historyDataLoader;

    public static HistoryFragment getInstance() {
        return new HistoryFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.history_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar historyTopBar = findView(R.id.history_topBar);
        historyTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {
            }
        });

        SmartRefreshRecyclerView<History> historyData = findView(R.id.history_data);
        historyData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                historyDataLoader.insertData(false);
            }
        });
        HistoryAdapter historyAdapter = new HistoryAdapter(historyList, context);
        historyAdapter.setHasStableIds(true);
        historyData.setRecyclerViewAdapter(historyAdapter);
        historyData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        historyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        historyDataLoader = new DataLoader<>(new HistoryParser(), historyData, historyAdapter, this);
        historyDataLoader.insertData(true);
    }
}
