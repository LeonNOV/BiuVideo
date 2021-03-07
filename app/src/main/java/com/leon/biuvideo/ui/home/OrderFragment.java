package com.leon.biuvideo.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.TabLayoutViewPagerAdapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.orderFragments.OrderBangumiFragment;
import com.leon.biuvideo.ui.home.orderFragments.OrderSeriesFragment;
import com.leon.biuvideo.ui.home.orderFragments.OrderTagsFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 订阅页面
 */
public class OrderFragment extends BaseSupportFragment {

    public static SupportFragment getInstance() {
        return new OrderFragment();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    protected int setLayout() {
        return R.layout.order_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar orderFragmentTopBar = findView(R.id.order_fragment_topBar);
        orderFragmentTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {
            }
        });

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new OrderBangumiFragment());
        viewPagerFragments.add(new OrderSeriesFragment());
        viewPagerFragments.add(new OrderTagsFragment());

        String[] titles = {"番剧", "剧集", "标签"};
        TabLayout orderFragmentTabLayout = findView(R.id.order_fragment_tabLayout);
        ViewPager orderFragmentViewPager = findView(R.id.order_fragment_viewPager);
        orderFragmentViewPager.setAdapter(new TabLayoutViewPagerAdapter(getChildFragmentManager(),titles, viewPagerFragments));
        orderFragmentViewPager.setCurrentItem(0);
        orderFragmentViewPager.setOffscreenPageLimit(3);
        orderFragmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewUtils.changeTabTitle(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ViewUtils.changeTabTitle(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        orderFragmentTabLayout.setupWithViewPager(orderFragmentViewPager, false);
    }
}