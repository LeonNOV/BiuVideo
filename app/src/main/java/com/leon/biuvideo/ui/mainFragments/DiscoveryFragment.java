package com.leon.biuvideo.ui.mainFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discover.DiscoverHotSearchAdapter;
import com.leon.biuvideo.beans.discoverBeans.HotSearch;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.discovery.SearchFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.discoverParseUtils.HotSearchParser;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 发现页面
 */
public class DiscoveryFragment extends BaseSupportFragment implements View.OnClickListener {
    private LoadingRecyclerView discoveryLoadingRecyclerView;
    private Handler handler;

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

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                List<HotSearch> hotSearchList= (List<HotSearch>) msg.obj;
                discoveryLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
                discoveryLoadingRecyclerView.setRecyclerViewAdapter(new DiscoverHotSearchAdapter(hotSearchList, context));

                discoveryLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);

                return true;
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                HotSearchParser hotSearchParser = new HotSearchParser();
                List<HotSearch> hotSearchList = hotSearchParser.parseData();

                Message message = handler.obtainMessage();
                message.obj = hotSearchList;

                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.discovery_search) {
            ((NavFragment) getParentFragment()).startBrotherFragment(SearchFragment.newInstance());
        }
    }
}
