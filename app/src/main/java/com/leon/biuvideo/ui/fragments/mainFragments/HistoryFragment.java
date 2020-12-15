package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.beans.userBeans.HistoryType;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.fragments.historyFragment.HistoryInnerFragment;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史记录Fragment
 */
public class HistoryFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private TextView history_video, history_live, history_article;

    private Map<Integer, TextView> textViewMap;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_history;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        viewPager = findView(R.id.history_view_pager);
        viewPager.setOffscreenPageLimit(3);

        history_video = findView(R.id.history_video);
        history_video.setOnClickListener(this);

        history_live = findView(R.id.history_live);
        history_live.setOnClickListener(this);

        history_article = findView(R.id.history_article);
        history_article.setOnClickListener(this);
    }

    @Override
    public void initValues() {
        textViewMap = new HashMap<>();
        textViewMap.put(0, history_video);
        textViewMap.put(1, history_live);
        textViewMap.put(2, history_article);

        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        String cookie = initValues.getString("cookie", null);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HistoryInnerFragment(cookie, HistoryType.VIDEO));
        fragments.add(new HistoryInnerFragment(cookie, HistoryType.LIVE));
        fragments.add(new HistoryInnerFragment(cookie, HistoryType.ARTICLE));

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_video:
                viewPager.setCurrentItem(0);
                break;
            case R.id.history_live:
                viewPager.setCurrentItem(1);
                break;
            case R.id.history_article:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                ViewUtils.changeText(textViewMap, 0);
                break;
            case 1:
                ViewUtils.changeText(textViewMap, 1);
                break;
            case 2:
                ViewUtils.changeText(textViewMap, 2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
