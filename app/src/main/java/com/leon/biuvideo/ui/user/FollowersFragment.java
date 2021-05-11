package com.leon.biuvideo.ui.user;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.FollowerAdapter;
import com.leon.biuvideo.beans.userBeans.Follower;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.FollowersParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/18
 * @Desc 粉丝页面
 */
public class FollowersFragment extends BaseSupportFragment {
    private final boolean isBiliUser;
    private final String mid;

    private final List<Follower> followerList = new ArrayList<>();
    private DataLoader<Follower> followerDataLoader;

    public FollowersFragment(boolean isBiliUser, String mid) {
        this.isBiliUser = isBiliUser;
        this.mid = mid;
    }

    public static FollowersFragment getInstance(boolean isBiliUser, String mid) {
        return new FollowersFragment(isBiliUser, mid);
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
        FollowerAdapter followerAdapter = new FollowerAdapter(followerList, context, isBiliUser);
        followerAdapter.setHasStableIds(true);
        followerSmartRefreshRecyclerView.setRecyclerViewAdapter(followerAdapter);
        followerSmartRefreshRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        followerSmartRefreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                followerDataLoader.insertData(false);
            }
        });

        followerSmartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        followerDataLoader = new DataLoader<>(new FollowersParser(context, mid), followerSmartRefreshRecyclerView, followerAdapter, this);
        followerDataLoader.insertData(true);
    }
}
