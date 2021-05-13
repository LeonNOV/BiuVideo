package com.leon.biuvideo.ui.user;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
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
        setTopBar(R.id.user_info_fragment_topBar);

        ((TagView)findView(R.id.user_info_fragment_tagView_id)).setRightValue(String.valueOf(userInfo.mid));
        ((TagView)findView(R.id.user_info_fragment_tagView_name)).setRightValue(String.valueOf(userInfo.userName));
        ((TagView)findView(R.id.user_info_fragment_tagView_sex)).setRightValue(String.valueOf(userInfo.sex));
        ((TagView)findView(R.id.user_info_fragment_tagView_level)).setRightValue(String.valueOf(userInfo.currentLevel));
        ((TagView)findView(R.id.user_info_fragment_tagView_birthday)).setRightValue(String.valueOf(userInfo.birthday));
        ((TagView)findView(R.id.user_info_fragment_tagView_flag)).setRightValue(String.valueOf(userInfo.sign));
    }
}
