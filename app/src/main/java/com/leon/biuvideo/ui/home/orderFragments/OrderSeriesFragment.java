package com.leon.biuvideo.ui.home.orderFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;

import com.leon.biuvideo.adapters.testAdapters.RvTestAdapter;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 订阅页面-剧集订阅
 */
public class OrderSeriesFragment extends BaseSupportFragmentWithSrr<RvTestBean> {
    @Override
    protected void initView() {
        view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
//                RefreshState state = refreshLayout.getState();
                Random random = new Random();

                List<RvTestBean> rvTestBeanList = new ArrayList<>();
                RvTestBean rvTestBean = new RvTestBean();
                rvTestBean.title = "Title" + random.nextInt(100);
                rvTestBean.view = (random.nextInt(5000) + 5000);
                rvTestBeanList.add(rvTestBean);

                view.append(rvTestBeanList);

                // 显示加载成功
                refreshLayout.finishLoadMore(true);

                // 标记没有更多的数据
//                refreshLayout.finishLoadMoreWithNoMoreData();

                //结束加载更多动画
                view.finishLoadMore();

                // 全部加载完后使用该方法
//                orderBangumiSmartRefreshRecyclerView.setEnablePureScrollMode(true);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {

            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        view.setStatus(LoadingRecyclerView.LOADING);
        List<RvTestBean> rvTestBeanList = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            RvTestBean rvTestBean = new RvTestBean();

            rvTestBean.title = "Title" + (i + 1);
            rvTestBean.view = (random.nextInt(5000) + 5000);

            rvTestBeanList.add(rvTestBean);
        }

        view.setRecyclerViewAdapter(new RvTestAdapter(rvTestBeanList, context));
        view.setStatus(LoadingRecyclerView.LOADING_FINISH);
    }
}
