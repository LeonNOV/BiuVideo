package com.leon.biuvideo.ui.fragments.userFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.UserArticleAdapter;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.parseDataUtils.articleParseUtils.ArticleParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * 用户主页-专栏Fragment
 */
public class UserArticlesFragment extends Fragment {
    private final long mid;
    private int pageNum;
    private Context context;

    //总条目数
    private int count;

    //以获取的条目数
    private int valueCount;

    //数据状态
    private boolean dataState;

    private View view;

    private RecyclerView user_article_recyclerView;
    private SmartRefreshLayout article_smartRefresh;

    public UserArticlesFragment(long mid, int pageNum, Context context) {
        this.mid = mid;
        this.pageNum = pageNum;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_up_space, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        user_article_recyclerView = view.findViewById(R.id.user_recyclerView_space);

        article_smartRefresh = view.findViewById(R.id.user_smartRefresh);

        //关闭下拉刷新
        article_smartRefresh.setEnableRefresh(false);
    }

    private void initValue() {
        count = ArticleParseUtils.getCount(mid);

        //判断条目是否为0
        if (count == 0) {
            //显示无数据提示

            return;
        }

        //获取初始数据
        List<Article> initArticles = getArticles(mid, pageNum);

        UserArticleAdapter articleAdapter = new UserArticleAdapter(initArticles, context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        user_article_recyclerView.setAdapter(articleAdapter);
        user_article_recyclerView.setLayoutManager(layoutManager);

        article_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<Article> addOns = getArticles(mid, pageNum);

                            Log.d(Fuck.blue, "成功获取了" + mid + "的第" + pageNum + "页的" + addOns.size() + "条数据");

                            //添加新数据
                            articleAdapter.append(addOns);
                        }
                    }, 1000);

                } else {
                    //关闭上滑刷新
                    article_smartRefresh.setEnabled(false);

                    Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
                }

                //结束加载更多动画
                article_smartRefresh.finishLoadMore();
            }
        });
    }

    private List<Article> getArticles(long mid, int pageNum) {
        List<Article> articles = ArticleParseUtils.parseArticle(mid, pageNum);

        valueCount += articles.size();

        //如果第一次获取的条目数小于30则设置dataState
        if (articles.size() < 30 || count == valueCount) {
            dataState = false;
        } else {
            dataState = true;
        }

        return articles;
    }
}
