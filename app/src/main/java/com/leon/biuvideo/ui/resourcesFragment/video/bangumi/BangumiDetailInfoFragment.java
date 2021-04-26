package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/4/26
 * @Desc 番剧详细信息
 */
public class BangumiDetailInfoFragment extends BaseSupportFragment {
    private final Bangumi bangumi;

    public BangumiDetailInfoFragment(Bangumi bangumi) {
        this.bangumi = bangumi;
    }

    @Override
    protected int setLayout() {
        return R.layout.bangumi_detail_info_fragment;
    }

    @Override
    protected void initView() {

    }

    private void getBangumiDetailInfo () {

    }
}
