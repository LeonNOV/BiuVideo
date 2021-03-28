package com.leon.biuvideo.adapters.homeAdapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.userBeans.History;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/5
 * @Desc 历史记录适配器
 */
public class HistoryAdapter extends BaseAdapter<History> {
    private final List<History> historyList;

    public HistoryAdapter(List<History> beans, Context context) {
        super(beans, context);
        this.historyList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        switch (viewType) {
            case 0:
                return R.layout.history_item_video;
            case 1:
                return R.layout.history_item_bangumi;
            case 2:
                return R.layout.history_item_article;
            default:
                return R.layout.history_item_series;
        }
    }

    @Override
    public int getItemViewType(int position) {
        History history = historyList.get(position);
        switch (history.historyType) {
            case VIDEO:
                return 0;
            case BANGUMI:
                return 1;
            case ARTICLE:
                return 2;
            default:
                return 3;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        History history = historyList.get(position);

        switch (history.historyType) {
            case VIDEO:
                initVideoItemView(holder, history,  position);
                break;
            case BANGUMI:
                initBangumiItemView(holder, history,  position);
                break;
            case ARTICLE:
                initArticleItemView(holder, history,  position);
                break;
            default:
                initSeriesItemView(holder, history,  position);
                break;
        }
    }

    /**
     * 初始化video条目
     * @param holder    holder
     * @param history   history
     * @param position  position
     */
    private void initVideoItemView(BaseViewHolder holder, History history, int position) {
    }

    /**
     * 初始化bangumi条目
     * @param holder    holder
     * @param history   history
     * @param position  position
     */
    private void initBangumiItemView(BaseViewHolder holder, History history, int position) {
    }

    /**
     * 初始化article条目
     * @param holder    holder
     * @param history   history
     * @param position  position
     */
    private void initArticleItemView(BaseViewHolder holder, History history, int position) {
    }

    /**
     * 初始化series条目
     * @param holder    holder
     * @param history   history
     * @param position  position
     */
    private void initSeriesItemView(BaseViewHolder holder, History history, int position) {
    }
}
