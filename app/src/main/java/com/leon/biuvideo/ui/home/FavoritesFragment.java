package com.leon.biuvideo.ui.home;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.TabLayoutViewPagerAdapter;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteAlbumFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteArticleFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteVideoFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteWatchLaterFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

public class FavoritesFragment extends SupportFragment {
    public static FavoritesFragment getInstance() {
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        SimpleTopBar favorites_fragment_topBar = view.findViewById(R.id.favorites_fragment_topBar);
        favorites_fragment_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                _mActivity.onBackPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(_mActivity, "more", Toast.LENGTH_SHORT).show();
            }
        });
        TabLayout favorites_fragment_tabLayout = view.findViewById(R.id.favorites_fragment_tabLayout);
        ViewPager favorites_fragment_viewPager = view.findViewById(R.id.favorites_fragment_viewPager);

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new FavoriteVideoFragment());
        viewPagerFragments.add(new FavoriteArticleFragment());
        viewPagerFragments.add(new FavoriteAlbumFragment());
        viewPagerFragments.add(new FavoriteWatchLaterFragment());

        String[] titles = {"视频收藏夹", "专栏收藏夹", "相簿收藏夹", "稍后观看"};
        favorites_fragment_viewPager.setAdapter(new TabLayoutViewPagerAdapter(_mActivity.getSupportFragmentManager(), titles, viewPagerFragments));
        favorites_fragment_viewPager.setCurrentItem(0);
        favorites_fragment_viewPager.setOffscreenPageLimit(4);
        favorites_fragment_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        favorites_fragment_tabLayout.setupWithViewPager(favorites_fragment_viewPager, false);

    }
}
