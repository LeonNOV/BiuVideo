package com.leon.biuvideo.ui.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.TabLayoutViewPagerAdapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteAlbumFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteArticleFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteVideoFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteWatchLaterFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 收藏夹页面
 */
public class FavoritesFragment extends BaseSupportFragment {

    public static FavoritesFragment getInstance() {
        return new FavoritesFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.favorites_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar favorites_fragment_topBar = view.findViewById(R.id.favorites_fragment_topBar);
        favorites_fragment_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(_mActivity, "more", Toast.LENGTH_SHORT).show();
            }
        });
        TabLayout favoritesFragmentTabLayout = view.findViewById(R.id.favorites_fragment_tabLayout);
        ViewPager favoritesFragmentViewPager = view.findViewById(R.id.favorites_fragment_viewPager);

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new FavoriteVideoFragment());
        viewPagerFragments.add(new FavoriteArticleFragment());
        viewPagerFragments.add(new FavoriteAlbumFragment());
        viewPagerFragments.add(new FavoriteWatchLaterFragment());

        String[] titles = {"视频收藏夹", "专栏收藏夹", "相簿收藏夹", "稍后观看"};
        favoritesFragmentViewPager.setAdapter(new TabLayoutViewPagerAdapter(getChildFragmentManager(), titles, viewPagerFragments));
        favoritesFragmentViewPager.setCurrentItem(0);
        favoritesFragmentViewPager.setOffscreenPageLimit(4);
        favoritesFragmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        favoritesFragmentTabLayout.setupWithViewPager(favoritesFragmentViewPager, false);
    }
}
