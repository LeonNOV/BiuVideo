package com.leon.biuvideo.adapters.userOrderAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.ui.activitys.BangumiActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiParser;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.OrderType;
import com.leon.biuvideo.values.SortType;

import java.util.Arrays;
import java.util.List;

public class UserOrderBaseAdapter extends BaseAdapter<Order> {
    private final Context context;
    private final List<Order> orders;
    private final OrderType orderType;

    public UserOrderBaseAdapter(List<Order> orders, Context context, OrderType orderType) {
        super(orders, context);
        this.context = context;
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

        if (order.badgeType.equals("")) {
            holder.setVisibility(R.id.order_item_textView_badge, View.GONE);
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
                .setText(R.id.order_item_textView_total, order.total)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!InternetUtils.checkNetwork(context)) {
                            SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        if (orderType == OrderType.SERIES) {
                            SimpleSnackBar.make(v, "追剧功能还未进行开发，请谅解(●'◡'●)", SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        // 如果为番剧则跳转到BangumiActivity
                        if (orderType == OrderType.BANGUMI) {
                            int pageNum = 1;
                            Bangumi targetBangumi = null;

                            BangumiParser bangumiParser = new BangumiParser(context);
                            List<Bangumi> bangumiList;

                            while (targetBangumi == null) {
                                bangumiList = bangumiParser.bangumiParse(order.title, pageNum, SortType.DEFAULT);
                                pageNum++;
                                for (Bangumi bangumi : bangumiList) {
                                    if (bangumi.title.equals(order.title)) {
                                        targetBangumi = bangumi;
                                        break;
                                    }
                                }
                            }

                            if (targetBangumi != null) {
                                Intent intent = new Intent(context, BangumiActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("bangumi", targetBangumi);
                                intent.putExtras(bundle);

                                context.startActivity(intent);
                            }
                        }
                    }
                });
    }
}
