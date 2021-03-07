package com.leon.biuvideo.ui.home;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.TabLayoutViewPagerAdapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.downloadManagerFragments.DownloadedFragment;
import com.leon.biuvideo.ui.home.downloadManagerFragments.DownloadingFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/6
 * @Desc 下载管理页面
 */
public class DownloadManagerFragment extends BaseSupportFragment {

    public static DownloadManagerFragment getInstance() {
        return new DownloadManagerFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.download_mamager_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar downloadManagerFragmentTopBar = findView(R.id.downloadManager_fragment_topBar);
        downloadManagerFragmentTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {
            }
        });

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new DownloadingFragment());
        viewPagerFragments.add(new DownloadedFragment());

        String[] titles = {"下载中", "已下载"};
        TabLayout downloadManagerFragmentTabLayout = findView(R.id.downloadManager_fragment_tabLayout);
        ViewPager downloadManagerFragmentViewPager = findView(R.id.downloadManager_fragment_viewPager);
        downloadManagerFragmentViewPager.setAdapter(new TabLayoutViewPagerAdapter(getChildFragmentManager(),titles, viewPagerFragments));
        downloadManagerFragmentViewPager.setCurrentItem(0);
        downloadManagerFragmentViewPager.setOffscreenPageLimit(2);
        downloadManagerFragmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        downloadManagerFragmentTabLayout.setupWithViewPager(downloadManagerFragmentViewPager, false);
    }
}
