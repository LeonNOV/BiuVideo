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
import com.leon.biuvideo.utils.InternetUtils;
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
    private final Context context;
    private int pageNum = 1;

    //总条目数
    private int total;

    //以获取的条目数
    private int currentCount;

    //数据状态
    private boolean dataState = true;

    private View view;
    private LayoutInflater inflater;

    private RecyclerView user_article_recyclerView;
    private SmartRefreshLayout article_smartRefresh;

    public UserArticlesFragment(long mid, Context context) {
        this.mid = mid;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_space, container, false);

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
        total = ArticleParseUtils.getArticleTotal(mid);

        //判断条目是否为0
        if (total == 0) {
            //设置无数据提示界面
            view = inflater.inflate(R.layout.fragment_no_data, null);
            return;
        }

        //获取初始数据
        List<Article> initArticles = ArticleParseUtils.parseArticle(mid, pageNum);
        currentCount += initArticles.size();

        //判断第一次是否已加载完所有数据
        if (total == currentCount) {
            dataState = false;
            article_smartRefresh.setEnabled(false);
        }

        UserArticleAdapter articleAdapter = new UserArticleAdapter(initArticles, context);
        user_article_recyclerView.setAdapter(articleAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        user_article_recyclerView.setLayoutManager(layoutManager);

        article_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

                    //结束加载更多动画
                    article_smartRefresh.finishLoadMore();

                    return;
                }

                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<Article> addOns = getNextArticles(mid, pageNum);

                            Log.d(Fuck.blue, "成功获取了" + mid + "的第" + pageNum + "页的" + addOns.size() + "条数据");

                            //添加新数据
                            articleAdapter.append(addOns);
                        }
                    }, 2000);

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

    /**
     * 获取下一页数据
     *
     * @param mid   用户ID
     * @param pageNum   页码
     * @return  返回下一页数据
     */
    private List<Article> getNextArticles(long mid, int pageNum) {
        List<Article> articles = ArticleParseUtils.parseArticle(mid, pageNum);

        currentCount += articles.size();

        //如果第一次获取的条目数小于30则设置dataState
        if (articles.size() < 30 || total == currentCount) {
            dataState = false;
            article_smartRefresh.setEnabled(false);
        }

        return articles;
    }
}
