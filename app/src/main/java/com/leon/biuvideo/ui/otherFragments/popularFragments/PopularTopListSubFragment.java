package com.leon.biuvideo.ui.otherFragments.popularFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.popularAdapters.PopularTopListAdapter;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularTopList;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers.PopularTopListParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/25
 * @Desc 全站排行榜子页面
 */
public class PopularTopListSubFragment extends BaseLazySupportFragment {
    private final List<PopularTopList> popularTopLists = new ArrayList<>();
    private LoadingRecyclerView loadingRecyclerView;

    private final String[] params;

    public PopularTopListSubFragment(String[] params) {
        this.params = params;
    }

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
                List<PopularTopList> lists = (List<PopularTopList>) msg.obj;

                if (lists == null || lists.size() == 0) {
                    loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                } else {
                    popularTopListAdapter.append(lists);
                    loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<PopularTopList> popularTopListList = PopularTopListParser.parseData(params[1], params[2]);

                Message message = receiveDataHandler.obtainMessage();
                message.obj = popularTopListList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
