package com.leon.biuvideo.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 创建Fragment时可继承该类来创建Fragment对象
 * 继承该类可更好的创建Fragment
 */
public abstract class BaseFragment extends Fragment {
    protected View view;
    protected Context context;

    /*
     *  Fragment容器ID
     */
    public abstract int setLayout();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setLayout(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        initView(new BindingUtils(view, context));
        initValues();
    }

    public void init () {
        view = getView();
        context = getContext();
    }

    public abstract void initView (BindingUtils bindingUtils);
    public abstract void initValues();

    /**
     * 获取本布局中的某一个控件
     *
     * @param id    控件ID
     * @param <T>   继承View类的控件
     * @return  返回控件对象
     */
    public <T extends View> T findView(int id) {
        return this.view.findViewById(id);
    }
}
