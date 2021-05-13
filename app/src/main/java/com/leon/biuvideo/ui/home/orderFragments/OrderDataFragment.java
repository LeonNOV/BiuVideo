package com.leon.biuvideo.ui.home.orderFragments;

import com.leon.biuvideo.adapters.homeAdapters.OrderDataAdapter;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.OrderParser;
import com.leon.biuvideo.values.OrderType;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 订阅页面-剧集订阅
 */
public class OrderDataFragment extends BaseSupportFragmentWithSrr<Order> {
    private final OrderType orderType;
    private DataLoader<Order> orderDataLoader;

    public OrderDataFragment(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    protected void onLazyLoad() {
        orderDataLoader.insertData(true);
    }

    @Override
    protected void initView() {
        orderDataLoader = new DataLoader<>(context, new OrderParser(orderType),
                new OrderDataAdapter(context), this);
    }
}
