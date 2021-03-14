package com.leon.biuvideo.adapters.discover;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.discoverBeans.HotSearch;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/14
 * @Desc 热搜列表适配器
 */
public class DiscoverHotSearchAdapter extends BaseAdapter<HotSearch> {
    private static final int TOP_RANGE = 3;
    private final List<HotSearch> hotSearchList;
    private final Context context;

    public DiscoverHotSearchAdapter(List<HotSearch> beans, Context context) {
        super(beans, context);

        this.hotSearchList = beans;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.discover_hot_search_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        HotSearch hotSearch = hotSearchList.get(position);

        if (position < TOP_RANGE) {
            TextView hotSearchItemPosition = holder.findById(R.id.hot_search_item_position);
            hotSearchItemPosition.setText(String.valueOf(hotSearch.position));
            hotSearchItemPosition.setTypeface(Typeface.DEFAULT_BOLD);
            hotSearchItemPosition.setTextColor(context.getColor(R.color.BiliBili_pink));
            hotSearchItemPosition.setTextSize(23);
        } else {
            holder.setText(R.id.hot_search_item_position, String.valueOf(hotSearch.position));
        }

        if (hotSearch.icon != null) {
            holder.setImage(R.id.hot_search_item_icon, hotSearch.icon, null);
        } else {
            holder.setVisibility(R.id.hot_search_item_icon, View.GONE);
        }

        holder
                .setText(R.id.hot_search_item_name, hotSearch.showName)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Position：" + position + "----Keyword：" + hotSearch.keyword, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
