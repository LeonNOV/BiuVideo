package com.leon.biuvideo.ui.otherFragments.popularFragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 热门排行榜页面-排行榜
 */
public class PopularTopListFragment extends BaseSupportFragment {
    private static final String[] TITLES = {
        "全站",
        "番剧",
        "国产动画",
        "国创相关",
        "纪录片",
        "动画",
        "音乐",
        "舞蹈",
        "游戏",
        "知识",
        "数码",
        "生活",
        "美食",
        "动物圈",
        "鬼畜",
        "时尚",
        "娱乐",
        "影视",
        "电影",
        "电视剧",
        "原创",
        "新人"
    };
    private MainActivity.OnTouchListener onTouchListener;

    @Override
    protected int setLayout() {
        return R.layout.popular_top_list;
    }

    @Override
    protected void initView() {
        SimpleTopBar popularTopList = view.findViewById(R.id.popular_top_list);
        popularTopList.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        List<Fragment> subFragments = new ArrayList<>(TITLES.length);
        for (int i = 0; i < TITLES.length; i++) {
            subFragments.add(new PopularTopListSubFragment());
        }

        TabLayout popularTopListTabLayout = findView(R.id.popular_top_list_tabLayout);
        ViewPager2 popularTopListViewPager = findView(R.id.popular_top_list_viewPager);
        popularTopListViewPager.setAdapter(new ViewPager2Adapter(this, subFragments));

        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), popularTopListTabLayout, popularTopListViewPager, TITLES, 0);
    }

    @Override
    public void onDestroy() {
        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);

        super.onDestroy();
    }
}