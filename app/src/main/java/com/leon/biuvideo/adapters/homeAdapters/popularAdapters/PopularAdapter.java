package com.leon.biuvideo.adapters.homeAdapters.popularAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 综合热门/每周必看/入站必刷 适配器
 */
public class PopularAdapter extends BaseAdapter<PopularVideo> {
    public static final int HOT_VIDEO = 0;
    public static final int WEEKLY = 1;
    public static final int PRECIOUS = 2;

    private final List<PopularVideo> popularVideoList;
    private final int popularType;

    public PopularAdapter(List<PopularVideo> beans, Context context, int popularType) {
        super(beans, context);
        this.popularVideoList = beans;
        this.popularType = popularType;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.popular_hot_list_and_weekly_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        PopularVideo popularVideo = popularVideoList.get(position);

        TextView popularHotListItemReason = holder.findById(R.id.popular_hot_list_item_reason);
        TextView popularWeeklyItemReason = holder.findById(R.id.popular_weekly_item_reason);
        TextView popularPreciousItemReason = holder.findById(R.id.popular_precious_item_reason);

        if (popularVideo.reason != null) {
            switch (popularType) {
                case HOT_VIDEO:
                    popularHotListItemReason.setVisibility(View.VISIBLE);
                    popularHotListItemReason.setText(popularVideo.reason);
                    break;
                case WEEKLY:
                    popularWeeklyItemReason.setVisibility(View.VISIBLE);
                    popularWeeklyItemReason.setText(popularVideo.reason);
                    break;
                case PRECIOUS:
                    popularPreciousItemReason.setVisibility(View.VISIBLE);
                    popularPreciousItemReason.setText(popularVideo.reason);
                    break;
                default:
                    break;
            }
        } else {
            popularHotListItemReason.setVisibility(View.GONE);
            popularWeeklyItemReason.setVisibility(View.GONE);
            popularPreciousItemReason.setVisibility(View.GONE);
        }

        holder
                .setImage(R.id.popular_hot_list_and_weekly_item_cover, popularVideo.pic, ImagePixelSize.COVER)
                .setText(R.id.popular_hot_list_and_weekly_item_duration, popularVideo.duration)
                .setText(R.id.popular_hot_list_and_weekly_item_title, popularVideo.title)
                .setText(R.id.popular_hot_list_and_weekly_item_view, ValueUtils.generateCN(popularVideo.view))
                .setText(R.id.popular_hot_list_and_weekly_item_danmaku, ValueUtils.generateCN(popularVideo.danmaku))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(v, "点击了第" + position + "个item", SimpleSnackBar.LENGTH_SHORT).show();
                    }
                });
    }
}
