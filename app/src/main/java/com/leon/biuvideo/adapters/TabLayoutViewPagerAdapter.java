package com.leon.biuvideo.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * TabLayout与ViewPager配合使用所用到的ViewPager适配器
 * <br/>
 * 构造方法中的参数fm，需要确定ViewPager的子fragment是否为嵌套的fragment，如OrderFragment中ViewPager的子fragment就
 * 是嵌套的fragment（其父容器为OrderFragment），所以就要使用getChildFragmentManager()来获取实例
 */
public class TabLayoutViewPagerAdapter extends FragmentPagerAdapter {
    private final String[] titles;
    private final List<Fragment> fragments;

    public TabLayoutViewPagerAdapter(@NonNull FragmentManager fm, String[] titles, List<Fragment> fragments) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
