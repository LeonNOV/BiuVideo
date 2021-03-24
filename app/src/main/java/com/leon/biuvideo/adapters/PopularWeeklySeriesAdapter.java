package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.PopularWeeklySeries;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/24
 * @Desc 每周必看目录适配器
 */
public class PopularWeeklySeriesAdapter extends BaseAdapter<PopularWeeklySeries> {
    private final List<PopularWeeklySeries> popularWeeklySeriesList;
    private final int selectedPosition;
    private OnSeriesChangedListener onSeriesChangedListener;

    public PopularWeeklySeriesAdapter(List<PopularWeeklySeries> beans, int selectedPosition, Context context) {
        super(beans, context);

        this.popularWeeklySeriesList = beans;
        this.selectedPosition = selectedPosition;
    }

    public interface OnSeriesChangedListener {
        /**
         * 点击了每周必看的某一期后的回调方法
         *
         * @param popularWeeklySeries   popularWeeklySeries
         * @param position  position
         */
        void onChanged(PopularWeeklySeries popularWeeklySeries, int position);
    }

    public void setOnSeriesChangedListener(OnSeriesChangedListener onSeriesChangedListener) {
        this.onSeriesChangedListener = onSeriesChangedListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.popular_weekly_before_series_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        PopularWeeklySeries popularWeeklySeries = popularWeeklySeriesList.get(position);

        if (selectedPosition == position) {
            holder.getItemView().setBackground(context.getDrawable(R.drawable.ic_selected));
        } else {
            holder.getItemView().setBackground(context.getDrawable(R.drawable.ripple_round_corners6dp_bg));
        }

        holder
                .setText(R.id.popular_weekly_before_series_item_name, popularWeeklySeries.name)
                .setText(R.id.popular_weekly_before_series_item_subject, popularWeeklySeries.subject)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSeriesChangedListener != null && position != selectedPosition) {
                            onSeriesChangedListener.onChanged(popularWeeklySeries, position);
                        }
                    }
                });
    }
}
