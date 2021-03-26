package com.leon.biuvideo.ui.otherFragments;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.popularAdapters.PopularAdapter;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularPreciousFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularTopListFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularWeeklyFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
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
 * @Desc 热门（综合热门、每周必看、入站必刷、排行榜）页面
 */
public class PopularFragment extends BaseSupportFragment {
    private final List<PopularVideo> popularVideoList = new ArrayList<>();
    private PopularHotListParser popularHotListParser;
    private SmartRefreshRecyclerView<PopularVideo> popularHotList;

    public static PopularFragment getInstance() {
        return new PopularFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.popular_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar popularTopBar = view.findViewById(R.id.popular_topBar);
        popularTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        PopularAdapter popularAdapter = new PopularAdapter(popularVideoList, context, PopularAdapter.HOT_VIDEO);
        popularAdapter.setOnClickFirstItemListener(new PopularAdapter.OnClickFirstItemListener() {
            @Override
            public void onClickTopList() {
                start(new PopularTopListFragment());
            }

            @Override
            public void onClickWeekly() {
                start(new PopularWeeklyFragment());
            }

            @Override
            public void onClickPrecious() {
                start(new PopularPreciousFragment());
            }
        });
        popularAdapter.setHasStableIds(true);
        popularHotList = findView(R.id.popular_hot_list);
        popularHotList.setRecyclerViewAdapter(popularAdapter);
        popularHotList.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        popularHotList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!popularHotListParser.dataStatus) {
                    popularHotList.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
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
                        if (popularVideos == null || popularVideos.size() == 0) {
                            popularHotList.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            popularHotList.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            popularAdapter.append(popularVideos);
                            popularHotList.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        }
                        break;
                    case 1:
                        if (popularVideos.size() > 0) {
                            popularAdapter.append(popularVideos);
                            popularHotList.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            popularHotList.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
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

        popularHotList.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

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