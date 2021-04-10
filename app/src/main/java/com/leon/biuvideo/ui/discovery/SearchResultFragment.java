package com.leon.biuvideo.ui.discovery;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.discovery.searchResultFragments.SearchResultArticleFragment;
import com.leon.biuvideo.ui.discovery.searchResultFragments.SearchResultBangumiFragment;
import com.leon.biuvideo.ui.discovery.searchResultFragments.SearchResultBiliUserFragment;
import com.leon.biuvideo.ui.discovery.searchResultFragments.SearchResultVideoFragment;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 搜索结果界面
 */
public class SearchResultFragment extends BaseSupportFragment implements View.OnClickListener {
    private MainActivity.OnTouchListener onTouchListener;
    private final String keyword;

    public SearchResultFragment (String keyword) {
        this.keyword = keyword;
    }

    @Override
    protected int setLayout() {
        return R.layout.search_result_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.search_result_back).setOnClickListener(this);
        findView(R.id.search_result_search).setOnClickListener(this);
        ((TextView) findView(R.id.search_result_keyword)).setText(keyword);
        findView(R.id.search_result_clear).setOnClickListener(this);

        List<Fragment> viewPagerFragments = new ArrayList<>();
        viewPagerFragments.add(new SearchResultVideoFragment(keyword));
        viewPagerFragments.add(new SearchResultBangumiFragment(keyword));
        viewPagerFragments.add(new SearchResultArticleFragment(keyword));
        viewPagerFragments.add(new SearchResultBiliUserFragment(keyword));

        String[] titles = {"视频", "番剧", "专栏", "用户"};
        TabLayout searchResultTabLayout = findView(R.id.search_result_tabLayout);
        ViewPager2 searchResultViewPager = findView(R.id.search_result_viewPager);
        searchResultViewPager.setAdapter(new ViewPager2Adapter(this, viewPagerFragments));

        // 初始化ViewPager2和TabLayout
        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), searchResultTabLayout, searchResultViewPager, titles, 0);
    }

    @Override
    public void onDestroyView() {
        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);

        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_result_back:
                backPressed();
                hideSoftInput();
                break;
            case R.id.search_result_search:
            case R.id.search_result_clear:
                start(new SearchFragment());
                break;
            default:
                break;
        }
    }
}
