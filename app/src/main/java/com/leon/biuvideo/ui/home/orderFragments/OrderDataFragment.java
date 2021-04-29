package com.leon.biuvideo.ui.home.orderFragments;

import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.adapters.homeAdapters.OrderDataAdapter;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
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
    private OrderParser orderParser;

    public OrderDataFragment(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    protected void onLazyLoad() {
        view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        // 加载初始数据
        getOrderData(0);
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
                getOrderData(1);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<Order> orders = (List<Order>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (orders != null && orders.size() > 0) {
                            orderDataAdapter.append(orders);
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!orderParser.dataStatus) {
                                view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        } else {
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }

                        break;
                    case 1:
                        if (orders != null && orders.size() > 0) {
                            orderDataAdapter.append(orders);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                            if (!orderParser.dataStatus) {
                                view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        } else {
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getOrderData(int what) {
        if (orderParser == null) {
            orderParser = new OrderParser(orderType);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<Order> orderList = orderParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = orderList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
