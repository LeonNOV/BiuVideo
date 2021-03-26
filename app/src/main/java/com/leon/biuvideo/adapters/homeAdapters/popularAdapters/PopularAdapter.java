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
public class PopularAdapter extends BaseAdapter<PopularVideo> implements View.OnClickListener {
    public static final int HOT_VIDEO = 0;
    public static final int WEEKLY = 1;
    public static final int PRECIOUS = 2;

    private final List<PopularVideo> popularVideoList;
    private final int popularType;

    private OnClickFirstItemListener onClickFirstItemListener;

    public PopularAdapter(List<PopularVideo> beans, Context context, int popularType) {
        super(beans, context);
        this.popularVideoList = beans;
        this.popularType = popularType;
    }

    public interface OnClickFirstItemListener {
        /**
         * 排行榜
         */
        void onClickTopList();

        /**
         * 每周必看
         */
        void onClickWeekly();

        /**
         * 入站必刷
         */
        void onClickPrecious();
    }

    public void setOnClickFirstItemListener(OnClickFirstItemListener onClickFirstItemListener) {
        this.onClickFirstItemListener = onClickFirstItemListener;
    }

    @Override
    public int getLayout(int viewType) {
        return viewType == 0 ? R.layout.popular_video_first_item : R.layout.popular_video_item;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        if (position == 0) {
            initFirstItem(holder);
        } else {
            initVideoItem(holder, popularVideoList.get(position - 1), position);
        }
    }

    private void initFirstItem(BaseViewHolder holder) {
        holder
                .setOnClickListener(R.id.popular_top_list, this)
                .setOnClickListener(R.id.popular_weekly, this)
                .setOnClickListener(R.id.popular_precious, this);
    }

    private void initVideoItem(BaseViewHolder holder, PopularVideo popularVideo, int position) {
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
                .setImage(R.id.popular_video_item_cover, popularVideo.pic, ImagePixelSize.COVER)
                .setText(R.id.popular_video_item_duration, popularVideo.duration)
                .setText(R.id.popular_video_item_title, popularVideo.title)
                .setText(R.id.popular_video_item_view, ValueUtils.generateCN(popularVideo.view))
                .setText(R.id.popular_video_item_danmaku, ValueUtils.generateCN(popularVideo.danmaku))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(v, "点击了第" + position + "个item", SimpleSnackBar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popular_top_list:
                if (onClickFirstItemListener != null) {
                    onClickFirstItemListener.onClickTopList();
                }
                break;
            case R.id.popular_weekly:
                if (onClickFirstItemListener != null) {
                    onClickFirstItemListener.onClickWeekly();
                }
                break;
            case R.id.popular_precious:
                if (onClickFirstItemListener != null) {
                    onClickFirstItemListener.onClickPrecious();
                }
                break;
            default:
                break;
        }
    }
}
