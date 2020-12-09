package com.leon.biuvideo.ui.fragments.historyFragments;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.BaseFragment;
import com.leon.biuvideo.ui.fragments.BindingUtils;

public class VideoHistoryFragment extends BaseFragment {
    @Override
    public int setLayout() {
        return R.layout.main_fragment_recycler_view;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        RecyclerView recyclerView = findView(R.id.recyclerView);


    }

    @Override
    public void initValues() {

    }
}
