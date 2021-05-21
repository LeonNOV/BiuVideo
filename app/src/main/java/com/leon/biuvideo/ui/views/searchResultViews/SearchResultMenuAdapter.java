package com.leon.biuvideo.ui.views.searchResultViews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/30
 * @Desc 视频搜索结果菜单适配器
 */
public class SearchResultMenuAdapter extends BaseAdapter<String[]> {
    private final List<String[]> stringsList;
    private final int selectedPosition;

    private OnSearchResultMenuItemListener onSearchResultMenuItemListener;

    public SearchResultMenuAdapter(List<String[]> beans, int selectedPosition, Context context) {
        super(beans, context);

        this.stringsList = beans;
        this.selectedPosition = selectedPosition;
    }

    public interface OnSearchResultMenuItemListener {
        /**
         * 点击筛选条件
         *
         * @param values    [筛选名称,筛选条件参数]
         * @param position  position
         */
        void onClickMenuItem(String[] values, int position);
    }

    public void setOnSearchResultMenuItemListener(OnSearchResultMenuItemListener onSearchResultMenuItemListener) {
        this.onSearchResultMenuItemListener = onSearchResultMenuItemListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_menu_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        String[] strings = stringsList.get(position);

        TextView searchResultMenuItemContent = holder.findById(R.id.search_result_menu_item_content);
        searchResultMenuItemContent.setText(strings[0]);
        searchResultMenuItemContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedPosition == position) {
                            return;
                        }

                        if (onSearchResultMenuItemListener != null) {
                            onSearchResultMenuItemListener.onClickMenuItem(strings, position);
                        }
                    }
                });

        if (selectedPosition == position) {
            searchResultMenuItemContent.setSelected(true);
            searchResultMenuItemContent.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }
}
