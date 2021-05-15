package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters.SearchResultArticleAdapter;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultArticle;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuAdapter;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuPopupWindow;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.SearchResultArticleParser;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 专栏搜索结果
 */
public class SearchResultArticleFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private final String keyword;
    private String order;
    private String category;

    private int orderSelectedPosition = 0;
    private int categorySelectedPosition = 0;

    private ObjectAnimator orderImgWhirl;
    private ObjectAnimator categoryImgWhirl;

    private TextView searchResultArticleMenuOrderText;
    private TextView searchResultArticleMenuCategoryText;

    private DataLoader<SearchResultArticle> searchResultArticleDataLoader;

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

        searchResultArticleDataLoader = new DataLoader<>(context, new SearchResultArticleParser(keyword, order, category),
                R.id.search_result_article_data,
                new SearchResultArticleAdapter(getMainActivity(), context),
                this);
    }

    @Override
    protected void onLazyLoad() {
        searchResultArticleDataLoader.insertData(true);
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

    /**
     *  重置当前所有的数据
     */
    private void reset() {
        searchResultArticleDataLoader.reset(new SearchResultArticleParser(keyword, order, category));
    }
}
