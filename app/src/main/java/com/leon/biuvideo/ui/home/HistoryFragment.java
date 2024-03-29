package com.leon.biuvideo.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.HistoryAdapter;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.HistoryParser;

/**
 * @Author Leon
 * @Time 2021/3/5
 * @Desc 历史记录页面
 */
public class HistoryFragment extends BaseSupportFragment {
    private DataLoader<History> historyDataLoader;

    public static HistoryFragment getInstance() {
        return new HistoryFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.history_fragment;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.history_topBar);

        historyDataLoader = new DataLoader<>(context, new HistoryParser(),
                R.id.history_data, new HistoryAdapter(getMainActivity(), context), this);
    }

    @Override
    public void onLazyInitView(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        historyDataLoader.insertData(true);
    }
}
