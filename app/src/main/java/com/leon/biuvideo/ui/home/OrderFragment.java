package com.leon.biuvideo.ui.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.TabLayoutViewPagerAdapter;
import com.leon.biuvideo.ui.home.orderFragments.OrderBangumiFragment;
import com.leon.biuvideo.ui.home.orderFragments.OrderSeriesFragment;
import com.leon.biuvideo.ui.home.orderFragments.OrderTagsFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 订阅页面
 */
public class OrderFragment extends SupportFragment {
    private List<Fragment> viewPagerFragments;

    public static SupportFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        SimpleTopBar order_fragment_topBar = view.findViewById(R.id.order_fragment_topBar);
        order_fragment_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                _mActivity.onBackPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(_mActivity, "more", Toast.LENGTH_SHORT).show();
            }
        });

        TabLayout order_fragment_tabLayout = view.findViewById(R.id.order_fragment_tabLayout);
        ViewPager order_fragment_viewPager = view.findViewById(R.id.order_fragment_viewPager);

        viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new OrderBangumiFragment());
        viewPagerFragments.add(new OrderSeriesFragment());
        viewPagerFragments.add(new OrderTagsFragment());

        String[] titles = {"番剧", "剧集", "标签"};
        order_fragment_viewPager.setAdapter(new TabLayoutViewPagerAdapter(_mActivity.getSupportFragmentManager(),titles, viewPagerFragments));
        order_fragment_viewPager.setCurrentItem(0);
        order_fragment_viewPager.setOffscreenPageLimit(4);
        order_fragment_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        order_fragment_tabLayout.setupWithViewPager(order_fragment_viewPager, false);
    }
}