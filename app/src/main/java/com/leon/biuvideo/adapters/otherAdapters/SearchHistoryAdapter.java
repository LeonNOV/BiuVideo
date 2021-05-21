package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.greendao.dao.SearchHistory;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 搜索历史数据适配器
 */
public class SearchHistoryAdapter extends BaseAdapter<SearchHistory> {
    private final List<SearchHistory> searchHistoryList;

    private OnSearchHistoryListener onSearchHistoryListener;

    public SearchHistoryAdapter(List<SearchHistory> beans, Context context) {
        super(beans, context);

        this.searchHistoryList = beans;
    }

    public interface OnSearchHistoryListener {
        /**
         * 删除搜索历史
         *
         * @param searchHistory 被删除的搜索历史
         */
        void onDelete(SearchHistory searchHistory);

        /**
         * 点击搜索历史
         *
         * @param keyword 关键字
         */
        void onClick(String keyword);
    }

    public void setOnSearchHistoryListener(OnSearchHistoryListener onSearchHistoryListener) {
        this.onSearchHistoryListener = onSearchHistoryListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.history_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SearchHistory searchHistory = searchHistoryList.get(position);

        holder
                .setText(R.id.history_item_keyword, searchHistory.getKeyword())
                .setOnClickListener(R.id.history_item_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSearchHistoryListener != null) {
                            onSearchHistoryListener.onDelete(searchHistoryList.get(position));
                        }
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSearchHistoryListener != null) {
                            onSearchHistoryListener.onClick(searchHistory.getKeyword());
                        }
                    }
                });
    }
}
