package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters.SearchResultBiliUserAdapter;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBiliUser;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuAdapter;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuPopupWindow;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.SearchResultBiliUserParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 用户搜索结果
 */
public class SearchResultBiliUserFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private final String keyword;
    private String order;
    private String orderSort;
    private String userType;

    private final List<SearchResultBiliUser> searchResultBiliUserList = new ArrayList<>();

    private int orderSelectedPosition = 0;
    private int categorySelectedPosition = 0;

    private TextView searchResultBiliUserMenuOrderText;
    private TextView searchResultBiliUserMenuCategoryText;
    private ObjectAnimator orderImgWhirl;
    private ObjectAnimator categoryImgWhirl;
    private SearchResultBiliUserAdapter searchResultBiliUserAdapter;
    private SmartRefreshRecyclerView<SearchResultBiliUser> searchResultBiliUserData;
    private DataLoader<SearchResultBiliUser> searchResultBiliUserDataLoader;

    public SearchResultBiliUserFragment(String keyword) {
        this.keyword = keyword;
    }

    @Override
    protected int setLayout() {
        return R.layout.search_ressult_biliuser_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.search_result_bili_user_menu_order).setOnClickListener(this);
        findView(R.id.search_result_bili_user_menu_category).setOnClickListener(this);

        searchResultBiliUserMenuOrderText = findView(R.id.search_result_bili_user_menu_order_text);
        searchResultBiliUserMenuCategoryText = findView(R.id.search_result_bili_user_menu_category_text);

        ImageView searchResultBiliUserMenuOrderImg = findView(R.id.search_result_bili_user_menu_order_img);
        orderImgWhirl = ObjectAnimator.ofFloat(searchResultBiliUserMenuOrderImg, "rotation", 0.0f, 180.0f);
        orderImgWhirl.setDuration(400);

        ImageView searchResultBiliUserMenuCategoryImg = findView(R.id.search_result_bili_user_menu_category_img);
        categoryImgWhirl = ObjectAnimator.ofFloat(searchResultBiliUserMenuCategoryImg, "rotation", 0.0f, 180.0f);
        categoryImgWhirl.setDuration(400);

        searchResultBiliUserData = findView(R.id.search_result_bili_user_data);
        searchResultBiliUserData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                searchResultBiliUserDataLoader.insertData(false);
            }
        });

        searchResultBiliUserAdapter = new SearchResultBiliUserAdapter(searchResultBiliUserList, context);
        searchResultBiliUserAdapter.setHasStableIds(true);
        searchResultBiliUserData.setRecyclerViewAdapter(searchResultBiliUserAdapter);
        searchResultBiliUserData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        searchResultBiliUserDataLoader = new DataLoader<>(new SearchResultBiliUserParser(keyword, order, orderSort, userType), searchResultBiliUserData, searchResultBiliUserAdapter, this);
    }

    @Override
    protected void onLazyLoad() {
        searchResultBiliUserData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        searchResultBiliUserDataLoader.insertData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_result_bili_user_menu_order:
                orderImgWhirl.start();
                SearchResultMenuPopupWindow searchResultOrderMenuPopupWindow = new SearchResultMenuPopupWindow(v, context, SearchResultMenuPopupWindow.SearchResultBiliUserMenuItem.BILI_USER_ORDER_LIST, orderSelectedPosition, 2);
                searchResultOrderMenuPopupWindow.setOnSearchResultMenuItemListener(new SearchResultMenuAdapter.OnSearchResultMenuItemListener() {
                    @Override
                    public void onClickMenuItem(String[] values, int position) {
                        searchResultBiliUserMenuOrderText.setText(values[0]);
                        orderSelectedPosition = position;
                        searchResultOrderMenuPopupWindow.dismiss();
                        if (!"".equals(values[1])) {
                            String[] split = values[1].split("\\|");
                            order = split[0];
                            orderSort = split[1];
                        } else {
                            order = null;
                            orderSort = null;
                        }
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
            case R.id.search_result_bili_user_menu_category:
                categoryImgWhirl.start();
                SearchResultMenuPopupWindow searchResultCateGoryMenuPopupWindow = new SearchResultMenuPopupWindow(v, context, SearchResultMenuPopupWindow.SearchResultBiliUserMenuItem.BILI_USER_CATEGORY_LIST, categorySelectedPosition, 2);
                searchResultCateGoryMenuPopupWindow.setOnSearchResultMenuItemListener(new SearchResultMenuAdapter.OnSearchResultMenuItemListener() {
                    @Override
                    public void onClickMenuItem(String[] values, int position) {
                        searchResultBiliUserMenuCategoryText.setText(values[0]);
                        categorySelectedPosition = position;
                        searchResultCateGoryMenuPopupWindow.dismiss();
                        userType = values[1];
                        reset();
                    }
                });
                searchResultCateGoryMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
        searchResultBiliUserData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        searchResultBiliUserAdapter.removeAll();

        searchResultBiliUserDataLoader.setParserInterface(new SearchResultBiliUserParser(keyword, order, orderSort, userType));
        searchResultBiliUserDataLoader.insertData(true);
    }
}
