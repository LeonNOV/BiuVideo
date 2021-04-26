package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiSeason;
import com.leon.biuvideo.ui.resourcesFragment.video.OnBottomSheetWithItemListener;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/26
 * @Desc 番剧系列适配器
 */
public class BangumiSeriesAdapter extends BaseAdapter<BangumiSeason> {
    private final int currentPosition;
    private final List<BangumiSeason> bangumiSeasonList;

    private OnBottomSheetWithItemListener onBottomSheetWithItemListener;

    public BangumiSeriesAdapter(List<BangumiSeason> beans, Context context, int currentPosition) {
        super(beans, context);
        this.currentPosition = currentPosition;
        this.bangumiSeasonList = beans;
    }

    public void setOnBottomSheetWithItemListener(OnBottomSheetWithItemListener onBottomSheetWithItemListener) {
        this.onBottomSheetWithItemListener = onBottomSheetWithItemListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.bangumi_series_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BangumiSeason bangumiSeason = bangumiSeasonList.get(position);

        TextView textView = holder.findById(R.id.bangumi_series_item_content);
        textView.setText(bangumiSeason.seasonTitle);
        textView.setSelected(currentPosition == position);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == position) {
                    return;
                }

                if (onBottomSheetWithItemListener != null) {
                    onBottomSheetWithItemListener.onItem(position);
                }
            }
        });
    }
}
