package com.leon.biuvideo.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.mainFragments.DiscoveryFragment;
import com.leon.biuvideo.ui.mainFragments.HomeFragment;
import com.leon.biuvideo.ui.mainFragments.UserFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 导航Fragment，用于显示底部导航栏
 */
public class NavFragment extends BaseSupportFragment implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final SupportFragment[] supportFragments = new SupportFragment[3];

    public static NavFragment newInstance() {
        return new NavFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.nav_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment homeFragment = findChildFragment(HomeFragment.class);

        if (homeFragment == null) {
            supportFragments[0] = new HomeFragment();
            supportFragments[1] = new DiscoveryFragment();
            supportFragments[2] = new UserFragment();

            loadMultipleRootFragment(R.id.fl_tab_container, 0, supportFragments[0], supportFragments[1], supportFragments[2]);
        } else {
            supportFragments[0] = homeFragment;
            supportFragments[1] = findChildFragment(DiscoveryFragment.class);
            supportFragments[2] = findChildFragment(UserFragment.class);
        }
    }

    @Override
    protected void initView() {
        BottomNavigationView main_nav_bottom = view.findViewById(R.id.main_nav_bottom);
        main_nav_bottom.setItemIconTintList(null);
        main_nav_bottom.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                showHideFragment(supportFragments[0]);
                return true;
            case R.id.discovery:
                showHideFragment(supportFragments[1]);
                return true;
            case R.id.user:
                showHideFragment(supportFragments[2]);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
    }

    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }
}
