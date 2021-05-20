package com.leon.biuvideo.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.mainFragments.DiscoveryFragment;
import com.leon.biuvideo.ui.mainFragments.HomeFragment;
import com.leon.biuvideo.ui.mainFragments.PartitionFragment;
import com.leon.biuvideo.ui.mainFragments.UserFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 导航Fragment，用于显示底部导航栏
 */
public class NavFragment extends BaseSupportFragment {
    private final SupportFragment[] supportFragments = new SupportFragment[4];

    public static NavFragment newInstance() {
        return new NavFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.nav_fragment;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        SupportFragment homeFragment = findChildFragment(HomeFragment.class);

        if (homeFragment == null) {
            supportFragments[0] = new HomeFragment();
            supportFragments[1] = new PartitionFragment();
            supportFragments[2] = new DiscoveryFragment();
            supportFragments[3] = new UserFragment();

            loadMultipleRootFragment(R.id.fl_tab_container, 0, supportFragments[0], supportFragments[1], supportFragments[2], supportFragments[3]);
        } else {
            supportFragments[0] = homeFragment;
            supportFragments[1] = findChildFragment(PartitionFragment.class);
            supportFragments[2] = findChildFragment(DiscoveryFragment.class);
            supportFragments[3] = findChildFragment(UserFragment.class);
        }
    }

    @Override
    protected void initView() {
        BottomNavigationView mainNavBottom = view.findViewById(R.id.main_nav_bottom);
        mainNavBottom.setItemIconTintList(null);
        mainNavBottom.setItemRippleColor(ColorStateList.valueOf(context.getColor(R.color.rippleColor)));
        mainNavBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        showHideFragment(supportFragments[0]);
                        return true;
                    case R.id.partition:
                        showHideFragment(supportFragments[1]);
                        return true;
                    case R.id.discovery:
                        showHideFragment(supportFragments[2]);
                        return true;
                    case R.id.user:
                        showHideFragment(supportFragments[3]);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
    }

    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }
}
