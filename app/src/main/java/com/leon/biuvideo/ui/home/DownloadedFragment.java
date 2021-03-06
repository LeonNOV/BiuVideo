package com.leon.biuvideo.ui.home;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/6
 * @Desc
 */
public class DownloadedFragment extends BaseSupportFragment {

    public static DownloadedFragment getInstance() {
        return new DownloadedFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.downloaded_fragment;
    }

    @Override
    protected void initView() {

    }
}
