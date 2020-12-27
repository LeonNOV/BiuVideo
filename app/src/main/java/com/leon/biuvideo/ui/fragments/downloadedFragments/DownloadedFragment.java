package com.leon.biuvideo.ui.fragments.downloadedFragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;

import java.util.ArrayList;
import java.util.List;

public class DownloadedFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    private ViewPager downloaded_fragment_viewPager;

    @Override
    public int setLayout() {
        return R.layout.downloaded_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        downloaded_fragment_viewPager = findView(R.id.downloaded_fragment_viewPager);
    }

    @Override
    public void initValues() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new VideoListFragment());
        fragments.add(new MusicListFragment());

        downloaded_fragment_viewPager.setAdapter(new ViewPageAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments));
        downloaded_fragment_viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
