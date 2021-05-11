package com.leon.biuvideo.utils.parseDataUtils;

import android.os.Handler;
import android.os.Message;

import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/11
 * @Desc 列表数据加载器
 */
public class DataLoader<T> implements BaseSupportFragment.OnLoadListener {
    public static final int INIT_CODE = 0;
    public static final int APPEND_CODE = 1;

    private ParserInterface<T> parserInterface;
    private final SmartRefreshRecyclerView<T> smartRefreshRecyclerView;
    private final BaseAdapter<T> baseAdapter;
    private final Handler receiveDataHandler;

    public DataLoader(ParserInterface<T> parserInterface, SmartRefreshRecyclerView<T> smartRefreshRecyclerView, BaseAdapter<T> baseAdapter, BaseSupportFragment baseSupportFragment) {
        this.parserInterface = parserInterface;
        this.smartRefreshRecyclerView = smartRefreshRecyclerView;
        this.baseAdapter = baseAdapter;

        baseSupportFragment.setOnLoadListener(this);
        this.receiveDataHandler = baseSupportFragment.getReceiveDataHandler();
    }

    /**
     * 获取数据，并进行显示
     *
     * @param isFirst   是否为第一次加载
     */
    public void insertData (boolean isFirst) {
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
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<T> data = parserInterface.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = data;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     * 设置数据解析器
     * @param parserInterface   ParserInterface子类
     */
    public void setParserInterface (ParserInterface<T> parserInterface) {
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
