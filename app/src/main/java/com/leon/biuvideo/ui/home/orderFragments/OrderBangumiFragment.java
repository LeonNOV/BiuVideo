package com.leon.biuvideo.ui.home.orderFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.adapters.homeAdapters.OrderAdapter;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.home.OrderFragment;
import com.leon.biuvideo.ui.otherFragments.BangumiFragment;
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
 * @Desc 订阅页面-番剧订阅
 */
public class OrderBangumiFragment extends BaseSupportFragmentWithSrr<Order> {
    private final List<Order> orderList = new ArrayList<>();
    private OrderParser orderParser;

    @Override
    protected void initView() {
        OrderAdapter orderAdapter = new OrderAdapter(orderList, context, OrderType.BANGUMI);
        orderAdapter.setOnClickMediaListener(new OrderAdapter.OnClickMediaListener() {
            @Override
            public void onClick(String mediaId) {
                ((OrderFragment) getParentFragment()).start(new BangumiFragment(mediaId));
            }
        });
        orderAdapter.setHasStableIds(true);
        view.setRecyclerViewAdapter(orderAdapter);
        view.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getOrderBangumis(1);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<Order> orders = (List<Order>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (orders.size() == 0) {
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            orderAdapter.append(orders);
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!orderParser.dataStatus) {
                                view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }
                        break;
                    case 1:
                        if (orders.size() > 0) {
                            orderAdapter.append(orders);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
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

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        // 加载初始数据
        getOrderBangumis(0);
    }

    private void getOrderBangumis(int what) {
        if (orderParser == null) {
            orderParser = new OrderParser(OrderType.BANGUMI);
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
