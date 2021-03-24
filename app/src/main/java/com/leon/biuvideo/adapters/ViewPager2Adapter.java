package com.leon.biuvideo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/20
 * @Desc ViewPager2适配器
 */
public class ViewPager2Adapter extends FragmentStateAdapter {
    private final List<Fragment> fragments;

    public ViewPager2Adapter(@NonNull Fragment fragment, List<Fragment> fragments) {
        super(fragment);

        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
