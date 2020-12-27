package com.leon.biuvideo.ui.fragments.orderFragments;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.OrderAdapter;
import com.leon.biuvideo.beans.userBeans.Order;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.OrderParser;
import com.leon.biuvideo.values.OrderFollowType;
import com.leon.biuvideo.values.OrderType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

public class OrderInnerFragment extends BaseFragment {
    private final String cookie;
    private final OrderType orderType;
    private final long mid;
    private final OrderFollowType orderFollowType;

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
    private OrderAdapter orderAdapter;

    public OrderInnerFragment(long mid, String cookie, OrderType orderType, OrderFollowType orderFollowType) {
        this.mid = mid;
        this.cookie = cookie;
        this.orderType = orderType;
        this.orderFollowType = orderFollowType;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);
        smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        no_data = findView(R.id.smart_refresh_layout_fragment_no_data);

        //关闭下拉刷新
        smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void initValues() {
        orderParser = new OrderParser();

        switch (orderType) {
            case VIDEO:
                //判断是否已登陆
            case ARTICLE:
                //判断是否已登陆
                if (mid == -1 || cookie == null) {

                }
                break;
        }

        total = orderParser.getOrderCount(mid, cookie, orderType, orderFollowType);

        if (total <= 0) {
            //设置无数据提示界面
            no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            smartRefresh.setEnabled(false);
        } else {
            no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            smartRefresh.setEnabled(true);

            orders = orderParser.parseOrder(mid, cookie, orderType, orderFollowType, pageNum);
            currentCount += orders.size();
            pageNum++;

            //判断第一次加载是否已加载完所有数据
            if (orders.size() < 15) {
                dataState = false;
                //关闭上滑加载
                smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || orderAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                Fuck.blue("linearLayoutManager" + linearLayoutManager);
                orderAdapter = new OrderAdapter(orders, context, orderType);
            }

            initAttr();
        }

    }

    private void initAttr() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(orderAdapter);

        Handler handler = new Handler();

        //添加加载更多监听事件
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

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
                                orderAdapter.append(orders);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        smartRefresh.setEnabled(false);

                        Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
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
        this.orders = orderParser.parseOrder(mid, cookie, orderType, orderFollowType, pageNum);

        currentCount += this.orders.size();

        //判断是否已获取完所有的数据
        if (currentCount >= total || this.orders.size() < 15) {
            dataState = false;
        }

        pageNum++;
    }
}
