package com.leon.biuvideo.ui.otherFragments;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularHistoryFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularHotListFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularTopListFragment;
import com.leon.biuvideo.ui.otherFragments.popularFragments.PopularWeeklyFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 热门（综合热门、每周必看、入站必刷、排行榜）页面
 */
public class PopularFragment extends BaseSupportFragment {

    public static PopularFragment getInstance() {
        return new PopularFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.popular_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar popularTopBar = view.findViewById(R.id.popular_topBar);
        popularTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        TabLayout popularTabLayout = view.findViewById(R.id.popular_tabLayout);
        ViewPager2 popularViewPager = view.findViewById(R.id.popular_viewPager);

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new PopularHotListFragment());
        viewPagerFragments.add(new PopularWeeklyFragment());
        viewPagerFragments.add(new PopularHistoryFragment());
        viewPagerFragments.add(new PopularTopListFragment());

        String[] titles = {"综合热门", "每周必看", "入站必刷", "排行榜"};
        popularViewPager.setAdapter(new ViewPager2Adapter(this, viewPagerFragments));

        // 初始化ViewPager2和TabLayout
        ViewUtils.initTabLayoutAndViewPager2(popularTabLayout, popularViewPager, titles, 0);
    }
}
