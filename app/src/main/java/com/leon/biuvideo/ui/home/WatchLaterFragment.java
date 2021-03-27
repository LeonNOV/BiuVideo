package com.leon.biuvideo.ui.home;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.WatchLaterAdapter;
import com.leon.biuvideo.beans.homeBeans.WatchLater;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/15
 * @Desc 稍后观看页面
 */
public class WatchLaterFragment extends BaseSupportFragment {
    private final List<WatchLater> watchLaterList;

    public WatchLaterFragment(List<WatchLater> watchLaterList) {
        this.watchLaterList = watchLaterList;
    }

    @Override
    protected int setLayout() {
        return R.layout.watch_later_fragment;
    }

    @Override
    protected void initView() {
        ViewUtils.setStatusBar(getActivity(), true);
        SimpleTopBar watchLaterFragmentTopBar = findView(R.id.watch_later_fragment_topBar);
        watchLaterFragmentTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
                ViewUtils.setStatusBar(getActivity(), false);
            }

            @Override
            public void onRight() {

            }
        });

        LoadingRecyclerView watchLaterFragmentLoadingRecyclerView = findView(R.id.watch_later_fragment_loadingRecyclerView);

        watchLaterFragmentLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        watchLaterFragmentLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        watchLaterFragmentLoadingRecyclerView.setRecyclerViewAdapter(new WatchLaterAdapter(watchLaterList, context));
        watchLaterFragmentLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }
}
