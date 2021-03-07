package com.leon.biuvideo.ui.user;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.TagView;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 用户个人详细信息页面
 */
public class UserInfoFragment extends BaseSupportFragment {
    public static UserInfoFragment getInstance() {
        return new UserInfoFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.user_info_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar user_info_fragment_topBar = view.findViewById(R.id.user_info_fragment_topBar);
        user_info_fragment_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
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
