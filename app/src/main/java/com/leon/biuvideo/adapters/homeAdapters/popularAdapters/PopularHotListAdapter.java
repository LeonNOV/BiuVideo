package com.leon.biuvideo.adapters.homeAdapters.popularAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.popularBeans.HotVideo;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 综合热门适配器
 */
public class PopularHotListAdapter extends BaseAdapter<HotVideo> {
    private final List<HotVideo> hotVideoList;

    public PopularHotListAdapter(List<HotVideo> beans, Context context) {
        super(beans, context);
        this.hotVideoList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.popular_hot_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        HotVideo hotVideo = hotVideoList.get(position);

        if (hotVideo.reason != null) {
            holder.setVisibility(R.id.popular_hot_list_item_reason, View.VISIBLE);
            holder.setText(R.id.popular_hot_list_item_reason, hotVideo.reason);
        } else {
            holder.setVisibility(R.id.popular_hot_list_item_reason, View.GONE);
        }

        holder
                .setImage(R.id.popular_hot_list_item_cover, hotVideo.pic, ImagePixelSize.COVER)
                .setText(R.id.popular_hot_list_item_duration, hotVideo.duration)
                .setText(R.id.popular_hot_list_item_desc, hotVideo.title)
                .setText(R.id.popular_hot_list_item_view, ValueUtils.generateCN(hotVideo.view))
                .setText(R.id.popular_hot_list_item_danmaku, ValueUtils.generateCN(hotVideo.danmaku))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(v, "点击了第" + position + "个item", SimpleSnackBar.LENGTH_SHORT).show();
                    }
                });
    }
}
