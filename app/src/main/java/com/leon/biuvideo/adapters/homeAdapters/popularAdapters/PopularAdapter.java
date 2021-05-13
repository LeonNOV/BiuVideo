package com.leon.biuvideo.adapters.homeAdapters.popularAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 综合热门/每周必看/入站必刷 适配器
 */
public class PopularAdapter extends BaseAdapter<PopularVideo> implements View.OnClickListener {
    public static final int HOT_VIDEO = 0;
    public static final int WEEKLY = 1;
    public static final int PRECIOUS = 2;

    private final int popularType;
    private final boolean isNav;

    private OnClickFirstItemListener onClickFirstItemListener;

    public PopularAdapter(Context context, int popularType, boolean isNav) {
        super(context);
        this.popularType = popularType;
        this.isNav = isNav;
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
    public int getItemViewType(int position) {
        if (isNav) {
            return position == 0 ? R.layout.popular_video_first_item : R.layout.popular_video_item;
        } else {
            return R.layout.popular_video_item;
        }
    }

    @Override
    public int getLayout(int viewType) {
        return viewType;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (isNav) {
            if (position == 0) {
                initFirstItem(holder);
            } else {
                initVideoItem(holder, getAllData().get(position - 1));
            }
        } else {
            initVideoItem(holder, getAllData().get(position));
        }
    }

    private void initFirstItem(BaseViewHolder holder) {
        holder
                .setOnClickListener(R.id.popular_top_list, this)
                .setOnClickListener(R.id.popular_weekly, this)
                .setOnClickListener(R.id.popular_precious, this);
    }

    private void initVideoItem(BaseViewHolder holder, PopularVideo popularVideo) {
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
                        startPublicFragment(FragmentType.VIDEO, popularVideo.bvid);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (InternetUtils.checkNetwork(v)) {
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
}
