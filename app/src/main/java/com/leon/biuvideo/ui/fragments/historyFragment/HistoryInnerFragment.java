package com.leon.biuvideo.ui.fragments.historyFragment;

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
import com.leon.biuvideo.adapters.historyAdapters.HistoryAdapter;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.beans.userBeans.HistoryType;
import com.leon.biuvideo.ui.AbstractSimpleLoadDataThread;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseLazyFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.HistoryParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.concurrent.FutureTask;

/**
 * 根据HistoryType创建对应的Fragment
 */
public class HistoryInnerFragment extends BaseLazyFragment {
    private final String cookie;
    private final HistoryType historyType;

    private LinearLayout smart_refresh_layout_fragment_linearLayout;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;
    private TextView no_data;

    private boolean dataState = true;

    private HistoryParser historyParser;
    private History history;

    private LinearLayoutManager linearLayoutManager;
    private HistoryAdapter historyAdapter;
    private Handler handler;

    public HistoryInnerFragment(String cookie, HistoryType historyType) {
        this.cookie = cookie;
        this.historyType = historyType;
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
                historyParser = new HistoryParser(context);
                history = historyParser.parseHistory(cookie, -1, -1, historyType);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        SimpleThreadPool simpleThreadPool = abstractSimpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(abstractSimpleLoadDataThread), "loadHistory");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                smart_refresh_layout_fragment_linearLayout.setVisibility(View.GONE);

                if (loadState) {
                    initValues();
                }

                simpleThreadPool.cancelTask("loadHistory");

                return true;
            }
        });
    }

    @Override
    public void initValues() {
        //判断当前条目数量是否大于0
        if (this.history.innerHistory.size() <= 0) {
            //设置无数据提示界面
            no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            smartRefresh.setEnabled(false);
        } else {
            no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            smartRefresh.setEnabled(true);

            //判断第一次加载是否已加载完所有数据
            if (this.history.innerHistory.size() < 20) {
                dataState = false;
                //关闭上滑加载
                smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || historyAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                historyAdapter = new HistoryAdapter(history.innerHistory, context);
            }

            initAttr();
        }
    }

    /**
     * 初始化控件属性
     */
    private void initAttr() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(historyAdapter);

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
                                History temp = getVideoHistory(history.max, history.viewAt);

                                //添加新数据
                                historyAdapter.append(temp.innerHistory);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        smartRefresh.setEnabled(false);

                        SimpleSnackBar.make(view, "只有这么多数据了~", SimpleSnackBar.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页视频历史记录
     *
     * @param max   history对象中的max变量的数值
     * @param viewAt    history对象中的viewAt变量的数值
     * @return  返回下一页数据
     */
    private History getVideoHistory(long max, long viewAt) {
        History history = historyParser.parseHistory(cookie, max, viewAt, historyType);
        this.history.max = history.max;
        this.history.viewAt = history.viewAt;

        //判断是否已获取完所有的数据
        if (history.innerHistory.size() < 20) {
            dataState = false;
        }

        return history;
    }
}
