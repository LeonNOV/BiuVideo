package com.leon.biuvideo.ui.baseSupportFragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.TabLayoutViewPagerAdapter;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/6
 * @Desc {@link com.google.android.material.tabs.TabLayout}、{@link androidx.viewpager.widget.ViewPager}和{@link com.leon.biuvideo.ui.views.SimpleTopBar}的SupportFragment
 */
public abstract class BaseTabLayoutSupportFragment extends BaseSupportFragment  {

    private List<Fragment> viewPagerFragments;
    private String[] tabTitles;

    private SimpleTopBar.OnSimpleTopBarListener onSimpleTopBarListener;
    public SimpleTopBar baseTabLayoutSupportFragmentTopBar;

    @Override
    protected int setLayout() {
        return R.layout.base_tab_layout_support_fragment;
    }

    @Override
    protected void initView() {
        init();
        baseTabLayoutSupportFragmentTopBar = findView(R.id.base_tab_layout_support_fragment_topBar);
        baseTabLayoutSupportFragmentTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                if (onSimpleTopBarListener != null) {
                    onSimpleTopBarListener.onLeft();
                }
            }

            @Override
            public void onRight() {
                if (onSimpleTopBarListener != null) {
                    onSimpleTopBarListener.onRight();
                }
            }
        });

        TabLayout baseTabLayoutSupportFragmentTabLayout = findView(R.id.base_tab_layout_support_fragment_tabLayout);
        ViewPager baseTabLayoutSupportFragmentViewPager = findView(R.id.base_tab_layout_support_fragment_viewPager);

        baseTabLayoutSupportFragmentViewPager.setAdapter(new TabLayoutViewPagerAdapter(getChildFragmentManager(), tabTitles, viewPagerFragments));
        baseTabLayoutSupportFragmentViewPager.setCurrentItem(0);
        baseTabLayoutSupportFragmentViewPager.setOffscreenPageLimit(viewPagerFragments.size());
        baseTabLayoutSupportFragmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        baseTabLayoutSupportFragmentTabLayout.setupWithViewPager(baseTabLayoutSupportFragmentViewPager, false);
    }

    /**
     * 初始化控件数据
     */
    protected abstract void init();

    public void setViewPagerFragments(List<Fragment> viewPagerFragments) {
        this.viewPagerFragments = viewPagerFragments;
    }

    public void setTabTitles(String[] tabTitles) {
        this.tabTitles = tabTitles;
    }

    public void setOnSimpleTopBarListener(SimpleTopBar.OnSimpleTopBarListener onSimpleTopBarListener) {
        this.onSimpleTopBarListener = onSimpleTopBarListener;
    }
}
