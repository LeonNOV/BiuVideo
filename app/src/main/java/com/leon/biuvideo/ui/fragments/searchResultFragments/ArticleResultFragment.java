package com.leon.biuvideo.ui.fragments.searchResultFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.OrderType;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.ArticleParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchResultActivity-article Fragment
 */
public class ArticleResultFragment extends Fragment {
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;
    private TextView search_result_no_data;

    private String keyword;

    private int count;
    private int currentCount;

    private ArticleParser articleParser;
    private List<Article> articles;

    private Context context;
    private UserArticleAdapter userArticleAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean dataState = true;
    private int pageNum = 1;

    private View view;

    public ArticleResultFragment() {
    }

    public ArticleResultFragment(String keyword) {
        this.keyword = keyword;
    }

    public static ArticleResultFragment getInstance(String keyword) {
        ArticleResultFragment articleResultFragment = new ArticleResultFragment(keyword);

        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        articleResultFragment.setArguments(bundle);

        return articleResultFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.smart_refresh_layout_fragment, container, false);

        initView();
        initVisibility();

        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        context = getContext();

        search_result_no_data = view.findViewById(R.id.smart_refresh_layout_fragment_no_data);
        search_result_smartRefresh = view.findViewById(R.id.smart_refresh_layout_fragment_smartRefresh);
        search_result_recyclerView = view.findViewById(R.id.smart_refresh_layout_fragment_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    /**
     * 初始化控件Visibility
     */
    private void initVisibility() {
        if (getDataState()) {
            //设置无数据提示界面
            search_result_no_data.setVisibility(View.VISIBLE);
            search_result_recyclerView.setVisibility(View.GONE);
            search_result_smartRefresh.setEnabled(false);
        } else {
            search_result_no_data.setVisibility(View.GONE);
            search_result_recyclerView.setVisibility(View.VISIBLE);
            search_result_smartRefresh.setEnabled(true);

            if (articleParser == null) {
                articleParser = new ArticleParser();
            }

            List<Article> newArticles = articleParser.articleParse(keyword, pageNum, OrderType.DEFAULT);

            currentCount += newArticles.size();

            if (count == newArticles.size()) {
                dataState = false;

                search_result_smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || userArticleAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                userArticleAdapter = new UserArticleAdapter(newArticles, context);
            }

            articles = newArticles;

            initAttr();
        }
    }

    /**
     * 初始化控件属性
     */
    private void initAttr() {
        search_result_recyclerView.setLayoutManager(linearLayoutManager);
        search_result_recyclerView.setAdapter(userArticleAdapter);

        //添加加载更多监听事件
        search_result_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

                    //结束加载更多动画
                    search_result_smartRefresh.finishLoadMore();

                    return;
                }

                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<Article> addOns = getArticles(pageNum);

                            Log.d(Fuck.blue, "成功获取了第" + pageNum + "页的" + addOns.size() + "条数据");

                            //添加新数据
                            userArticleAdapter.append(addOns);
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

    /**
     * 获取数据状态，同时对总数量进行赋值
     *
     * @return  返回是否等于0
     */
    private boolean getDataState() {
        //获取结果总数，最大为1000， 最小为0
        count = ArticleParser.getSearchArticleCount(keyword);
        return count == 0;
    }

    /**
     * 获取下一页数据
     *
     * @param pageNum   页码
     * @return  返回下一页数据
     */
    private List<Article> getArticles(int pageNum) {
        List<Article> articles = articleParser.articleParse(keyword, pageNum, OrderType.DEFAULT);

        //记录获取的总数
        currentCount += articles.size();

        //判断是否已获取完所有的数据
        if (articles.size() < 20 || currentCount == count) {
            dataState = false;
        }

        return articles;
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

        initVisibility();

        ArrayList<Article> temp = new ArrayList<>(this.articles);

        userArticleAdapter.removeAll();
        userArticleAdapter.append(temp);
    }
}