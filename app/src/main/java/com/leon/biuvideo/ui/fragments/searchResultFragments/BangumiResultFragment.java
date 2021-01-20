package com.leon.biuvideo.ui.fragments.searchResultFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userFragmentAdapters.BangumiAdapter;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.ui.SimpleLoadDataThread;
import com.leon.biuvideo.ui.dialogs.LoadingDialog;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseLazyFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiParser;
import com.leon.biuvideo.values.SortType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * 番剧搜索结果fragment
 */
public class BangumiResultFragment extends BaseLazyFragment {
    private LinearLayout smart_refresh_layout_fragment_linearLayout;
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;
    private TextView search_result_no_data;
    private String keyword;

    private BangumiParser bangumiParser;
    private List<Bangumi> bangumiList;

    private int count;
    private int pageNum = 1;
    private int currentCount;
    private boolean dataState;

    private BangumiAdapter bangumiAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler;

    public BangumiResultFragment(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        smart_refresh_layout_fragment_linearLayout = findView(R.id.smart_refresh_layout_fragment_linearLayout);
        search_result_no_data = findView(R.id.smart_refresh_layout_fragment_no_data);
        search_result_smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        search_result_recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void loadData() {
        SimpleLoadDataThread simpleLoadDataThread = new SimpleLoadDataThread() {
            @Override
            public void load() {
                if (bangumiParser == null) {
                    bangumiParser = new BangumiParser(context);
                }
                count = bangumiParser.getSearchBangumiCount(keyword);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        SimpleThreadPool simpleThreadPool = simpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(simpleLoadDataThread), "loadBangumiResult");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                smart_refresh_layout_fragment_linearLayout.setVisibility(View.GONE);

                if (loadState) {
                    initValues();
                }

                simpleThreadPool.cancelTask("loadBangumiResult");

                return true;
            }
        });
    }

    @Override
    public void initValues() {
        if (count == 0) {
            //设置无数据提示界面
            search_result_no_data.setVisibility(View.VISIBLE);
            search_result_recyclerView.setVisibility(View.GONE);
            search_result_smartRefresh.setVisibility(View.GONE);
        } else {
            search_result_no_data.setVisibility(View.GONE);
            search_result_recyclerView.setVisibility(View.VISIBLE);
            search_result_smartRefresh.setVisibility(View.VISIBLE);

            bangumiList = bangumiParser.bangumiParse(keyword, pageNum, SortType.DEFAULT);
            currentCount = bangumiList.size();
            pageNum++;


            if (currentCount == count) {
                dataState = false;
                search_result_smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || bangumiAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                bangumiAdapter = new BangumiAdapter(bangumiList, context);
            }

            bangumiAdapter.append(bangumiList);

            initAttr();
        }
    }

    private void initAttr() {
        search_result_recyclerView.setAdapter(bangumiAdapter);
        search_result_recyclerView.setLayoutManager(linearLayoutManager);

        Handler handler = new Handler();

        //添加加载更多监听事件
        search_result_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Snackbar.make(view, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();

                    //结束加载更多动画
                    search_result_smartRefresh.finishLoadMore();

                    return;
                }

                RefreshState state = refreshLayout.getState();

                //判断是否处于拖拽已释放的状态
                if (state.finishing == RefreshState.ReleaseToLoad.finishing) {
                    if (dataState) {
                        pageNum++;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //获取新数据
                                getBangumi();

                                //添加新数据
                                bangumiAdapter.append(bangumiList);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        search_result_smartRefresh.setEnabled(false);

                        Snackbar.make(view, R.string.isDone, Snackbar.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                search_result_smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页数据
     */
    private void getBangumi() {
        this.bangumiList = bangumiParser.bangumiParse(keyword, pageNum, SortType.DEFAULT);

        currentCount += this.bangumiList.size();

        if (currentCount == count) {
            dataState = false;
            search_result_smartRefresh.setEnabled(false);
        }
    }

    /**
     * 用于二次搜索刷新fragment数据
     *
     * @param keyword   搜索关键字
     */
    public void updateData(String keyword) {
        this.keyword = keyword;
        this.pageNum = 1;
        this.currentCount = 0;
        this.dataState = true;

        // 将isLoaded状态设置为“未加载状态”
        this.isLoaded = false;
        onResume();
        this.smart_refresh_layout_fragment_linearLayout.setVisibility(View.VISIBLE);

        if (bangumiList != null) {
            bangumiAdapter.removeAll();
        }
    }
}
