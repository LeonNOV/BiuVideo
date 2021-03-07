package com.leon.biuvideo.ui.otherFragments;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 登录界面
 */
public class LoginFragment extends BaseSupportFragment {
    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.login_fragment;
    }

    @Override
    protected void initView() {

    }
}
