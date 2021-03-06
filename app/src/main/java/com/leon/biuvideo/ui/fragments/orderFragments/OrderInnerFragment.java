package com.leon.biuvideo.ui.fragments.orderFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userOrderAdapters.UserOrderBaseAdapter;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.ui.AbstractSimpleLoadDataThread;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseLazyFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.OrderParser;
import com.leon.biuvideo.values.OrderFollowType;
import com.leon.biuvideo.values.OrderType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * 用户订阅文件夹的详细数据fragment
 */
public class OrderInnerFragment extends BaseLazyFragment {
    private final OrderType orderType;
    private final long mid;
    private final OrderFollowType orderFollowType;

    private LinearLayout smart_refresh_layout_fragment_linearLayout;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;
    private TextView no_data;

    private int total;
    private int currentCount;
    private int pageNum = 1;
    private boolean dataState = true;

    private OrderParser orderParser;
    private List<Order> orders;

    private LinearLayoutManager linearLayoutManager;
    private UserOrderBaseAdapter userOrderBaseAdapter;
    private Handler handler;

    public OrderInnerFragment(long mid, OrderType orderType, OrderFollowType orderFollowType) {
        this.mid = mid;
        this.orderType = orderType;
        this.orderFollowType = orderFollowType;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        smart_refresh_layout_fragment_linearLayout = findView(R.id.smart_refresh_layout_fragment_linearLayout);
        recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);
        smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        no_data = findView(R.id.smart_refresh_layout_fragment_no_data);

        //关闭下拉刷新
        smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void loadData() {
        AbstractSimpleLoadDataThread abstractSimpleLoadDataThread = new AbstractSimpleLoadDataThread() {
            @Override
            public void load() {
                orderParser = new OrderParser(context);
                total = orderParser.getOrderCount(mid, orderType, orderFollowType);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        SimpleThreadPool simpleThreadPool = abstractSimpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(abstractSimpleLoadDataThread), "loadOrders");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                smart_refresh_layout_fragment_linearLayout.setVisibility(View.GONE);

                if (loadState) {
                    initValues();
                }

                simpleThreadPool.cancelTask("loadOrders");

                return true;
            }
        });
    }

    @Override
    public void initValues() {
        if (total <= 0) {
            //设置无数据提示界面
            no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            smartRefresh.setEnabled(false);
        } else {
            no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            smartRefresh.setEnabled(true);

            orders = orderParser.parseOrder(mid, orderType, orderFollowType, pageNum);
            currentCount += orders.size();
            pageNum++;

            //判断第一次加载是否已加载完所有数据
            if (orders.size() < 15) {
                dataState = false;
                //关闭上滑加载
                smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || userOrderBaseAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                userOrderBaseAdapter = new UserOrderBaseAdapter(orders, context, orderType);
            }

            initAttr();
        }
    }

    private void initAttr() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userOrderBaseAdapter);

        Handler handler = new Handler();

        //添加加载更多监听事件
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    SimpleSnackBar.make(view, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();

                    //结束加载更多动画
                    smartRefresh.finishLoadMore();

                    return;
                }

                RefreshState state = refreshLayout.getState();

                //判断是否处于拖拽已释放的状态
                if (state.finishing == RefreshState.ReleaseToLoad.finishing) {
                    if (dataState) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //获取新数据
                                getOrder();

                                //添加新数据
                                userOrderBaseAdapter.append(orders);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        smartRefresh.setEnabled(false);

                        SimpleSnackBar.make(view, R.string.isDone, SimpleSnackBar.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页订阅数据
     */
    private void getOrder() {
        this.orders = orderParser.parseOrder(mid, orderType, orderFollowType, pageNum);

        currentCount += this.orders.size();

        //判断是否已获取完所有的数据
        if (currentCount == total) {
            dataState = false;
        }

        pageNum++;
    }
}
