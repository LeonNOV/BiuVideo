package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.userBeans.Order;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.OrderType;

import java.util.Arrays;
import java.util.List;

public class OrderAdapter extends BaseAdapter<Order> {
    private final List<Order> orders;
    private final OrderType orderType;

    public OrderAdapter(List<Order> orders, Context context, OrderType orderType) {
        super(orders, context);
        this.orders = orders;
        this.orderType = orderType;
    }

    @Override
    public int getLayout(int viewType) {
        switch (orderType) {
            case VIDEO:
                return R.layout.play_list_video_item;
            case BANGUMI:
            case SERIES:
                return R.layout.order_bangumi_item;
            case ARTICLE:
                return R.layout.user_article_list_view_item;
            default:
                return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Order order = orders.get(position);

        int backgroundTint = R.color.bilibili_pink;
        switch (order.badgeType) {
            case "独家":
            case "出品":
                backgroundTint = R.color.blue;
                break;
            default:
                break;
        }

        if (order.badgeType.equals("")) {
            holder.setVisibility(R.id.order_item_textView_badge, View.GONE);
        } else {
            holder.findById(R.id.order_item_textView_badge).getBackground().setTint(backgroundTint);
        }

        if (order.seasonTitle.equals("")) {
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
                .setText(R.id.order_item_textView_progress, order.progress.equals("") ? "尚未观看" : order.progress)
                .setText(R.id.order_item_textView_total, order.total);
    }
}
