package com.leon.biuvideo.ui.discovery;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.discovery.searchResultFragments.SearchResultArticleFragment;
import com.leon.biuvideo.ui.discovery.searchResultFragments.SearchResultBangumiFragment;
import com.leon.biuvideo.ui.discovery.searchResultFragments.SearchResultBiliUserFragment;
import com.leon.biuvideo.ui.discovery.searchResultFragments.SearchResultVideoFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
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
    private EditText searchResultSearch;

    private String keyword;
    private SearchResultVideoFragment searchResultVideoFragment;
    private SearchResultBangumiFragment searchResultBangumiFragment;
    private SearchResultArticleFragment searchResultArticleFragment;
    private SearchResultBiliUserFragment searchResultBiliUserFragment;

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
        searchResultSearch = findView(R.id.search_result_search);
        searchResultSearch.setText(keyword);
        searchResultSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //按下软键盘的搜索按钮会触发该方法
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //获取输入内容
                    String value = searchResultSearch.getText().toString();

                    if (!"".equals(value)) {
                        keyword = value;
                        hideSoftInput();

//                        searchResultVideoFragment.reSearch(value);
//                        searchResultBangumiFragment.reSearch(value);
//                        searchResultArticleFragment.reSearch(value);
//                        searchResultBiliUserFragment.reSearch(value);

                        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
                    }
                }

                return false;
            }
        });
        findView(R.id.search_result_clear).setOnClickListener(this);

        List<Fragment> viewPagerFragments = new ArrayList<>();
        searchResultVideoFragment = new SearchResultVideoFragment(keyword);
        viewPagerFragments.add(searchResultVideoFragment);

        searchResultBangumiFragment = new SearchResultBangumiFragment(keyword);
        viewPagerFragments.add(searchResultBangumiFragment);

        searchResultArticleFragment = new SearchResultArticleFragment(keyword);
        viewPagerFragments.add(searchResultArticleFragment);

        searchResultBiliUserFragment = new SearchResultBiliUserFragment(keyword);
        viewPagerFragments.add(searchResultBiliUserFragment);

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
            case R.id.search_result_clear:
                searchResultSearch.getText().clear();
                break;
            default:
                break;
        }
    }
}
