package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.sun.easysnackbar.EasySnackBar;

/**
 * @Author Leon
 * @Time 2021/2/13
 * @Desc 一个简单的SnackBar
 */
public class SimpleSnackBar {
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    public static final int LENGTH_INDEFINITE = -2;

    public static EasySnackBar make(View view, CharSequence content, int duration) {
        return setAnimation(makeSimpleSnackBar(view, content, Gravity.START, duration));
    }

    public static EasySnackBar make(View view, CharSequence content, int contentGravity, int duration) {
        return setAnimation(makeSimpleSnackBar(view, content, contentGravity, duration));
    }

    /**
     * 创建一个SnackBar
     *
     * @param view  view
     * @param content   显示的文字内容
     * @param duration  显示时长
     */
    public static EasySnackBar makeSimpleSnackBar(View view, CharSequence content, int contentGravity, int duration) {
        View convert = convertToContentView(view);

        TextView simpleSnackbarContent = convert.findViewById(R.id.simple_snackbar_content);
        simpleSnackbarContent.setText(content);
        simpleSnackbarContent.setGravity(contentGravity);

        return setAnimation(EasySnackBar.make(view, convert, duration, false));
    }

    /**
     * 设置监听事件
     *
     * @param easySnackBar  easySnackBar
     * @param action    action文本
     * @param onClickListener   点击事件
     */
    public static EasySnackBar setAction(EasySnackBar easySnackBar,String action, View.OnClickListener onClickListener) {
        View view = easySnackBar.getView();

        TextView textViewAction = view.findViewById(R.id.simple_snackbar_action);
        textViewAction.setVisibility(View.VISIBLE);
        textViewAction.setText(action);
        textViewAction.setOnClickListener(onClickListener);

        return easySnackBar;
    }

    /**
     * 将自定义layout文件转换为view对象
     *
     * @param view  view
     * @return  返回自定义layout的view对象
     */
    private static View convertToContentView(View view) {
        return EasySnackBar.convertToContentView(view, R.layout.simple_snackbar_layout);
    }

    private static EasySnackBar setAnimation(EasySnackBar easySnackBar) {
        // 由于EasySnackBar的父类BaseTransientBottomBar会检测无障碍服是否开启，该服务如果开启则没有动画，没有开启则有动画
        // 此处则是如果开启无障碍，则使用此处的动画，未开启，则使用自带动画
        AccessibilityManager mAccessibilityManager = (AccessibilityManager) easySnackBar.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        boolean enabled = mAccessibilityManager.isEnabled();
        if (enabled) {
            Animation enterAnimation = AnimationUtils.loadAnimation(easySnackBar.getContext(), R.anim.simple_snackbar_anim_enter);
            Animation exitAnimation = AnimationUtils.loadAnimation(easySnackBar.getContext(), R.anim.simple_snackbar_anim_exit);

            View view = easySnackBar.getView();
            easySnackBar.addCallback(new EasySnackBar.Callback() {
                @Override
                public void onShown(EasySnackBar sb) {
                    view.startAnimation(enterAnimation);
                }

                @Override
                public void onDismissed(EasySnackBar transientBottomBar, int event) {
                    view.startAnimation(exitAnimation);
                }
            });
        }

        return easySnackBar;
    }
}
