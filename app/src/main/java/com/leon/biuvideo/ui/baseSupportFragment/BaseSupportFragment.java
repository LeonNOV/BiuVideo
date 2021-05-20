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
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.otherFragments.biliUserFragments.BiliUserFragment;
import com.leon.biuvideo.ui.resourcesFragment.article.ArticleFragment;
import com.leon.biuvideo.ui.resourcesFragment.audio.AudioFragment;
import com.leon.biuvideo.ui.resourcesFragment.picture.PictureFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.FragmentType;
import com.weikaiyun.fragmentation.SupportFragment;

import org.jetbrains.annotations.NotNull;

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

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.context = context;
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
        this.view = inflater.inflate(setLayout(), container, false);
        this.view.setBackgroundColor(context.getColor(R.color.bg));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    public MainActivity getMainActivity () {
        return (MainActivity) _mActivity;
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();

    public Handler getReceiveDataHandler() {
        return receiveDataHandler;
    }

    /**
     * 启动一个“公共”Fragment
     *
     * @param fragmentType  FragmentType
     * @param params 参数
     */
    public void startPublicFragment (FragmentType fragmentType, String ... params) {
        if (InternetUtils.checkNetwork(_mActivity.getWindow().getDecorView())) {
            NavFragment navFragment = getMainActivity().findFragment(NavFragment.class);
            switch (fragmentType) {
                case BILI_USER:
                    navFragment.startBrotherFragment(new BiliUserFragment(params[0]));
                    break;
                case VIDEO:
                    navFragment.startBrotherFragment(new VideoFragment(params[0]));
                    break;
                case BANGUMI:
                    if (params.length > 1) {
                        navFragment.startBrotherFragment(new VideoFragment(params[0], params[1]));
                    } else {
                        navFragment.startBrotherFragment(new VideoFragment(params[0], null));
                    }
                    break;
                case AUDIO:
                    navFragment.startBrotherFragment(new AudioFragment(params[0]));
                    break;
                case ARTICLE:
                    navFragment.startBrotherFragment(new ArticleFragment(params[0]));
                    break;
                case PICTURE:
                    navFragment.startBrotherFragment(new PictureFragment(params[0]));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获取本布局中的某一个控件
     *
     * @param id    控件ID
     * @param <T>   继承View类的控件
     * @return  返回控件对象
     */
    public <T extends View> T findView(@IdRes int id) {
        return view.findViewById(id);
    }

    /**
     * 为SimpleTopBar设置返回事件
     */
    protected void setTopBar(@IdRes int topBarId) {
        ((SimpleTopBar) findView(topBarId)).setBackListener(this::backPressed);
    }

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
}