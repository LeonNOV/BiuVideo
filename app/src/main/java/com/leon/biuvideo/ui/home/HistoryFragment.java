package com.leon.biuvideo.ui.home;

import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.HistoryAdapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author Leon
 * @Time 2021/3/5
 * @Desc 历史记录页面
 */
public class HistoryFragment extends BaseSupportFragment {
    public static HistoryFragment getInstance() {
        return new HistoryFragment();
    }

    private static final String[] TYPES = {"video", "bangumi", "article", "series"};

    @Override
    protected int setLayout() {
        return R.layout.history_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar historyTopBar = findView(R.id.history_topBar);
        historyTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(context, "more", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView historyRecyclerView = findView(R.id.history_recyclerView);

        initValue(historyRecyclerView);
    }

    private void initValue(RecyclerView recyclerView) {
        Random random = new Random();
        List<String[]> stringArrays = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String[] data = {
                TYPES[random.nextInt(4)], "DescDescDescDescDescDescDescDescDescDesc"
            };
            stringArrays.add(data);
        }

        HistoryAdapter historyAdapter = new HistoryAdapter(stringArrays, context);
        recyclerView.setAdapter(historyAdapter);
    }
}
