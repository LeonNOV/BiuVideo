package com.leon.biuvideo.ui.mainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.discovery.SearchFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 发现页面
 */
public class DiscoveryFragment extends SupportFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discovery_fragment, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        LinearLayout discoverySearchLinearLayout = view.findViewById(R.id.discovery_search);
        discoverySearchLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((NavFragment) getParentFragment()).startBrotherFragment(SearchFragment.newInstance());
    }
}
