package com.leon.biuvideo.ui.otherFragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.TabLayoutViewPagerAdapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularHistoryFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularHotListFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularTopListFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularWeeklyFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/7
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
        ViewPager popularViewPager = view.findViewById(R.id.popular_viewPager);

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new PopularHotListFragment());
        viewPagerFragments.add(new PopularWeeklyFragment());
        viewPagerFragments.add(new PopularHistoryFragment());
        viewPagerFragments.add(new PopularTopListFragment());

        String[] titles = {"综合热门", "每周必看", "入站必刷", "排行榜"};
        popularViewPager.setAdapter(new TabLayoutViewPagerAdapter(getChildFragmentManager(),titles, viewPagerFragments));
        popularViewPager.setCurrentItem(0);
        popularViewPager.setOffscreenPageLimit(4);
        popularTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view == null) {
                    tab.setCustomView(R.layout.tab_layout_title);
                }

                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextColor(Color.BLACK);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view == null) {
                    tab.setCustomView(R.layout.tab_layout_title);
                }

                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTypeface(Typeface.DEFAULT);
                textView.setTextColor(Color.GRAY);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        popularTabLayout.setupWithViewPager(popularViewPager, false);
    }
}
