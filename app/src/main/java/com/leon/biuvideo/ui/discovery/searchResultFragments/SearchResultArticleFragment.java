package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.animation.ObjectAnimator;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuAdapter;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuPopupWindow;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 专栏搜索结果
 */
public class SearchResultArticleFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private int orderSelectedPosition = 0;
    private int categorySelectedPosition = 0;

    private ObjectAnimator orderImgWhirl;
    private ObjectAnimator categoryImgWhirl;

    private TextView searchResultArticleMenuOrderText;
    private TextView searchResultArticleMenuCategoryText;

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

        SmartRefreshRecyclerView searchResultArticleData = findView(R.id.search_result_article_data);
        searchResultArticleData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {

            }
        });
    }

    @Override
    protected void onLazyLoad() {

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
}
