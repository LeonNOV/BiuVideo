package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiSection;
import com.leon.biuvideo.ui.MainActivity;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc PV、花絮等系列的父容器适配器
 */
public class BangumiSectionContainerAdapter extends BaseAdapter<BangumiSection> {
    private final List<BangumiSection> bangumiSectionList;
    private final MainActivity mainActivity;

    public BangumiSectionContainerAdapter(List<BangumiSection> beans, MainActivity mainActivity, Context context) {
        super(beans, context);
        this.bangumiSectionList = beans;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.bangumi_section_container_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BangumiSection bangumiSection = bangumiSectionList.get(position);

        RecyclerView bangumiSectionContainerItemData = holder
                .setText(R.id.bangumi_section_container_item_title, bangumiSection.title)
                .findById(R.id.bangumi_section_container_item_data);

        BangumiSectionItemAdapter bangumiSectionItemAdapter = new BangumiSectionItemAdapter(bangumiSection.episodeList, mainActivity, context);
        bangumiSectionItemAdapter.setHasStableIds(true);
        bangumiSectionContainerItemData.setAdapter(bangumiSectionItemAdapter);
    }
}
