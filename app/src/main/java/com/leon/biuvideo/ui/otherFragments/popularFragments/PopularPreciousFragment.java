package com.leon.biuvideo.ui.otherFragments.popularFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.popularAdapters.PopularAdapter;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers.PopularPreciousParser;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 热门排行榜页面-入站必刷
 */
public class PopularPreciousFragment extends BaseSupportFragment {
    private LoadingRecyclerView discoverPopularPreciousLoadingRecyclerView;

    @Override
    protected int setLayout() {
        return R.layout.popular_precious;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.popular_precious_topBar);

        PopularAdapter popularAdapter = new PopularAdapter(getMainActivity(), context, PopularAdapter.PRECIOUS, false);
        popularAdapter.setHasStableIds(true);

        discoverPopularPreciousLoadingRecyclerView = findView(R.id.discover_popular_precious_loadingRecyclerView);
        discoverPopularPreciousLoadingRecyclerView.setRecyclerViewAdapter(popularAdapter);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<PopularVideo> popularVideos = (List<PopularVideo>) msg.obj;

                popularAdapter.append(popularVideos);
                discoverPopularPreciousLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        discoverPopularPreciousLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        if (InternetUtils.checkNetwork(_mActivity.getWindow().getDecorView())) {
            getPrecious();
        } else {
            discoverPopularPreciousLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
        }
    }

    private void getPrecious() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<PopularVideo> popularVideos = new PopularPreciousParser().parseData();

                Message message = receiveDataHandler.obtainMessage();
                message.obj = popularVideos;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
