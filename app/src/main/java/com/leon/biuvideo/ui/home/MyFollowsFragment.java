package com.leon.biuvideo.ui.home;

import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.MyFollowsAdapter;
import com.leon.biuvideo.beans.Follow;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;

public class MyFollowsFragment extends BaseSupportFragment {
    public static MyFollowsFragment newInstance() {
        return new MyFollowsFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.my_follows_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar my_follow_topBar = findView(R.id.my_follow_topBar);
        my_follow_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                _mActivity.onBackPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(context, "more", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView my_follow_recyclerView = findView(R.id.my_follow_recyclerView);

        initValue(my_follow_recyclerView);
    }

    private void initValue(RecyclerView recyclerView) {
        List<Follow> follows = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Follow follow = new Follow();
            follow.name = "name" + i;
            follow.desc = "DescDescDescDescDescDescDescDescDesc" + i;
            follows.add(follow);
        }

        MyFollowsAdapter myFollowsAdapter = new MyFollowsAdapter(follows, context);
        recyclerView.setAdapter(myFollowsAdapter);
    }
}
