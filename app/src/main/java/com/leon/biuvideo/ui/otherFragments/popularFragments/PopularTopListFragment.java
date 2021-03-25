package com.leon.biuvideo.ui.otherFragments.popularFragments;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 热门排行榜页面-排行榜
 */
public class PopularTopListFragment extends BaseSupportFragment {
    @Override
    protected int setLayout() {
        return R.layout.popular_top_list;
    }

    @Override
    protected void initView() {
        SimpleTopBar popularTopList = view.findViewById(R.id.popular_top_list);
        popularTopList.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }
}
