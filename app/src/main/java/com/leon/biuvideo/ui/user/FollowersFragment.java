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
        followerAdapter.setHasStableIds(true);
        followerSmartRefreshRecyclerView.setRecyclerViewAdapter(followerAdapter);
        followerSmartRefreshRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        followerSmartRefreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                // 获取下一页数据
                getFollowers(1);
            }
        });

        followerSmartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        // 第一次加载数据
        getFollowers(0);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {

                List<Follower> followers = (List<Follower>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (followers.size() == 0) {
                            followerSmartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            followerSmartRefreshRecyclerView.setEnableLoadMore(false);
                        } else {
                            followerAdapter.append(followers);
                            followerSmartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!followersParser.dataStatus) {
                                followerSmartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }
                        break;
                    case 1:
                        if (followers.size() > 0) {
                            followerAdapter.append(followers);
                            followerSmartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            followerSmartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }

                        break;
                    default:
                        break;
                }
            }
        });


    }

    /**
     * 获取粉丝数据
     *
     * @return  Follower集合
     */
    private void getFollowers(int what) {
        if (followersParser == null) {
            followersParser = new FollowersParser(context);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取粉丝数据
                List<Follower> followers = followersParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = followers;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
