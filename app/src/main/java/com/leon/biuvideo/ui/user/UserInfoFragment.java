package com.leon.biuvideo.ui.user;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.TagView;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 用户个人详细信息页面
 */
public class UserInfoFragment extends BaseSupportFragment {
    private final UserInfo userInfo;

    public UserInfoFragment(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    protected int setLayout() {
        return R.layout.user_info_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar userInfoFragmentTopBar = view.findViewById(R.id.user_info_fragment_topBar);
        userInfoFragmentTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        ((TagView)view.findViewById(R.id.user_info_fragment_tagView_id)).setRightValue(String.valueOf(userInfo.mid));
        ((TagView)view.findViewById(R.id.user_info_fragment_tagView_name)).setRightValue(String.valueOf(userInfo.userName));
        ((TagView)view.findViewById(R.id.user_info_fragment_tagView_sex)).setRightValue(String.valueOf(userInfo.sex));
        ((TagView)view.findViewById(R.id.user_info_fragment_tagView_level)).setRightValue(String.valueOf(userInfo.currentLevel));
        ((TagView)view.findViewById(R.id.user_info_fragment_tagView_birthday)).setRightValue(String.valueOf(userInfo.birthday));
        ((TagView)view.findViewById(R.id.user_info_fragment_tagView_flag)).setRightValue(String.valueOf(userInfo.sign));
    }
}
