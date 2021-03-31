package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discover.searchResultAdapters.searchResultBnagumiAdapters.SearchResultBangumiAdapter;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBangumi;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
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
    private String keyword;

    private final List<SearchResultBangumi> searchResultBangumiList = new ArrayList<>();
    private SmartRefreshRecyclerView<SearchResultBangumi> searchResultBangumiData;
    private SearchResultBangumiParser searchResultBangumiParser;
    private SearchResultBangumiAdapter searchResultBangumiAdapter;

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
                getBangumi(1);
            }
        });
        searchResultBangumiAdapter = new SearchResultBangumiAdapter(searchResultBangumiList, context);
        searchResultBangumiAdapter.setHasStableIds(true);
        searchResultBangumiData.setRecyclerViewAdapter(searchResultBangumiAdapter);
        searchResultBangumiData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<SearchResultBangumi> searchResultBangumis = (List<SearchResultBangumi>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (searchResultBangumis == null || searchResultBangumis.size() == 0) {
                            searchResultBangumiData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            searchResultBangumiData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            searchResultBangumiAdapter.append(searchResultBangumis);
                            searchResultBangumiData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        }
                        break;
                    case 1:
                        if (searchResultBangumis != null && searchResultBangumis.size() > 0) {
                            searchResultBangumiAdapter.append(searchResultBangumis);
                            searchResultBangumiData.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            searchResultBangumiData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        searchResultBangumiData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        getBangumi(0);
    }

    private void getBangumi(int what) {
        if (searchResultBangumiParser == null) {
            searchResultBangumiParser = new SearchResultBangumiParser(keyword);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<SearchResultBangumi> searchResultBangumiList = searchResultBangumiParser.parseData();

                Message message;
                if (what == -1) {
                    message = receiveDataHandler.obtainMessage(0);
                } else {
                    message = receiveDataHandler.obtainMessage(what);
                }
                message.obj = searchResultBangumiList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     *  重置当前所有的数据
     */
    private void reset() {
        searchResultBangumiData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        // 清空列表中的数据
        searchResultBangumiAdapter.removeAll();

        searchResultBangumiParser = new SearchResultBangumiParser(keyword);
        getBangumi(-1);
    }

    public void reSearch (String keyword) {
        this.keyword = keyword;

        reset();
    }
}
