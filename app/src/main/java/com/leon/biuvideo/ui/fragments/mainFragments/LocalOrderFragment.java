package com.leon.biuvideo.ui.fragments.mainFragments;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;

/**
 * 播放列表fragment
 */
public class LocalOrderFragment extends BaseFragment {
    private TabLayout main_fragment_local_order_tabLayout;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_local_order;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        main_fragment_local_order_tabLayout = findView(R.id.main_fragment_local_order_tabLayout);
    }

    @Override
    public void initValues() {
        main_fragment_local_order_tabLayout.addTab(main_fragment_local_order_tabLayout.newTab().setText("1"));
        main_fragment_local_order_tabLayout.addTab(main_fragment_local_order_tabLayout.newTab().setText("2"));
        main_fragment_local_order_tabLayout.addTab(main_fragment_local_order_tabLayout.newTab().setText("3"));
    }
}
