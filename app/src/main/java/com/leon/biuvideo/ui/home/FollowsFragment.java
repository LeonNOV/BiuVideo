package com.leon.biuvideo.ui.home;

import android.os.Message;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.home.FollowsAdapter;
import com.leon.biuvideo.beans.userBeans.Follow;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
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

    public static FollowsFragment getInstance() {
        return new FollowsFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.follows_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar followsTopBar = findView(R.id.follows_topBar);
        followsTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(context, "more", Toast.LENGTH_SHORT).show();
            }
        });

        SmartRefreshRecyclerView<Follow> followsSmartRefreshLoadingRecyclerView = findView(R.id.follows_smartRefreshLoadingRecyclerView);
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

        followsSmartRefreshLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING);

        // 获取初始数据
        getFollows(0);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<Follow> follows = (List<Follow>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (follows.size() == 0) {
                            followsSmartRefreshLoadingRecyclerView.setStatus(LoadingRecyclerView.NO_DATA);
                            followsSmartRefreshLoadingRecyclerView.setEnableLoadMore(false);
                        } else {
                            followsAdapter.append(follows);
                            followsSmartRefreshLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!followsParser.dataStatus) {
                                followsSmartRefreshLoadingRecyclerView.setLoadStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }
                        break;
                    case 1:

                        if (follows.size() > 0) {
                            followsAdapter.append(follows);
                            followsSmartRefreshLoadingRecyclerView.setLoadStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            followsSmartRefreshLoadingRecyclerView.setLoadStatus(SmartRefreshRecyclerView.NO_DATA);
                        }

                        break;
                    default:
                        break;
                }
            }
        });
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
