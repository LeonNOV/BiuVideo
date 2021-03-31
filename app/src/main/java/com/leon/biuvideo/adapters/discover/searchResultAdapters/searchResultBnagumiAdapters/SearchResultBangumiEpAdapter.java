package com.leon.biuvideo.adapters.discover.searchResultAdapters.searchResultBnagumiAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBangumi;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/31
 * @Desc 番剧搜索结果选集适配器
 */
public class SearchResultBangumiEpAdapter extends BaseAdapter<SearchResultBangumi.SearchResultBangumiEp> {
    private final List<SearchResultBangumi.SearchResultBangumiEp> searchResultBangumiEpList;

    public SearchResultBangumiEpAdapter(List<SearchResultBangumi.SearchResultBangumiEp> beans, Context context) {
        super(beans, context);

        this.searchResultBangumiEpList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_item_bangumi_ep;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SearchResultBangumi.SearchResultBangumiEp searchResultBangumiEp = searchResultBangumiEpList.get(position);

        holder.setText(R.id.search_result_item_bangumi_ep_title, searchResultBangumiEp.title);
        TextView searchResultItemBangumiEpBadge = holder.findById(R.id.search_result_item_bangumi_ep_badge);

        if (searchResultBangumiEp.badge == null) {
            searchResultItemBangumiEpBadge.setVisibility(View.GONE);
        } else {
            searchResultItemBangumiEpBadge.setVisibility(View.VISIBLE);
            searchResultItemBangumiEpBadge.setText(searchResultBangumiEp.badge);
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
