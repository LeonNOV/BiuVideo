package com.leon.biuvideo.ui.home.orderFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseLazyFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 订阅页面-标签订阅
 */
public class OrderTagsFragment extends BaseLazyFragment {
    @Override
    public int setLayout() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_tags_fragment, container, false);
        return view;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void initValues() {

    }
}
