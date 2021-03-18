package com.leon.biuvideo.ui.user;

import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userAdapters.FollowerAdapter;
import com.leon.biuvideo.beans.userBeans.Follower;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.FollowersParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/18
 * @Desc 我的粉丝页面
 */
public class FollowersFragment extends BaseSupportFragment {
    private FollowersParser followersParser;

    private FollowerAdapter followerAdapter;
    private final List<Follower> followerList = new ArrayList<>();

    public static FollowersFragment getInstance() {
        return new FollowersFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.followers_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar followersTopBar = findView(R.id.followers_topBar);
        followersTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        SmartRefreshRecyclerView<Follower> followerSmartRefreshRecyclerView = findView(R.id.followers_smartRefreshLoadingRecyclerView);
        followerAdapter = new FollowerAdapter(followerList, context);
        followerSmartRefreshRecyclerView.setRecyclerViewAdapter(followerAdapter);
        followerSmartRefreshRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        followerSmartRefreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                // 加载数据

            }
        });

        followerSmartRefreshRecyclerView.setStatus(LoadingRecyclerView.LOADING);
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                if (msg.what == 0) {
                    List<Follower> followers = (List<Follower>) msg.obj;

                    if (followers.size() == 0) {
                        followerSmartRefreshRecyclerView.setStatus(LoadingRecyclerView.NO_DATA);
                        followerSmartRefreshRecyclerView.setEnableLoadMore(false);
                    } else {
                        followerAdapter.append(followers);
                        followerSmartRefreshRecyclerView.setStatus(LoadingRecyclerView.LOADING_FINISH);
                    }
                }
            }
        });

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取粉丝数据
                List<Follower> followers = getFollowers();

                Message message = receiveDataHandler.obtainMessage(0);
                message.obj = followers;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     * 获取粉丝数据
     *
     * @return  Follower集合
     */
    private List<Follower> getFollowers() {
        if (followersParser == null) {
            followersParser = new FollowersParser(context);
        }

        return followersParser.parseData();
    }
}
