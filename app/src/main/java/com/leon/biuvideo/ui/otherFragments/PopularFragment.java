package com.leon.biuvideo.ui.otherFragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.TabLayoutViewPagerAdapter;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularHistoryFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularHotListFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularTopListFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularWeeklyFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 热门（综合热门、每周必看、入站必刷、排行榜）页面
 */
public class PopularFragment extends SupportFragment {
    private List<Fragment> viewPagerFragments;

    private TabLayout popular_tabLayout;
    private ViewPager popular_viewPager;

    public static PopularFragment getInstance() {
        return new PopularFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popular_fragment, container, false);

        initView(view);
        initValue();

        return view;
    }

    private void initView(View view) {
        SimpleTopBar popular_topBar = view.findViewById(R.id.popular_topBar);
        popular_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                _mActivity.onBackPressed();
            }

            @Override
            public void onRight() {

            }
        });

        popular_tabLayout = view.findViewById(R.id.popular_tabLayout);
        popular_viewPager = view.findViewById(R.id.popular_viewPager);
    }

    private void initValue() {
        viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new PopularHotListFragment());
        viewPagerFragments.add(new PopularWeeklyFragment());
        viewPagerFragments.add(new PopularHistoryFragment());
        viewPagerFragments.add(new PopularTopListFragment());

        String[] titles = {"综合热门", "每周必看", "入站必刷", "排行榜"};
        popular_viewPager.setAdapter(new TabLayoutViewPagerAdapter(_mActivity.getSupportFragmentManager(),titles, viewPagerFragments));
        popular_viewPager.setCurrentItem(0);
        popular_viewPager.setOffscreenPageLimit(4);
        popular_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        popular_tabLayout.setupWithViewPager(popular_viewPager, false);
    }
}
