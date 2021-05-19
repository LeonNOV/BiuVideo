package com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters.searchResultBnagumiAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBangumi;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.values.FragmentType;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/31
 * @Desc 番剧搜索结果选集适配器
 */
public class SearchResultBangumiEpAdapter extends BaseAdapter<SearchResultBangumi.SearchResultBangumiEp> {
    private final String seasonId;
    private final List<SearchResultBangumi.SearchResultBangumiEp> searchResultBangumiEpList;
    private final MainActivity activity;

    public SearchResultBangumiEpAdapter(String seasonId, List<SearchResultBangumi.SearchResultBangumiEp> beans, MainActivity activity, Context context) {
        super(beans, context);

        this.seasonId = seasonId;
        this.searchResultBangumiEpList = beans;
        this.activity = activity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_bangumi_ep_item;
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
                if (InternetUtils.checkNetwork(v)) {
                    if (searchResultBangumiEp.isVip) {
                        if (PreferenceUtils.getVipStatus()) {
                            startPublicFragment(activity, FragmentType.BANGUMI, seasonId, String.valueOf(position));
                        } else {
                            SimpleSnackBar.make(v, "该选集需要大会员才能观看~", SimpleSnackBar.LENGTH_SHORT).show();
                        }
                    } else {
                        startPublicFragment(activity, FragmentType.BANGUMI, seasonId, String.valueOf(position));
                    }
                }
            }
        });
    }
}
