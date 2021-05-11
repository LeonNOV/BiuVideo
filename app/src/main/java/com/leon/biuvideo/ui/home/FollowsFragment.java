package com.leon.biuvideo.ui.home;

import android.os.Bundle;
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
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.FollowsParser;
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
    private final String mid;
    private final boolean isBiliUser;

    private final List<Follow> followList = new ArrayList<>();
    private SmartRefreshRecyclerView<Follow> followsSmartRefreshLoadingRecyclerView;
    private DataLoader<Follow> followDataLoader;

    public FollowsFragment(boolean isBiliUser, String mid) {
        this.isBiliUser = isBiliUser;
        this.mid = mid;
    }

    public static FollowsFragment getInstance(boolean isBiliUser, String mid) {
        return new FollowsFragment(isBiliUser, mid);
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

        followsSmartRefreshLoadingRecyclerView = findView(R.id.follows_smartRefreshLoadingRecyclerView);
        FollowsAdapter followsAdapter = new FollowsAdapter(followList, context, isBiliUser);
        followsAdapter.setHasStableIds(true);
        followsSmartRefreshLoadingRecyclerView.setRecyclerViewAdapter(followsAdapter);
        followsSmartRefreshLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        followsSmartRefreshLoadingRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                followDataLoader.insertData(false);
            }
        });

        followDataLoader = new DataLoader<>(new FollowsParser(context, mid), followsSmartRefreshLoadingRecyclerView, followsAdapter, this);
        setOnLoadListener(followDataLoader);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        followsSmartRefreshLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        followDataLoader.insertData(true);
    }
}
