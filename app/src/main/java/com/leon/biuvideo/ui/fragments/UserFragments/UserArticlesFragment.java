package com.leon.biuvideo.ui.fragments.UserFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.leon.biuvideo.ui.activitys.ArticleActivity;
import com.leon.biuvideo.utils.articleParseUtils.ArticleParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

public class UserArticlesFragment extends Fragment {
    private long mid;
    private int pageNum = 1;


    //总条目数
    private int count;

    //以获取的条目数
    private int valueCount;

    //数据状态
    private boolean dataState;

    private View view;
    private Context context;

    private RecyclerView user_article_recyclerView;
    private SmartRefreshLayout user_smartRefresh;

    public UserArticlesFragment(long mid, int pageNum) {
        this.mid = mid;
        this.pageNum = pageNum;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_up_space, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        view = getView();
        context = getActivity();

        user_article_recyclerView = view.findViewById(R.id.user_recyclerView_space);

        user_smartRefresh = view.findViewById(R.id.user_smartRefresh);

        //关闭下拉刷新
        user_smartRefresh.setEnableRefresh(false);
    }

    private void initValue() {
        count = ArticleParseUtils.getCount(646456);

        //获取初始数据
        List<Article> initArticles = getArticles(213123, pageNum);

        //判断条目是否为0
        if (initArticles.size() == 0) {
            //显示无数据提示
        }

        UserArticleAdapter articleAdapter = new UserArticleAdapter(initArticles, context);
        articleAdapter.setOnItemClickListener(new UserArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //跳转至ArticleActivity
                Article article = initArticles.get(position);

                Intent intent = new Intent(context, ArticleActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("article", article);

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        user_article_recyclerView.setAdapter(articleAdapter);
        user_article_recyclerView.setLayoutManager(layoutManager);

        user_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (dataState) {
                    pageNum++;

                    List<Article> addOns = getArticles(1231, pageNum);
                    articleAdapter.refresh(addOns);
                } else {
                    Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
                }
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
