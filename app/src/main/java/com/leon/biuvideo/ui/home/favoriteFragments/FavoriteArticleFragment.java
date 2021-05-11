package com.leon.biuvideo.ui.home.favoriteFragments;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters.FavoriteArticleAdapter;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.FavoriteArticleParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 收藏页面-专栏收藏
 */
public class FavoriteArticleFragment extends BaseSupportFragmentWithSrr<FavoriteArticle> {
    private final List<FavoriteArticle> favoriteArticleList = new ArrayList<>();
    private DataLoader<FavoriteArticle> favoriteArticleDataLoader;

    @Override
    protected void onLazyLoad() {
        view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        favoriteArticleDataLoader.insertData(true);
    }

    @Override
    protected void initView() {
        FavoriteArticleAdapter favoriteArticleAdapter = new FavoriteArticleAdapter(favoriteArticleList, context);
        favoriteArticleAdapter.setHasStableIds(true);
        view.setRecyclerViewAdapter(favoriteArticleAdapter);
        view.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                favoriteArticleDataLoader.insertData(false);
            }
        });

        favoriteArticleDataLoader = new DataLoader<>(new FavoriteArticleParser(), view, favoriteArticleAdapter, this);
    }
}
