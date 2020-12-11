package com.leon.biuvideo.ui.fragments.historyFragments;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.HistoryAdapters.VideoHistoryAdapter;
import com.leon.biuvideo.adapters.UserFragmentAdapters.UserVideoAdapter;
import com.leon.biuvideo.beans.upMasterBean.Video;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.beans.userBeans.HistoryType;
import com.leon.biuvideo.ui.fragments.BaseFragment;
import com.leon.biuvideo.ui.fragments.BindingUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.VideoParser;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.HistoryParser;
import com.leon.biuvideo.values.OrderType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

public class VideoHistoryFragment extends BaseFragment {
    private final String cookie;

    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;
    private TextView no_data;

    private int total;
    private int currentCount;
    private boolean dataState = true;
    private int pageNum = 1;

    private HistoryParser historyParser;
    private History history;

    private LinearLayoutManager linearLayoutManager;
    private VideoHistoryAdapter videoHistoryAdapter;

    public VideoHistoryFragment(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        //获取初始数据
        recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);
        smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        no_data = findView(R.id.smart_refresh_layout_fragment_no_data);

        //关闭下拉刷新
        smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void initValues() {
        historyParser = new HistoryParser();

        this.history = historyParser.parseHistory(cookie, -1, -1, HistoryType.ARCHIVE);
        this.total = history.innerHistory.size();

        //判断结果是否大于0
        if (this.total <= 0) {
            //设置无数据提示界面
            no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            smartRefresh.setEnabled(false);
        } else {
            no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            smartRefresh.setEnabled(true);

            //获取第一页结果总数，最大为20，最小为0
            currentCount += history.innerHistory.size();

            //判断第一次加载是否已加载完所有数据
            if (currentCount < 20) {
                dataState = false;
                //关闭上滑加载
                smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || videoHistoryAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                videoHistoryAdapter = new VideoHistoryAdapter(history.innerHistory, context);
            }

            initAttr();
        }
    }

    /**
     * 初始化控件属性
     */
    private void initAttr() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(videoHistoryAdapter);

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
                        pageNum++;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //获取新数据

                                History temp = getHistory(history.max, history.viewAt);

                                Log.d(Fuck.blue, "成功获取了第" + pageNum + "页的" + temp.innerHistory.size() + "条数据");

                                //添加新数据
                                videoHistoryAdapter.append(temp.innerHistory);
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

    private History getHistory(long max, long viewAt) {
        History history = historyParser.parseHistory(cookie, max, viewAt, HistoryType.ARCHIVE);
        this.history.max = history.max;
        this.history.viewAt = history.viewAt;

        //记录获取的总数
        currentCount += history.innerHistory.size();

        //判断是否已获取完所有的数据
        if (history.innerHistory.size() < 20 || currentCount == total) {
            dataState = false;
        }

        return history;
    }
}
