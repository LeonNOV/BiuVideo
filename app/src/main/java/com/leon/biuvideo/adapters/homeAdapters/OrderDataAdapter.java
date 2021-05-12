package com.leon.biuvideo.adapters.homeAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.Arrays;

/**
 * @Author Leon
 * @Time 2020/12/14
 * @Desc 订阅数据适配器
 */
public class OrderDataAdapter extends BaseAdapter<Order> {
    public OrderDataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.order_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Order order = getAllData().get(position);

        if ("".equals(order.badgeType)) {
            holder.setVisibility(R.id.order_item_textView_badge, View.GONE);
        }

        if ("".equals(order.seasonTitle)) {
            holder.setVisibility(R.id.order_item_textView_subtitle, View.GONE);
        } else {
            holder.setText(R.id.order_item_textView_subtitle, order.seasonTitle);
        }

        holder
                .setText(R.id.order_item_textView_badge, order.badgeType)
                .setImage(R.id.order_item_imageView_cover, order.cover, ImagePixelSize.COVER)
                .setText(R.id.order_item_textView_title, order.title)
                .setText(R.id.order_item_textView_desc, order.desc)
                .setText(R.id.order_item_textView_type, order.seasonType)
                .setText(R.id.order_item_textView_area, Arrays.toString(order.areas))
                .setText(R.id.order_item_textView_progress, "".equals(order.progress) ? "尚未观看" : order.progress)
                .setText(R.id.order_item_textView_total, order.total)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPublicFragment(FragmentType.BANGUMI, String.valueOf(order.seasonId));
                    }
                });
    }
}
