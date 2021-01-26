package com.leon.biuvideo.ui.fragments.searchResultFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userFragmentAdapters.UserArticleAdapter;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.ui.SimpleLoadDataThread;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseLazyFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.ArticleParser;
import com.leon.biuvideo.values.SortType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * SearchResultActivity-article Fragment
 */
public class ArticleResultFragment extends BaseLazyFragment {
    private LinearLayout smart_refresh_layout_fragment_linearLayout;
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;
    private TextView search_result_no_data;

    private String keyword;

    private int count;
    private int currentCount;

    private ArticleParser articleParser;
    private List<Article> articleList;

    private UserArticleAdapter userArticleAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean dataState = true;
    private int pageNum = 1;
    private Handler handler;

    public ArticleResultFragment() {
    }

    public ArticleResultFragment(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        smart_refresh_layout_fragment_linearLayout = findView(R.id.smart_refresh_layout_fragment_linearLayout);
        search_result_no_data = findView(R.id.smart_refresh_layout_fragment_no_data);
        search_result_smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        search_result_recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void loadData() {
        SimpleLoadDataThread simpleLoadDataThread = new SimpleLoadDataThread() {
            @Override
            public void load() {
                if (articleParser == null) {
                    articleParser = new ArticleParser(context);
                }
                count = articleParser.getSearchArticleCount(keyword);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        SimpleThreadPool simpleThreadPool = simpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(simpleLoadDataThread), "loadArticleResult");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                smart_refresh_layout_fragment_linearLayout.setVisibility(View.GONE);

                if (loadState) {
                    initValues();
                }

                simpleThreadPool.cancelTask("loadArticleResult");

                return true;
            }
        });
    }

    @Override
    public void initValues() {
        if (count == 0) {
            //设置无数据提示界面
            search_result_no_data.setVisibility(View.VISIBLE);
            search_result_recyclerView.setVisibility(View.GONE);
            search_result_smartRefresh.setEnabled(false);
        } else {
            search_result_no_data.setVisibility(View.GONE);
            search_result_recyclerView.setVisibility(View.VISIBLE);
            search_result_smartRefresh.setEnabled(true);

            articleList = articleParser.articleParse(keyword, pageNum, SortType.DEFAULT);
            currentCount += articleList.size();
            pageNum++;

            if (count == articleList.size()) {
                dataState = false;

                search_result_smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || userArticleAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                userArticleAdapter = new UserArticleAdapter(articleList, context);
            }

            userArticleAdapter.append(articleList);

            initAttr();
        }
    }

    /**
     * 初始化控件属性
     */
    private void initAttr() {
        search_result_recyclerView.setLayoutManager(linearLayoutManager);
        search_result_recyclerView.setAdapter(userArticleAdapter);

        Handler handler = new Handler();

        //添加加载更多监听事件
        search_result_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Snackbar.make(view, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();

                    //结束加载更多动画
                    search_result_smartRefresh.finishLoadMore();

                    return;
                }

                if (dataState) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            getArticles();

                            //添加新数据
                            userArticleAdapter.append(articleList);
                        }
                    }, 1000);
                } else {
                    //关闭上滑刷新
                    search_result_smartRefresh.setEnabled(false);

                    Snackbar.make(view, R.string.isDone, Snackbar.LENGTH_SHORT).show();
                }

                //结束加载更多动画
                search_result_smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页数据
     */
    private void getArticles() {
        articleList = articleParser.articleParse(keyword, pageNum, SortType.DEFAULT);

        //记录获取的总数
        currentCount += articleList.size();

        //判断是否已获取完所有的数据
        if (currentCount == count) {
            dataState = false;
            search_result_smartRefresh.setEnabled(false);
        }

        pageNum++;
    }

    /**
     * 用于二次搜索刷新fragment数据
     *
     * @param keyword   搜索关键字
     */
    public void updateData(String keyword) {
        this.keyword = keyword;
        this.pageNum = 1;
        this.currentCount = 0;
        this.dataState = true;

        // 将isLoaded状态设置为“未加载状态”
        this.isLoaded = false;
        onResume();
        this.smart_refresh_layout_fragment_linearLayout.setVisibility(View.VISIBLE);

        if (articleList != null) {
            userArticleAdapter.removeAll();
        }
    }
}