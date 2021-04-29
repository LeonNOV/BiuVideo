package com.leon.biuvideo.ui.home;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.orderFragments.OrderDataFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.OrderType;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 订阅页面
 */
public class OrderFragment extends BaseSupportFragment {

    private MainActivity.OnTouchListener onTouchListener;

    public static SupportFragment getInstance() {
        return new OrderFragment();
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
        viewPagerFragments.add(new OrderDataFragment(OrderType.BANGUMI));
        viewPagerFragments.add(new OrderDataFragment(OrderType.SERIES));

        /*viewPagerFragments.add(new OrderTagsFragment());*/

        String[] titles = {"番剧", "剧集"};
        TabLayout orderFragmentTabLayout = findView(R.id.order_fragment_tabLayout);
        ViewPager2 orderFragmentViewPager = findView(R.id.order_fragment_viewPager);
        orderFragmentViewPager.setAdapter(new ViewPager2Adapter(this, viewPagerFragments));

        // 初始化ViewPager2和TabLayout
        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), orderFragmentTabLayout, orderFragmentViewPager, titles, 0);
    }

    @Override
    public void onDestroyView() {
        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);

        super.onDestroyView();
    }
}