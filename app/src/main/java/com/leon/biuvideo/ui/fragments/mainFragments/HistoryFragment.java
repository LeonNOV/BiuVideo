package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.ui.fragments.BaseFragment;
import com.leon.biuvideo.ui.fragments.BindingUtils;
import com.leon.biuvideo.ui.fragments.historyFragments.VideoHistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.main_fragment_history;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        ViewPager viewPager = findView(R.id.history_view_pager);

        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        String cookie = initValues.getString("cookie", null);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new VideoHistoryFragment(cookie));

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        viewPager.setAdapter(viewPageAdapter);
    }

    @Override
    public void initValues() {

    }
}
