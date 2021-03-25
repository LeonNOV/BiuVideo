package com.leon.biuvideo.adapters.homeAdapters.popularAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularTopList;
import com.leon.biuvideo.ui.views.TagView;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/25
 * @Desc 排行榜 同UP主上榜视频适配器
 */
public class PopularTopListExpandAdapter extends BaseAdapter<PopularTopList.OtherVideo> {
    private final List<PopularTopList.OtherVideo> otherVideoList;

    public PopularTopListExpandAdapter(List<PopularTopList.OtherVideo> beans, Context context) {
        super(beans, context);

        this.otherVideoList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.popular_top_list_expand_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        PopularTopList.OtherVideo otherVideo = otherVideoList.get(position);

        holder
                .setText(R.id.popular_top_list_expand_item_title, otherVideo.title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

        ((TagView)holder.findById(R.id.popular_top_list_expand_item_score)).setRightValue(otherVideo.score);
    }
}
