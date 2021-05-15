package com.leon.biuvideo.ui.mainFragments;

import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discoverAdapters.DiscoverHotSearchAdapter;
import com.leon.biuvideo.beans.discoverBeans.HotSearch;
import com.leon.biuvideo.greendao.dao.SearchHistory;
import com.leon.biuvideo.greendao.daoutils.SearchHistoryUtils;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.discovery.SearchFragment;
import com.leon.biuvideo.ui.discovery.SearchResultFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.HotSearchParser;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 发现页面
 */
public class DiscoveryFragment extends BaseSupportFragment implements View.OnClickListener {
    private LoadingRecyclerView discoveryLoadingRecyclerView;

    @Override
    protected int setLayout() {
        return R.layout.discovery_fragment;
    }

    @Override
    protected void initView() {
        LinearLayout discoverySearchLinearLayout = view.findViewById(R.id.discovery_search);
        discoverySearchLinearLayout.setOnClickListener(this);

        discoveryLoadingRecyclerView = view.findViewById(R.id.discovery_loadingRecyclerView);
        discoveryLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<HotSearch> hotSearchList = (List<HotSearch>) msg.obj;
                discoveryLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

                DiscoverHotSearchAdapter discoverHotSearchAdapter = new DiscoverHotSearchAdapter(hotSearchList, context);
                discoverHotSearchAdapter.setOnClickHotWordListener(new DiscoverHotSearchAdapter.OnClickHotWordListener() {
                    @Override
                    public void onClick(String keyword) {
                        if (InternetUtils.checkNetwork(_mActivity.getWindow().getDecorView())) {
                            // 存放当前的关键词
                            new SearchHistoryUtils(context).getSearchHistoryDaoUtils()
                                    .insert(new SearchHistory(null, (long) keyword.hashCode(), keyword));

                            // 对该关键词进行搜索
                            ((NavFragment) getParentFragment()).startBrotherFragment(new SearchResultFragment(keyword));
                        }
                    }
                });
                discoveryLoadingRecyclerView.setRecyclerViewAdapter(discoverHotSearchAdapter);

                discoveryLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
            }
        });
    }

    @Override
    public void lazyInit() {
        super.lazyInit();

        if (InternetUtils.checkNetwork(_mActivity.getWindow().getDecorView())) {
            SimpleSingleThreadPool.executor(new Runnable() {
                @Override
                public void run() {
                    HotSearchParser hotSearchParser = new HotSearchParser();
                    List<HotSearch> hotSearchList = hotSearchParser.parseData();

                    Message message = receiveDataHandler.obtainMessage();
                    message.obj = hotSearchList;
                    receiveDataHandler.sendMessage(message);
                }
            });
        } else {
            discoveryLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
        }
    }

    @Override
    public void onClick(View v) {
        if (InternetUtils.checkNetwork(v)) {
            ((NavFragment) getParentFragment()).startBrotherFragment(SearchFragment.newInstance());
        }
    }
}
