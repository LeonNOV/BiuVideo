package com.leon.biuvideo.ui.home;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.downloadManagerFragments.DownloadedFragment;
import com.leon.biuvideo.ui.home.downloadManagerFragments.DownloadingFragment;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/6
 * @Desc 下载管理页面
 */
public class DownloadManagerFragment extends BaseSupportFragment {
    private MainActivity.OnTouchListener onTouchListener;

    public static DownloadManagerFragment getInstance() {
        return new DownloadManagerFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.download_mamager_fragment;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.downloadManager_fragment_topBar);

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new DownloadingFragment());
        viewPagerFragments.add(new DownloadedFragment());

        String[] titles = {"下载中", "已下载"};
        TabLayout downloadManagerFragmentTabLayout = findView(R.id.downloadManager_fragment_tabLayout);
        ViewPager2 downloadManagerFragmentViewPager = findView(R.id.downloadManager_fragment_viewPager);
        downloadManagerFragmentViewPager.setAdapter(new ViewPager2Adapter(this, viewPagerFragments));
        // 初始化ViewPager2和TabLayout

        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), downloadManagerFragmentTabLayout, downloadManagerFragmentViewPager, titles, 0);
    }

    @Override
    public void onDestroyView() {
        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);

        super.onDestroyView();
    }
}
