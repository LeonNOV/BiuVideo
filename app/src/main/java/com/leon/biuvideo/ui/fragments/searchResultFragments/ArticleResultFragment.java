package com.leon.biuvideo.ui.fragments.searchResultFragments;

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
import com.leon.biuvideo.utils.OrderType;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.ArticleParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * 搜索结果-专栏片段
 */
public class ArticleResultFragment extends Fragment {
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;

    private final String keyword;

    private int count;
    private int currentCount;

    private ArticleParser articleParser;
    private List<Article> articles;

    private LayoutInflater inflater;
    private Context context;

    private boolean dataState = true;
    private int pageNum = 1;

    private View view;

    public ArticleResultFragment(String keyword) {
        this.keyword = keyword;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.search_result_fragment, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        context = getContext();

        search_result_smartRefresh = view.findViewById(R.id.search_result_smartRefresh);
        search_result_recyclerView = view.findViewById(R.id.search_result_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    private void initValue() {
        //获取结果总数，最大为1000， 最小为0
        count = ArticleParser.getSearchArticleCount(keyword);

        //判断获取的数据条目是否为0
        if (count == 0) {
            //设置无数据提示界面
            view = inflater.inflate(R.layout.fragment_no_data, null);
            return;
        }

        articleParser = new ArticleParser();
        articles = articleParser.articleParse(keyword, pageNum, OrderType.DEFAULT);

        //判断第一次加载是否已加载完所有数据
        if (count > articles.size()) {
            dataState = false;
        }

        UserArticleAdapter userArticleAdapter = new UserArticleAdapter(articles, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        search_result_recyclerView.setLayoutManager(linearLayoutManager);
        search_result_recyclerView.setAdapter(userArticleAdapter);

        //添加加载更多监听事件
        search_result_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<Article> addOns = getArticles(pageNum);

                            Log.d(Fuck.blue, "成功获取了第" + pageNum + "页的" + addOns.size() + "条数据");

                            //添加新数据
                            userArticleAdapter.refresh(addOns);
                        }
                    }, 1000);
                } else {
                    //关闭上滑刷新
                    search_result_smartRefresh.setEnabled(false);

                    Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
                }

                //结束加载更多动画
                search_result_smartRefresh.finishLoadMore();
            }
        });
    }

    public List<Article> getArticles(int pageNum) {
        List<Article> articles = articleParser.articleParse(keyword, pageNum, OrderType.DEFAULT);

        //记录获取的总数
        currentCount += articles.size();

        //判断是否已获取完所有的数据
        if (articles.size() < 30 || currentCount == count) {
            dataState = false;
        }

        return articles;
    }
}
