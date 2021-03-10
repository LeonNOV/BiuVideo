package com.leon.biuvideo.ui.home.orderFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.testAdapters.RvTestAdapter;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
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
public class OrderBangumiFragment extends BaseLazySupportFragment {
    private SmartRefreshRecyclerView<RvTestBean> orderBangumiSmartRefreshRecyclerView;

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
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        boolean isVisible = savedInstanceState.getBoolean("isVisible", false);

        if (isVisible) {
            orderBangumiSmartRefreshRecyclerView.setStatus(LoadingRecyclerView.LOADING);
            List<RvTestBean> rvTestBeanList = new ArrayList<>();

            Random random = new Random();

            for (int i = 0; i < 10; i++) {
                RvTestBean rvTestBean = new RvTestBean();

                rvTestBean.title = "Title" + (i + 1);
                rvTestBean.view = (random.nextInt(5000) + 5000);

                rvTestBeanList.add(rvTestBean);
            }

            orderBangumiSmartRefreshRecyclerView.setRecyclerViewAdapter(new RvTestAdapter(rvTestBeanList, context));
            orderBangumiSmartRefreshRecyclerView.setStatus(LoadingRecyclerView.LOADING_FINISH);
        }

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {

            }
        });

        super.onLazyInitView(savedInstanceState);
    }
}
