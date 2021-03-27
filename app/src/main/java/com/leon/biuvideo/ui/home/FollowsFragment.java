package com.leon.biuvideo.ui.home;

import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.FollowsAdapter;
import com.leon.biuvideo.beans.userBeans.Follow;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.FollowsParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/5
 * @Desc 关注数据界面
 */
public class FollowsFragment extends BaseSupportFragment {
    private final List<Follow> followList = new ArrayList<>();
    private FollowsAdapter followsAdapter;
    private FollowsParser followsParser;
    private SmartRefreshRecyclerView<Follow> followsSmartRefreshLoadingRecyclerView;

    public static FollowsFragment getInstance() {
        return new FollowsFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.follows_fragment;
    }

    @Override
    protected void initView() {
        ViewUtils.setStatusBar(getActivity(), true);
        SimpleTopBar followsTopBar = findView(R.id.follows_topBar);
        followsTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
                ViewUtils.setStatusBar(getActivity(), false);
            }

            @Override
            public void onRight() {
                Toast.makeText(context, "more", Toast.LENGTH_SHORT).show();
            }
        });

        followsSmartRefreshLoadingRecyclerView = findView(R.id.follows_smartRefreshLoadingRecyclerView);
        followsAdapter = new FollowsAdapter(followList, context);
        followsAdapter.setHasStableIds(true);
        followsSmartRefreshLoadingRecyclerView.setRecyclerViewAdapter(followsAdapter);
        followsSmartRefreshLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        followsSmartRefreshLoadingRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                // 获取下一页数据
                getFollows(1);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<Follow> follows = (List<Follow>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (follows.size() == 0) {
                            followsSmartRefreshLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            followsSmartRefreshLoadingRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            followsAdapter.append(follows);
                            followsSmartRefreshLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!followsParser.dataStatus) {
                                followsSmartRefreshLoadingRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }
                        break;
                    case 1:

                        if (follows.size() > 0) {
                            followsAdapter.append(follows);
                            followsSmartRefreshLoadingRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            followsSmartRefreshLoadingRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
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
        followsSmartRefreshLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        // 获取初始数据
        getFollows(0);
    }

    /**
     * 获取关注数据
     *
     * @return  Follow集合
     */
    private void getFollows(int what) {
        if (followsParser == null) {
            followsParser = new FollowsParser(context);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取关注数据
                List<Follow> follows = followsParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = follows;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
