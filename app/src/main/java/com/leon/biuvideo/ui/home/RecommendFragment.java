package com.leon.biuvideo.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.testAdapters.RvTestAdapter;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 推荐页面
 */
public class RecommendFragment extends BaseSupportFragment {
    private Context context;
    private RecyclerView recommend_recyclerView;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar recommend_topBar = view.findViewById(R.id.recommend_topBar);
        recommend_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(context, "点击了more", Toast.LENGTH_SHORT).show();
            }
        });

        recommend_recyclerView = view.findViewById(R.id.recommend_recyclerView);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        List<RvTestBean> rvTestBeanList = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            RvTestBean rvTestBean = new RvTestBean();

            rvTestBean.title = "Title" + (i + 1);
            rvTestBean.view = (random.nextInt(5000) + 5000);

            rvTestBeanList.add(rvTestBean);
        }

        RvTestAdapter rvTestAdapter = new RvTestAdapter(rvTestBeanList, context);
        recommend_recyclerView.setAdapter(rvTestAdapter);
    }
}
