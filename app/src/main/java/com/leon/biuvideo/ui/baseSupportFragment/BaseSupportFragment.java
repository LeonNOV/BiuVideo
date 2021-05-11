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
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.otherFragments.biliUserFragments.BiliUserFragment;
import com.leon.biuvideo.ui.resourcesFragment.article.ArticleFragment;
import com.leon.biuvideo.ui.resourcesFragment.audio.AudioFragment;
import com.leon.biuvideo.ui.resourcesFragment.picture.PictureFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.contribution.VideoFragment;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.values.FragmentType;

import me.yokeyword.fragmentation.SupportActivity;
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
     * 用于接收数据使用，并在主线程进行处理
     */
    protected Handler receiveDataHandler;

    protected OnLoadListener onLoadListener;

    public interface OnLoadListener {
        /**
         * 实现该方法，用于在主线程中处理数据
         *
         * @param msg   data
         */
        void onLoad(Message msg);
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    /**
     * 设置LayoutId
     *
     * @return  LayoutId
     */
    protected abstract @LayoutRes int setLayout();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();
        this.view = inflater.inflate(setLayout(), container, false);
        this.view.setBackgroundColor(context.getColor(R.color.bg));
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

    @Override
    public boolean onBackPressedSupport() {
        onDestroy();
        return super.onBackPressedSupport();
    }

    public Handler getReceiveDataHandler() {
        return receiveDataHandler;
    }

    /**
     * 启动一个“公共”Fragment
     *
     * @param fragmentType  FragmentType
     * @param params 参数
     */
    public void startPublicFragment (FragmentType fragmentType, String params) {
        // 获取栈顶的SupportFragment
        SupportFragment topFragment = (SupportFragment) ((SupportActivity) getActivity()).getTopFragment();
        switch (fragmentType) {
            case BILI_USER:
                topFragment.start(new BiliUserFragment(params));
                break;
            case VIDEO:
                topFragment.start(new VideoFragment(params));
                break;
            case AUDIO:
                topFragment.start(new AudioFragment(params));
                break;
            case ARTICLE:
                topFragment.start(new ArticleFragment(params));
                break;
            case PICTURE:
                topFragment.start(new PictureFragment(params));
                break;
            default:
                break;
        }
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
