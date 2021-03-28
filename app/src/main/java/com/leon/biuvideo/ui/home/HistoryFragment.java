package com.leon.biuvideo.ui.home;

import android.os.Message;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.HistoryAdapter;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.HistoryParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author Leon
 * @Time 2021/3/5
 * @Desc 历史记录页面
 */
public class HistoryFragment extends BaseSupportFragment {
    private HistoryParser historyParser;
    private final List<History> historyList = new ArrayList<>();

    public static HistoryFragment getInstance() {
        return new HistoryFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.history_fragment;
    }

    @Override
    protected void initView() {
        ViewUtils.setStatusBar(getActivity(), true);
        SimpleTopBar historyTopBar = findView(R.id.history_topBar);
        historyTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
                ViewUtils.setStatusBar(getActivity(), false);
            }

            @Override
            public void onRight() {
            }
        });

        SmartRefreshRecyclerView<History> historyData = findView(R.id.history_data);
        historyData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!historyParser.dataStatus) {
                    historyData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                } else {
                    getHistory(1);
                }
            }
        });
        HistoryAdapter historyAdapter = new HistoryAdapter(historyList, context);
        historyAdapter.setHasStableIds(true);
        historyData.setRecyclerViewAdapter(historyAdapter);
        historyData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        historyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        getHistory(0);
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<History> histories = (List<History>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (histories == null || histories.size() == 0) {
                            historyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            historyData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            historyAdapter.append(histories);
                            historyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        }
                        break;
                    case 1:
                        if (histories != null || histories.size() > 0) {
                            historyAdapter.append(histories);
                            historyData.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            historyData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getHistory (int what) {
        if (historyParser == null) {
            historyParser = new HistoryParser();
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<History> historyList = historyParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = historyList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
