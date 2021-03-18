package com.leon.biuvideo.ui.home.orderFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.OrderParser;
import com.leon.biuvideo.values.OrderType;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 订阅页面-番剧订阅
 */
public class OrderBangumiFragment extends BaseSupportFragment {
    private SmartRefreshRecyclerView<RvTestBean> orderBangumiSmartRefreshRecyclerView;
    private OrderParser orderParser;
    private List<Order> orderList;

    @Override
    protected int setLayout() {
        return R.layout.order_bangumi_fragment;
    }

    @Override
    protected void initView() {
        orderBangumiSmartRefreshRecyclerView = findView(R.id.order_bangumi_smartRefreshRecyclerView);
        orderBangumiSmartRefreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
//                RefreshState state = refreshLayout.getState();
                Random random = new Random();

                List<RvTestBean> rvTestBeanList = new ArrayList<>();
                RvTestBean rvTestBean = new RvTestBean();
                rvTestBean.title = "Title" + random.nextInt(100);
                rvTestBean.view = (random.nextInt(5000) + 5000);
                rvTestBeanList.add(rvTestBean);

                orderBangumiSmartRefreshRecyclerView.append(rvTestBeanList);

                // 显示加载成功
                refreshLayout.finishLoadMore(true);

                // 标记没有更多的数据
//                refreshLayout.finishLoadMoreWithNoMoreData();

                //结束加载更多动画
                orderBangumiSmartRefreshRecyclerView.finishLoadMore();

                // 全部加载完后使用该方法
//                orderBangumiSmartRefreshRecyclerView.setEnablePureScrollMode(true);
            }
        });

        orderParser = new OrderParser(OrderType.BANGUMI);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                if (msg.what == 0) {
                    // 设置初始数据
//                    orderBangumiSmartRefreshRecyclerView.setRecyclerViewAdapter();
                    orderBangumiSmartRefreshRecyclerView.setStatus(LoadingRecyclerView.LOADING_FINISH);
                }
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        // 加载初始数据
//        SimpleSingleThreadPool.executor(new Runnable() {
//            @Override
//            public void run() {
//                orderList = orderParser.parseOrder();
//
//                Message message = receiveDataHandler.obtainMessage();
//                message.what = 0;
//                receiveDataHandler.sendMessage(message);
//            }
//        });

        orderBangumiSmartRefreshRecyclerView.setStatus(LoadingRecyclerView.LOADING);
    }
}
