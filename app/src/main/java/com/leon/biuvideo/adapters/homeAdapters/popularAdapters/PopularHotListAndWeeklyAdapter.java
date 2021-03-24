package com.leon.biuvideo.adapters.homeAdapters.popularAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

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
 * @Desc 综合热门/每周必看适配器
 */
public class PopularHotListAndWeeklyAdapter extends BaseAdapter<HotVideo> {
    private final List<HotVideo> hotVideoList;
    private final boolean isHotList;

    public PopularHotListAndWeeklyAdapter(List<HotVideo> beans, Context context, boolean isHotList) {
        super(beans, context);
        this.hotVideoList = beans;
        this.isHotList = isHotList;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.popular_hot_list_and_weekly_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        HotVideo hotVideo = hotVideoList.get(position);

        TextView popularHotListItemReason = holder.findById(R.id.popular_hot_list_item_reason);
        TextView popularWeeklyItemReason = holder.findById(R.id.popular_weekly_item_reason);

        if (hotVideo.reason != null) {
            if (isHotList) {
                popularHotListItemReason.setVisibility(View.VISIBLE);
                popularHotListItemReason.setText(hotVideo.reason);
            } else {
                popularWeeklyItemReason.setVisibility(View.VISIBLE);
                popularWeeklyItemReason.setText(hotVideo.reason);
            }
        } else {
            popularHotListItemReason.setVisibility(View.GONE);
            popularWeeklyItemReason.setVisibility(View.GONE);
        }

        holder
                .setImage(R.id.popular_hot_list_and_weekly_item_cover, hotVideo.pic, ImagePixelSize.COVER)
                .setText(R.id.popular_hot_list_and_weekly_item_duration, hotVideo.duration)
                .setText(R.id.popular_hot_list_and_weekly_item_title, hotVideo.title)
                .setText(R.id.popular_hot_list_and_weekly_item_view, ValueUtils.generateCN(hotVideo.view))
                .setText(R.id.popular_hot_list_and_weekly_item_danmaku, ValueUtils.generateCN(hotVideo.danmaku))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(v, "点击了第" + position + "个item", SimpleSnackBar.LENGTH_SHORT).show();
                    }
                });
    }
}
