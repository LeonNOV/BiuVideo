package com.leon.biuvideo.ui.home.favoriteFragments;

import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters.FavoriteArticleAdapter;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
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

    private FavoriteArticleParser favoriteArticleParser;

    @Override
    protected void initView() {
        FavoriteArticleAdapter favoriteArticleAdapter = new FavoriteArticleAdapter(favoriteArticleList, context);
        favoriteArticleAdapter.setHasStableIds(true);
        view.setRecyclerViewAdapter(favoriteArticleAdapter);
        view.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getFavoriteArticles(1);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<FavoriteArticle> favoriteArticles = (List<FavoriteArticle>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (favoriteArticles.size() == 0) {
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            favoriteArticleAdapter.append(favoriteArticles);
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!favoriteArticleParser.dataStatus) {
                                view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }
                        break;
                    case 1:
                        if (favoriteArticles.size() > 0) {
                            favoriteArticleAdapter.append(favoriteArticles);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 获取专栏数据
     *
     * @param what  what
     */
    private void getFavoriteArticles(int what) {
        if (favoriteArticleParser == null) {
            favoriteArticleParser = new FavoriteArticleParser();
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<FavoriteArticle> favoriteArticles = favoriteArticleParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = favoriteArticles;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        // 获取初始数据
        getFavoriteArticles(0);
    }
}
