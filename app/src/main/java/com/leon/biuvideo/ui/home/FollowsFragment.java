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
                // 加载数据

            }
        });

        followsSmartRefreshLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING);

        // 第一次加载数据
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                if (msg.what == 0) {
                    List<Follow> follows = (List<Follow>) msg.obj;

                    if (follows.size() == 0) {
                        followsSmartRefreshLoadingRecyclerView.setStatus(LoadingRecyclerView.NO_DATA);
                        followsSmartRefreshLoadingRecyclerView.setEnableLoadMore(false);
                    } else {
                        followsAdapter.append(follows);
                        followsSmartRefreshLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING_FINISH);
                    }
                }
            }
        });

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取关注数据
                List<Follow> follows = getFollows();

                Message message = receiveDataHandler.obtainMessage(0);
                message.obj = follows;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     * 获取关注数据
     *
     * @return  Follow集合
     */
    private List<Follow> getFollows() {
        if (followsParser == null) {
            followsParser = new FollowsParser(context);
        }

        return followsParser.parseData();
    }
}
