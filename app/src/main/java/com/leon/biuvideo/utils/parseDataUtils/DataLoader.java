package com.leon.biuvideo.utils.parseDataUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/11
 * @Desc 列表数据加载器
 */
public class DataLoader<T> implements BaseSupportFragment.OnLoadListener {
    public static final int INIT_CODE = 0;
    public static final int APPEND_CODE = 1;

    private final Context context;
    private ParserInterface<T> parserInterface;
    private final SmartRefreshRecyclerView<T> smartRefreshRecyclerView;
    private final BaseAdapter<T> baseAdapter;
    private final Handler receiveDataHandler;

    public DataLoader(Context context, @NotNull ParserInterface<T> parserInterface, @NotNull BaseAdapter<T> baseAdapter, BaseSupportFragmentWithSrr<T> baseSupportFragmentWithSrr) {
        this.context = context;
        this.parserInterface = parserInterface;
        this.smartRefreshRecyclerView = baseSupportFragmentWithSrr.getSmartRefreshRecyclerView();

        this.baseAdapter = baseAdapter;
        this.baseAdapter.setHasStableIds(true);

        baseSupportFragmentWithSrr.setOnLoadListener(this);
        this.receiveDataHandler = baseSupportFragmentWithSrr.getReceiveDataHandler();

        initSmartRefreshRecyclerView();
    }

    public DataLoader(Context context, @NotNull ParserInterface<T> parserInterface, @IdRes int smartRefreshRecyclerView, @NotNull BaseAdapter<T> baseAdapter, BaseSupportFragment baseSupportFragment) {
        this.context = context;
        this.parserInterface = parserInterface;
        this.smartRefreshRecyclerView = baseSupportFragment.findView(smartRefreshRecyclerView);

        this.baseAdapter = baseAdapter;
        this.baseAdapter.setHasStableIds(true);

        baseSupportFragment.setOnLoadListener(this);
        this.receiveDataHandler = baseSupportFragment.getReceiveDataHandler();

        initSmartRefreshRecyclerView();
    }

    public DataLoader(@NotNull ParserInterface<T> parserInterface, @IdRes int smartRefreshRecyclerView, @NotNull BaseAdapter<T> baseAdapter, BaseSupportFragment baseSupportFragment, RecyclerView.LayoutManager layoutManager, Context context) {
        this.parserInterface = parserInterface;
        this.context = context;
        this.smartRefreshRecyclerView = baseSupportFragment.findView(smartRefreshRecyclerView);

        this.baseAdapter = baseAdapter;
        this.baseAdapter.setHasStableIds(true);

        baseSupportFragment.setOnLoadListener(this);
        this.receiveDataHandler = baseSupportFragment.getReceiveDataHandler();

        initSmartRefreshRecyclerView();
        this.smartRefreshRecyclerView.setRecyclerViewLayoutManager(layoutManager);
    }

    /**
     * 初始化SmartRefreshRecyclerView
     * 对SmartRefreshRecyclerView设置Adapter和加载更多监听
     */
    private void initSmartRefreshRecyclerView() {
        if (smartRefreshRecyclerView != null) {
            this.smartRefreshRecyclerView.setRecyclerViewAdapter(baseAdapter);
            this.smartRefreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    insertData(false);
                }
            });
        } else {
            throw new NullPointerException("SmartRefreshRecyclerView 为null");
        }
    }

    /**
     * 获取数据，并进行显示
     *
     * @param isFirst   是否为第一次加载
     */
    public void insertData (boolean isFirst) {
        if (isFirst) {
            smartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        }
        getApiData(isFirst ? INIT_CODE : APPEND_CODE);
    }

    /**
     * 获取数据，并进行显示
     *
     * @param what   what
     */
    public void insertData (int what) {
        getApiData(what);
    }

    private void getApiData(int what) {
        if (InternetUtils.checkNetwork(context)) {
            SimpleSingleThreadPool.executor(new Runnable() {
                @Override
                public void run() {
                    List<T> data = parserInterface.parseData();

                    Message message = receiveDataHandler.obtainMessage(what);
                    message.obj = data;
                    receiveDataHandler.sendMessage(message);
                }
            });
        } else {
            Message message = receiveDataHandler.obtainMessage(what);
            message.obj = null;
            receiveDataHandler.sendMessage(message);
        }
    }

    /**
     * 对数据进行重置
     *
     * @param parserInterface   新的数据解析器
     */
    public void reset (@NotNull ParserInterface<T> parserInterface) {
        baseAdapter.removeAll();
        this.parserInterface = parserInterface;
        insertData(true);
    }

    /**
     * 设置数据解析器
     * @param parserInterface   ParserInterface子类
     */
    public void setParserInterface (@NotNull ParserInterface<T> parserInterface) {
        this.parserInterface = parserInterface;
    }

    @Override
    public void onLoad(Message msg) {
        List<T> newData = (List<T>) msg.obj;

        switch (msg.what) {
            case 0:
                if (newData != null && newData.size() > 0) {
                    baseAdapter.append(newData);
                    smartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                    smartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);

                    if (!parserInterface.dataStatus) {
                        smartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                    }
                } else {
                    smartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                    smartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                }
                break;

            case 1:
                if (newData != null && newData.size() > 0) {
                    baseAdapter.append(newData);
                    smartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);

                    if (!parserInterface.dataStatus) {
                        smartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                    }
                } else {
                    smartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                }
                break;
            default:
                break;
        }
    }
}
