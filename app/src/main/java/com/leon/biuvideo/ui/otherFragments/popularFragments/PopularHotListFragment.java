package com.leon.biuvideo.ui.otherFragments.popularFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.adapters.homeAdapters.popularAdapters.PopularAdapter;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers.PopularHotListParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 热门排行榜页面-综合热门
 */
public class PopularHotListFragment extends BaseSupportFragmentWithSrr<PopularVideo> {
    private final List<PopularVideo> popularVideoList = new ArrayList<>();
    private PopularHotListParser popularHotListParser;

    @Override
    protected void initView() {
        PopularAdapter popularAdapter = new PopularAdapter(popularVideoList, context, PopularAdapter.HOT_VIDEO);
        popularAdapter.setHasStableIds(true);

        view.setRecyclerViewAdapter(popularAdapter);
        view.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!popularHotListParser.dataStatus) {
                    view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                } else {
                    getPopularHotList(1);
                }
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<PopularVideo> popularVideos = (List<PopularVideo>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (popularVideos.size() == 0) {
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            popularAdapter.append(popularVideos);
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!popularHotListParser.dataStatus) {
                                view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }
                        break;
                    case 1:
                        if (popularVideos.size() > 0) {
                            popularAdapter.append(popularVideos);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        getPopularHotList(0);
    }

    private void getPopularHotList(int what) {
        if (popularHotListParser == null) {
            popularHotListParser = new PopularHotListParser();
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<PopularVideo> popularVideos = popularHotListParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = popularVideos;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
