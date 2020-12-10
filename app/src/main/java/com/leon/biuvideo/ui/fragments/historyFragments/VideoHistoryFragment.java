package com.leon.biuvideo.ui.fragments.historyFragments;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.BaseFragment;
import com.leon.biuvideo.ui.fragments.BindingUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class VideoHistoryFragment extends BaseFragment {
    private String cookie;

    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;

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

        //关闭下拉刷新
        smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void initValues() {

    }
}
