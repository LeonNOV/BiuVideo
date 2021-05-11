package com.leon.biuvideo.ui.home.orderFragments;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.adapters.homeAdapters.OrderDataAdapter;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.OrderParser;
import com.leon.biuvideo.values.OrderType;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 订阅页面-剧集订阅
 */
public class OrderDataFragment extends BaseSupportFragmentWithSrr<Order> {
    private final OrderType orderType;
    private final List<Order> orderList = new ArrayList<>();
    private DataLoader<Order> orderDataLoader;

    public OrderDataFragment(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    protected void onLazyLoad() {
        view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        orderDataLoader.insertData(true);
    }

    @Override
    protected void initView() {
        OrderDataAdapter orderDataAdapter = new OrderDataAdapter(orderList, context);
        orderDataAdapter.setHasStableIds(true);
        view.setRecyclerViewAdapter(orderDataAdapter);
        view.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                orderDataLoader.insertData(false);
            }
        });

        orderDataLoader = new DataLoader<>(new OrderParser(orderType), view, orderDataAdapter, this);
    }
}
