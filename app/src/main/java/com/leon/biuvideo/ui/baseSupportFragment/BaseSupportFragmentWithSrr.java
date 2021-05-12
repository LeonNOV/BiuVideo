package com.leon.biuvideo.ui.baseSupportFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;

/**
 * @Author Leon
 * @Time 2021/3/9
 * @Desc 基本的LazySupportFragment，该Fragment默认设置{@link com.leon.biuvideo.ui.views.SmartRefreshRecyclerView}为布局,所以不用指定布局文件
 */
public abstract class BaseSupportFragmentWithSrr<T> extends BaseLazySupportFragment {
    protected Context context;
    protected BindingUtils bindingUtils;
    protected SmartRefreshRecyclerView<T> view;

    @Override
    protected int setLayout() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();
        this.view = new SmartRefreshRecyclerView<>(context);
        int padding = context.getResources().getDimensionPixelOffset(R.dimen.recyclerViewPadding);
        this.view.setBackgroundColor(context.getColor(R.color.bg));
        this.view.setPadding(padding, padding, padding, padding);
        this.bindingUtils = new BindingUtils(view, context);

        receiveDataHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (onLoadListener != null) {
                    onLoadListener.onLoad(msg);
                }

                return true;
            }
        });

        initView();

        return view;
    }

    /**
     * 获取SmartRefreshRecyclerView
     */
    public SmartRefreshRecyclerView<T> getSmartRefreshRecyclerView() {
        return view;
    }

    /**
     * 初始化控件
     */
    @Override
    protected abstract void initView();
}