package com.leon.biuvideo.ui.fragments.userFragments;

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
import com.leon.biuvideo.utils.parseDataUtils.articleParseUtils.ArticleParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * 用户已发布专栏fragment
 */
public class UserArticlesFragment extends BaseLazyFragment {
    private final long mid;

    private LinearLayout smart_refresh_layout_fragment_linearLayout;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;
    private TextView no_data;

    private ArticleParser articleParser;

    private int pageNum = 1;
    private int currentCount;
    private boolean dataState = true;

    private List<Article> articles;
    private int count;
    private Handler handler;

    public UserArticlesFragment(long mid) {
        this.mid = mid;
    }

    private LinearLayoutManager linearLayoutManager;
    private UserArticleAdapter userArticleAdapter;

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        smart_refresh_layout_fragment_linearLayout = findView(R.id.smart_refresh_layout_fragment_linearLayout);
        recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);
        smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        no_data = findView(R.id.smart_refresh_layout_fragment_no_data);

        //关闭下拉刷新
        smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void loadData() {
        SimpleLoadDataThread simpleLoadDataThread = new SimpleLoadDataThread() {
            @Override
            public void load() {
                articleParser = new ArticleParser(context, mid);
                count = articleParser.getArticleTotal();

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        SimpleThreadPool simpleThreadPool = simpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(simpleLoadDataThread), "loadUserArticles");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                smart_refresh_layout_fragment_linearLayout.setVisibility(View.GONE);

                if (loadState) {
                    initValues();
                }

                simpleThreadPool.cancelTask("loadUserArticles");

                return true;
            }
        });
    }

    @Override
    public void initValues() {
        if (count == 0) {
            //设置无数据提示界面
            no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            smartRefresh.setEnabled(false);
        } else {
            no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            smartRefresh.setEnabled(true);

            //获取初始数据
            articles = articleParser.parseArticle(pageNum);
            currentCount += articles.size();
            pageNum++;

            if (currentCount == count) {
                dataState = false;
                smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || userArticleAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                userArticleAdapter = new UserArticleAdapter(articles, context);
            }

            initAttr();
        }
    }

    private void initAttr() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userArticleAdapter);

        Handler handler = new Handler();

        //添加加载更多监听事件
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Snackbar.make(view, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();

                    //结束加载更多动画
                    smartRefresh.finishLoadMore();

                    return;
                }

                RefreshState state = refreshLayout.getState();

                //判断是否处于拖拽已释放的状态
                if (state.finishing == RefreshState.ReleaseToLoad.finishing) {
                    if (dataState) {

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //获取新数据
                                getArticles();

                                //添加新数据
                                userArticleAdapter.append(articles);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        smartRefresh.setEnabled(false);

                        Snackbar.make(view, R.string.isDone, Snackbar.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页数据
     */
    private void getArticles() {
        articles = articleParser.parseArticle(pageNum);
        currentCount += articles.size();

        //如果第一次获取的条目数小于30则设置dataState
        if (currentCount == count) {
            dataState = false;
            smartRefresh.setEnabled(false);
        }

        pageNum++;
    }
}