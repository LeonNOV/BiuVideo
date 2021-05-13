package com.leon.biuvideo.ui.otherFragments;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.popularAdapters.PopularAdapter;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularPreciousFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularTopListFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularWeeklyFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers.PopularHotListParser;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 综合热门页面
 */
public class PopularFragment extends BaseSupportFragment {
    private DataLoader<PopularVideo> popularVideoDataLoader;

    public static PopularFragment getInstance() {
        return new PopularFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.popular_fragment;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.popular_topBar);

        PopularAdapter popularAdapter = new PopularAdapter(context, PopularAdapter.HOT_VIDEO, true);
        popularAdapter.setOnClickFirstItemListener(new PopularAdapter.OnClickFirstItemListener() {
            @Override
            public void onClickTopList() {
                start(new PopularTopListFragment());
            }

            @Override
            public void onClickWeekly() {
                start(new PopularWeeklyFragment());
            }

            @Override
            public void onClickPrecious() {
                start(new PopularPreciousFragment());
            }
        });

        popularVideoDataLoader = new DataLoader<>(new PopularHotListParser(), R.id.popular_hot_list,
                popularAdapter, this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        popularVideoDataLoader.insertData(true);
    }
}