package com.leon.biuvideo.ui.baseSupportFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author Leon
 * @Time 2021/3/9
 * @Desc 基本的LazySupportFragment，该Fragment默认设置{@link com.leon.biuvideo.ui.views.SmartRefreshRecyclerView}为布局,所以不用指定布局文件
 */
public abstract class BaseSupportFragmentWithSrr<T> extends SupportFragment {
    protected Context context;
    protected BindingUtils bindingUtils;
    protected SmartRefreshRecyclerView<T> view;

    /**
     * 用于接收数据使用，并在主线程进行处理
     */
    protected Handler receiveDataHandler;

    public interface OnLoadListener {
        /**
         * 实现该方法，用于在主线程中处理数据
         *
         * @param msg   data
         */
        void onLoad(Message msg);
    }

    private OnLoadListener onLoadListener;

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();
        this.view = new SmartRefreshRecyclerView<>(context);
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
        receiveDataHandler = null;
        _mActivity.onBackPressed();
        onDestroy();
    }

    /**
     * 获取本布局中的某一个控件
     *
     * @param id  控件ID
     * @param <T> 继承View类的控件
     * @return 返回控件对象
     */
    protected <T extends View> T findView(@IdRes int id) {
        return view.findViewById(id);
    }
}