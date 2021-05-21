package com.leon.biuvideo.ui.views.searchResultViews;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 搜索界面筛选条件弹窗
 */
public class SearchResultMenuPopupWindow extends PopupWindow {
    private final View anchor;
    private final Context context;
    private final List<String[]> menuItems;
    private final int selectedPosition;
    private final int spanCount;

    private SearchResultMenuAdapter.OnSearchResultMenuItemListener onSearchResultMenuItemListener;

    public SearchResultMenuPopupWindow (View anchor, Context context, List<String[]> menuItems, int selectedPosition, int spanCount) {
        this.anchor = anchor;
        this.context = context;
        this.menuItems = menuItems;
        this.selectedPosition = selectedPosition;
        this.spanCount = spanCount;

        initView();
    }

    public void setOnSearchResultMenuItemListener(SearchResultMenuAdapter.OnSearchResultMenuItemListener onSearchResultMenuItemListener) {
        this.onSearchResultMenuItemListener = onSearchResultMenuItemListener;
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_menu_popup_window, null);

        RecyclerView recyclerView = view.findViewById(R.id.search_result_menu_popup_window_data);
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount));
        SearchResultMenuAdapter searchResultMenuAdapter = new SearchResultMenuAdapter(menuItems, selectedPosition, context);
        searchResultMenuAdapter.setHasStableIds(true);
        searchResultMenuAdapter.setOnSearchResultMenuItemListener(new SearchResultMenuAdapter.OnSearchResultMenuItemListener() {
            @Override
            public void onClickMenuItem(String[] values, int position) {
                if (onSearchResultMenuItemListener != null) {
                    onSearchResultMenuItemListener.onClickMenuItem(values, position);
                }
            }
        });
        recyclerView.setAdapter(searchResultMenuAdapter);

        this.setContentView(view);
        this.setHeight(ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.MarginLayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.showAsDropDown(anchor, 0, 0, Gravity.CENTER_HORIZONTAL);
    }

    /**
     * 视频搜索结果筛选条件
     */
    public static class SearchResultVideoMenuItem {
        public final static List<String[]> VIDEO_PARTITIONS_LIST = new ArrayList<>();
        public final static List<String[]> VIDEO_ORDER_LIST = new ArrayList<>();
        public final static List<String[]> VIDEO_LENGTH_LIST = new ArrayList<>();

        static {
            VIDEO_PARTITIONS_LIST.add(new String[]{"全部分区", "0"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"番剧", "13"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"国创", "167"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"纪录片", "177"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"动画", "1"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"音乐", "3"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"舞蹈", "129"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"游戏", "4"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"知识", "36"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"数码", "188"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"生活", "160"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"美食", "211"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"动物圈", "217"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"鬼畜", "119"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"时尚", "155"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"资讯", "202"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"娱乐", "5"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"影视", "181"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"电影", "23"});
            VIDEO_PARTITIONS_LIST.add(new String[]{"电视剧", "11"});

            VIDEO_ORDER_LIST.add(new String[]{"默认排序", "totalrank"});
            VIDEO_ORDER_LIST.add(new String[]{"播放多", "click"});
            VIDEO_ORDER_LIST.add(new String[]{"新发布", "pubdate"});
            VIDEO_ORDER_LIST.add(new String[]{"弹幕多", "dm"});
            VIDEO_ORDER_LIST.add(new String[]{"收藏多", "stow"});

            VIDEO_LENGTH_LIST.add(new String[]{"全部时长", "0"});
            VIDEO_LENGTH_LIST.add(new String[]{"0–10分钟", "1"});
            VIDEO_LENGTH_LIST.add(new String[]{"10–30分钟", "2"});
            VIDEO_LENGTH_LIST.add(new String[]{"30–60分钟", "3"});
            VIDEO_LENGTH_LIST.add(new String[]{"60分钟+", "4"});
        }
    }

    /**
     * 专栏搜索结果筛选条件
     */
    public static class SearchResultArticleMenuItem {
        public final static List<String[]> ARTICLE_ORDER_LIST = new ArrayList<>();
        public final static List<String[]> ARTICLE_CATEGORY_LIST = new ArrayList<>();

        static {
            ARTICLE_ORDER_LIST.add(new String[]{"默认排序", "totalrank"});
            ARTICLE_ORDER_LIST.add(new String[]{"发布时间", "pubdate"});
            ARTICLE_ORDER_LIST.add(new String[]{"按阅读数", "click"});
            ARTICLE_ORDER_LIST.add(new String[]{"按评论数", "attention"});
            ARTICLE_ORDER_LIST.add(new String[]{"按点赞数", "scores"});

            ARTICLE_CATEGORY_LIST.add(new String[]{"全部分类", "0"});
            ARTICLE_CATEGORY_LIST.add(new String[]{"动画", "2"});
            ARTICLE_CATEGORY_LIST.add(new String[]{"游戏", "1"});
            ARTICLE_CATEGORY_LIST.add(new String[]{"影视", "28"});
            ARTICLE_CATEGORY_LIST.add(new String[]{"生活", "3"});
            ARTICLE_CATEGORY_LIST.add(new String[]{"兴趣", "29"});
            ARTICLE_CATEGORY_LIST.add(new String[]{"轻小说", "16"});
            ARTICLE_CATEGORY_LIST.add(new String[]{"科技", "17"});
        }
    }

    /**
     * 用户搜索结果筛选条件
     */
    public static class SearchResultBiliUserMenuItem {
        public final static List<String[]> BILI_USER_ORDER_LIST = new ArrayList<>();
        public final static List<String[]> BILI_USER_CATEGORY_LIST = new ArrayList<>();

        static {
            // order|order_sort
            BILI_USER_ORDER_LIST.add(new String[]{"默认排序", ""});
            BILI_USER_ORDER_LIST.add(new String[]{"粉丝数由高到底", "fans|1"});
            BILI_USER_ORDER_LIST.add(new String[]{"粉丝数由低到高", "fans|0"});
            BILI_USER_ORDER_LIST.add(new String[]{"Lv等级由高到底", "level|0"});
            BILI_USER_ORDER_LIST.add(new String[]{"Lv等级由低到高", "level|1"});

            // user_type
            BILI_USER_CATEGORY_LIST.add(new String[]{"全部用户", ""});
            BILI_USER_CATEGORY_LIST.add(new String[]{"UP主", "1"});
            BILI_USER_CATEGORY_LIST.add(new String[]{"认证用户", "2"});
            BILI_USER_CATEGORY_LIST.add(new String[]{"普通用户", "3"});
        }
    }
}
