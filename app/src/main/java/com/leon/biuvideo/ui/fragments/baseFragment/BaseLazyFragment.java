package com.leon.biuvideo.ui.fragments.baseFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseLazyFragment extends Fragment {
    protected View view;
    protected Context context;

    // 界面初始化状态
    protected boolean isLoaded = false;

    /*
     *  Fragment容器ID
     */
    public abstract int setLayout();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setLayout(), container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoaded && !isHidden()) {
            init();
            isLoaded = true;
        }
    }

    public void init () {
        context = getContext();

        initView(new BindingUtils(view, context));
        loadData();
    }

    public abstract void initView (BindingUtils bindingUtils);
    public abstract void loadData();
    public abstract void initValues();

    /**
     * 获取本布局中的某一个控件
     *
     * @param id    控件ID
     * @param <T>   继承View类的控件
     * @return  返回控件对象
     */
    public <T extends View> T findView(int id) {
        return view.findViewById(id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded = false;
    }
}
