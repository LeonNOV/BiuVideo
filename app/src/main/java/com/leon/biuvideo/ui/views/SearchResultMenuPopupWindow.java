package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 搜索界面筛选条件弹窗
 */
public class SearchResultMenuPopupWindow extends PopupWindow {
    private final Map<String, String> menuItemMap;
    private final Context context;
    private FrameLayout frameLayout;
    private RecyclerView recyclerView;

    public SearchResultMenuPopupWindow (Context context, Map<String, String> menuItemMap) {
        this.context = context;
        this.menuItemMap = menuItemMap;

        initView();
    }

    private void initView() {
        addParent();
        addRecyclerView();
    }

    private void addRecyclerView() {
        recyclerView = new RecyclerView(context);
        frameLayout.addView(recyclerView);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        recyclerView.setLayoutParams(layoutParams);
    }

    private void addParent() {
        frameLayout = new FrameLayout(context);

        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        frameLayout.setLayoutParams(layoutParams);
    }
}
