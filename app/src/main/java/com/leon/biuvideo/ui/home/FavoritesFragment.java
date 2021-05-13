package com.leon.biuvideo.ui.home;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.FavoriteArticleFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.favoriteVideo.FavoriteVideoFolderFragment;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 收藏夹页面
 */
public class FavoritesFragment extends BaseSupportFragment {

    private MainActivity.OnTouchListener onTouchListener;

    public static FavoritesFragment getInstance() {
        return new FavoritesFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.favorites_fragment;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.favorites_fragment_topBar);

        TabLayout favoritesFragmentTabLayout = findView(R.id.favorites_fragment_tabLayout);
        ViewPager2 favoritesFragmentViewPager = findView(R.id.favorites_fragment_viewPager);

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new FavoriteVideoFolderFragment());
        viewPagerFragments.add(new FavoriteArticleFragment());

        String[] titles = {"视频收藏夹", "专栏收藏夹"};
        favoritesFragmentViewPager.setAdapter(new ViewPager2Adapter(this, viewPagerFragments));

        // 初始化ViewPager2和TabLayout
        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), favoritesFragmentTabLayout, favoritesFragmentViewPager, titles, 0);
    }

    @Override
    public void onDestroyView() {
        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);

        super.onDestroyView();
    }
}
