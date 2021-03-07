package com.leon.biuvideo.ui.baseSupportFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 基本的SupportFragment
 */
public abstract class BaseSupportFragment extends SupportFragment {
    protected Context context;
    protected View view;
    protected BindingUtils bindingUtils;

    /**
     * 设置LayoutId
     *
     * @return  LayoutId
     */
    protected abstract @LayoutRes int setLayout();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(setLayout(), container, false);
        this.context = getContext();
        this.bindingUtils = new BindingUtils(view, context);

        initView();

        return view;
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 返回方法，用于fragment的返回
     */
    protected void backPressed() {
        _mActivity.onBackPressed();
        onDestroy();
    }

    /**
     * 获取本布局中的某一个控件
     *
     * @param id    控件ID
     * @param <T>   继承View类的控件
     * @return  返回控件对象
     */
    protected  <T extends View> T findView(@IdRes int id) {
        return view.findViewById(id);
    }
}
