package com.leon.biuvideo.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.TagView;

import me.yokeyword.fragmentation.SupportFragment;

public class UserInfoFragment extends SupportFragment {
    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        SimpleTopBar user_info_fragment_topBar = view.findViewById(R.id.user_info_fragment_topBar);
        user_info_fragment_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                _mActivity.onBackPressed();
            }

            @Override
            public void onRight() {

            }
        });
        
        TagView user_info_fragment_tagView_name = view.findViewById(R.id.user_info_fragment_tagView_name);
        TagView user_info_fragment_tagView_sex = view.findViewById(R.id.user_info_fragment_tagView_sex);
        TagView user_info_fragment_tagView_level = view.findViewById(R.id.user_info_fragment_tagView_level);
        TagView user_info_fragment_tagView_birthday = view.findViewById(R.id.user_info_fragment_tagView_birthday);
        TagView user_info_fragment_tagView_flag = view.findViewById(R.id.user_info_fragment_tagView_flag);
    }
}
