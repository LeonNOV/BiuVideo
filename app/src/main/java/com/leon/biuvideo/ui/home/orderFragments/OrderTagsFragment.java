package com.leon.biuvideo.ui.home.orderFragments;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.TagAdapter;
import com.leon.biuvideo.beans.orderBeans.Tag;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.TagParser;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 订阅页面-标签订阅
 */
public class OrderTagsFragment extends BaseSupportFragment {

    private LoadingRecyclerView orderTagsLoadingRecyclerView;
    private List<Tag> tagList;

    @Override
    public int setLayout() {
        return R.layout.order_tags_fragment;
    }

    @Override
    protected void initView() {
        orderTagsLoadingRecyclerView = findView(R.id.order_tags_loadingRecyclerView);
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                if (msg.what == 0) {
                    orderTagsLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
                    orderTagsLoadingRecyclerView.setRecyclerViewAdapter(new TagAdapter(tagList, context));

                    orderTagsLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                }
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        orderTagsLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                tagList = new TagParser().parseData();

                Message message = receiveDataHandler.obtainMessage();
                message.what = 0;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
