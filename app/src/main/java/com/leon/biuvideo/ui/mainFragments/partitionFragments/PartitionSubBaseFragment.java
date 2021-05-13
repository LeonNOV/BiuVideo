package com.leon.biuvideo.ui.mainFragments.partitionFragments;

import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.adapters.homeAdapters.PartitionAdapter;
import com.leon.biuvideo.beans.homeBeans.PartitionVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.PartitionParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 分区子页面
 */
public class PartitionSubBaseFragment extends BaseSupportFragmentWithSrr<PartitionVideo> {
    private final String id;

    private PartitionParser partitionParser;

    public PartitionSubBaseFragment(String id) {
        this.id = id;
    }

    @Override
    protected void initView() {
        PartitionAdapter partitionAdapter = new PartitionAdapter(context);
        partitionAdapter.setHasStableIds(true);

        view.setRecyclerViewAdapter(partitionAdapter);
        view.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        view.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getPartitionResult(1);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<PartitionVideo> partitionVideos = (List<PartitionVideo>) msg.obj;
                if (partitionVideos == null) {
                    view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                    view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                    return;
                }

                switch (msg.what) {
                    case 0:
                        if (partitionVideos.size() == 0) {
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            partitionAdapter.append(partitionVideos);
                            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!partitionParser.dataStatus) {
                                view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }
                        break;
                    case 1:
                        if (partitionVideos.size() > 0) {
                            partitionAdapter.append(partitionVideos);
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            view.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getPartitionResult(int what) {
        if (InternetUtils.checkNetwork(_mActivity.getWindow().getDecorView())) {
            if (partitionParser == null) {
                partitionParser = new PartitionParser(id);
            }
            SimpleSingleThreadPool.executor(new Runnable() {
                @Override
                public void run() {
                    List<PartitionVideo> partitionVideoList = partitionParser.parseData(PartitionParser.ORDER_CLICK);

                    Message message = receiveDataHandler.obtainMessage(what);
                    message.obj = partitionVideoList;
                    receiveDataHandler.sendMessage(message);
                }
            });
        } else {
            view.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
        }
    }

    @Override
    protected void onLazyLoad() {
        view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        getPartitionResult(0);
    }
}
