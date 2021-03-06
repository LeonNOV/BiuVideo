package com.leon.biuvideo.ui.home;

import androidx.fragment.app.Fragment;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseTabLayoutSupportFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularHistoryFragment;
import com.leon.biuvideo.ui.discovery.popularFragments.PopularHotListFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/6
 * @Desc
 */
public class DownloadedFragment extends BaseTabLayoutSupportFragment {
    public static DownloadedFragment getInstance() {
        return new DownloadedFragment();
    }

    public DownloadedFragment() {
        init();
    }

    @Override
    protected void init() {
        setTabTitles(new String[]{"111", "222"});
        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new PopularHistoryFragment());
        viewPagerFragments.add(new PopularHotListFragment());
        setViewPagerFragments(viewPagerFragments);
//        baseTabLayoutSupportFragmentTopBar.setRightSrc(null);
//        baseTabLayoutSupportFragmentTopBar.setTopBarTitle("下载");

        setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                _mActivity.onBackPressed();
            }

            @Override
            public void onRight() {

            }
        });
    }
}
