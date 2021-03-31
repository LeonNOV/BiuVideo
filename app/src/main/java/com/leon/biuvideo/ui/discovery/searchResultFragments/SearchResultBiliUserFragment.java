package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.animation.ObjectAnimator;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discover.searchResultAdapters.SearchResultBiliUserAdapter;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBiliUser;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuAdapter;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuPopupWindow;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
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
    private String keyword;
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
    private SearchResultBiliUserParser searchResultBiliUserParser;

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
                if (!searchResultBiliUserParser.dataStatus) {
                    searchResultBiliUserData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                } else {
                    getArticle(1);
                }
            }
        });

        searchResultBiliUserAdapter = new SearchResultBiliUserAdapter(searchResultBiliUserList, context);
        searchResultBiliUserAdapter.setHasStableIds(true);
        searchResultBiliUserData.setRecyclerViewAdapter(searchResultBiliUserAdapter);
        searchResultBiliUserData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<SearchResultBiliUser> searchResultBiliUsers = (List<SearchResultBiliUser>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (searchResultBiliUsers == null || searchResultBiliUsers.size() == 0) {
                            searchResultBiliUserData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            searchResultBiliUserData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            searchResultBiliUserAdapter.append(searchResultBiliUsers);
                            searchResultBiliUserData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        }
                        break;
                    case 1:
                        if (searchResultBiliUsers != null && searchResultBiliUsers.size() > 0) {
                            searchResultBiliUserAdapter.append(searchResultBiliUsers);
                            searchResultBiliUserData.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            searchResultBiliUserData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
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
        searchResultBiliUserData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        getArticle(0);
    }

    private void getArticle(int what) {
        if (searchResultBiliUserParser == null) {
            searchResultBiliUserParser = new SearchResultBiliUserParser(keyword, order, orderSort, userType);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<SearchResultBiliUser> searchResultBiliUsers = searchResultBiliUserParser.parseData();

                Message message;
                if (what == -1) {
                    message = receiveDataHandler.obtainMessage(0);
                } else {
                    message = receiveDataHandler.obtainMessage(what);
                }
                message.obj = searchResultBiliUsers;
                receiveDataHandler.sendMessage(message);
            }
        });
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
        setLoaded(false);

        // 清空列表中的数据
        searchResultBiliUserAdapter.removeAll();

        searchResultBiliUserParser = new SearchResultBiliUserParser(keyword, order, orderSort, userType);
        getArticle(-1);
    }

    public void reSearch (String keyword) {
        this.keyword = keyword;
        this.order = null;
        this.orderSort = null;
        this.userType = null;

        this.orderSelectedPosition = 0;
        this.categorySelectedPosition = 0;
        reset();
    }
}
