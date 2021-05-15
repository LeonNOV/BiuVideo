package com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters.searchResultBnagumiAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBangumi;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

/**
 * @Author Leon
 * @Time 2021/3/31
 * @Desc 番剧搜索结果适配器
 */
public class SearchResultBangumiAdapter extends BaseAdapter<SearchResultBangumi> {
    private final MainActivity mainActivity;

    public SearchResultBangumiAdapter(MainActivity mainActivity, Context context) {
        super(context);
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_bangumi_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SearchResultBangumi searchResultBangumi = getAllData().get(position);

        holder
                .setImage(R.id.search_result_item_bangumi_cover, searchResultBangumi.cover, ImagePixelSize.COVER)
                .setText(R.id.search_result_item_bangumi_title, searchResultBangumi.title)
                .setText(R.id.search_result_item_bangumi_pubTime, searchResultBangumi.pubTime)
                .setText(R.id.search_result_item_bangumi_areas, searchResultBangumi.areas)
                .setText(R.id.search_result_item_bangumi_style, searchResultBangumi.styles)
                .setText(R.id.search_result_item_bangumi_score, searchResultBangumi.score)
                .setText(R.id.search_result_item_bangumi_reviews, searchResultBangumi.userCount)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPublicFragment(mainActivity, FragmentType.VIDEO, searchResultBangumi.seasonId);
                    }
                });

        TextView searchResultItemBangumiAngleTitle = holder.findById(R.id.search_result_item_bangumi_angleTitle);

        if (searchResultBangumi.badge == null) {
            searchResultItemBangumiAngleTitle.setVisibility(View.GONE);
        } else {
            searchResultItemBangumiAngleTitle.setVisibility(View.GONE);
            searchResultItemBangumiAngleTitle.setText(searchResultBangumi.badge);
        }

        RecyclerView searchResultItemBangumiEps = holder.findById(R.id.search_result_item_bangumi_eps);

        SearchResultBangumiEpAdapter searchResultBangumiEpAdapter = new SearchResultBangumiEpAdapter(searchResultBangumi.searchResultBangumiEpList, context);
        searchResultBangumiEpAdapter.setHasStableIds(true);
        searchResultItemBangumiEps.setAdapter(searchResultBangumiEpAdapter);
    }
}
