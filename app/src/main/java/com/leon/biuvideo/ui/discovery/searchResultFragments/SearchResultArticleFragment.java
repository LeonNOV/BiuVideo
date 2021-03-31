package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.animation.ObjectAnimator;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discover.searchResultAdapters.SearchResultArticleAdapter;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultArticle;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuAdapter;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuPopupWindow;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.SearchResultArticleParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 专栏搜索结果
 */
public class SearchResultArticleFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private String keyword;
    private String order;
    private String category;

    private final List<SearchResultArticle> searchResultArticleList = new ArrayList<>();

    private int orderSelectedPosition = 0;
    private int categorySelectedPosition = 0;

    private ObjectAnimator orderImgWhirl;
    private ObjectAnimator categoryImgWhirl;

    private TextView searchResultArticleMenuOrderText;
    private TextView searchResultArticleMenuCategoryText;
    private SearchResultArticleParser searchResultArticleParser;
    private SmartRefreshRecyclerView<SearchResultArticle> searchResultArticleData;
    private SearchResultArticleAdapter searchResultArticleAdapter;

    public SearchResultArticleFragment(String keyword) {
        this.keyword = keyword;
        this.order = "totalrank";
        this.category = "0";
    }

    @Override
    protected int setLayout() {
        return R.layout.search_ressult_article_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.search_result_article_menu_order).setOnClickListener(this);
        findView(R.id.search_result_article_menu_category).setOnClickListener(this);

        searchResultArticleMenuOrderText = findView(R.id.search_result_article_menu_order_text);
        searchResultArticleMenuCategoryText = findView(R.id.search_result_article_menu_category_text);

        ImageView searchResultArticleMenuOrderImg = findView(R.id.search_result_article_menu_order_img);
        orderImgWhirl = ObjectAnimator.ofFloat(searchResultArticleMenuOrderImg, "rotation", 0.0f, 180.0f);
        orderImgWhirl.setDuration(400);

        ImageView searchResultArticleMenuCategoryImg = findView(R.id.search_result_article_menu_category_img);
        categoryImgWhirl = ObjectAnimator.ofFloat(searchResultArticleMenuCategoryImg, "rotation", 0.0f, 180.0f);
        categoryImgWhirl.setDuration(400);

        searchResultArticleData = findView(R.id.search_result_article_data);
        searchResultArticleData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!searchResultArticleParser.dataStatus) {
                    searchResultArticleData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                } else {
                    getArticle(1);
                }
            }
        });
        searchResultArticleAdapter = new SearchResultArticleAdapter(searchResultArticleList, context);
        searchResultArticleAdapter.setHasStableIds(true);
        searchResultArticleData.setRecyclerViewAdapter(searchResultArticleAdapter);
        searchResultArticleData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<SearchResultArticle> searchResultArticles = (List<SearchResultArticle>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (searchResultArticles == null || searchResultArticles.size() == 0) {
                            searchResultArticleData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            searchResultArticleData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            searchResultArticleAdapter.append(searchResultArticles);
                            searchResultArticleData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        }
                        break;
                    case 1:
                        if (searchResultArticles != null && searchResultArticles.size() > 0) {
                            searchResultArticleAdapter.append(searchResultArticles);
                            searchResultArticleData.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            searchResultArticleData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        searchResultArticleData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        getArticle(0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_result_article_menu_order:
                orderImgWhirl.start();
                SearchResultMenuPopupWindow searchResultOrderMenuPopupWindow = new SearchResultMenuPopupWindow(v, context, SearchResultMenuPopupWindow.SearchResultArticleMenuItem.ARTICLE_ORDER_LIST, orderSelectedPosition, 4);
                searchResultOrderMenuPopupWindow.setOnSearchResultMenuItemListener(new SearchResultMenuAdapter.OnSearchResultMenuItemListener() {
                    @Override
                    public void onClickMenuItem(String[] values, int position) {
                        searchResultArticleMenuOrderText.setText(values[0]);
                        orderSelectedPosition = position;
                        searchResultOrderMenuPopupWindow.dismiss();
                        order = values[1];
                        reset();
                    }
                });
                searchResultOrderMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        orderImgWhirl.reverse();
                    }
                });

                break;
            case R.id.search_result_article_menu_category:
                categoryImgWhirl.start();
                SearchResultMenuPopupWindow searchResultCategoryMenuPopupWindow = new SearchResultMenuPopupWindow(v, context, SearchResultMenuPopupWindow.SearchResultArticleMenuItem.ARTICLE_CATEGORY_LIST, categorySelectedPosition, 4);
                searchResultCategoryMenuPopupWindow.setOnSearchResultMenuItemListener(new SearchResultMenuAdapter.OnSearchResultMenuItemListener() {
                    @Override
                    public void onClickMenuItem(String[] values, int position) {
                        searchResultArticleMenuCategoryText.setText(values[0]);
                        categorySelectedPosition = position;
                        searchResultCategoryMenuPopupWindow.dismiss();
                        category = values[1];
                        reset();
                    }
                });
                searchResultCategoryMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        categoryImgWhirl.reverse();
                    }
                });

                break;
            default:
                break;
        }
    }

    private void getArticle(int what) {
        if (searchResultArticleParser == null) {
            searchResultArticleParser = new SearchResultArticleParser(keyword, order, category);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<SearchResultArticle> searchResultArticles = searchResultArticleParser.parseData();

                Message message;
                if (what == -1) {
                    message = receiveDataHandler.obtainMessage(0);
                } else {
                    message = receiveDataHandler.obtainMessage(what);
                }
                message.obj = searchResultArticles;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     *  重置当前所有的数据
     */
    private void reset() {
        searchResultArticleData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        setLoaded(false);

        // 清空列表中的数据
        searchResultArticleAdapter.removeAll();

        searchResultArticleParser = new SearchResultArticleParser(keyword, order, category);
        getArticle(-1);
    }

    public void reSearch (String keyword) {
        this.keyword = keyword;
        this.order = "totalrank";
        this.category = "0";

        this.orderSelectedPosition = 0;
        this.categorySelectedPosition = 0;

        reset();
    }
}
