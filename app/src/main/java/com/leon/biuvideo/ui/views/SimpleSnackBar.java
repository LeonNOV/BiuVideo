package com.leon.biuvideo.ui.views;

import android.view.View;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.sun.easysnackbar.EasySnackBar;

public class SimpleSnackBar {
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    public static final int LENGTH_INDEFINITE = -2;

    public static EasySnackBar make(View view, int resId, int duration) {
        return make(view, view.getResources().getText(resId).toString(), duration);
    }

    /**
     * 创建一个Snackbar
     *
     * @param view  view
     * @param content   显示的文字内容
     * @param duration  显示时长
     */
    public static EasySnackBar make(View view, CharSequence content, int duration) {
        View convert = convertToContentView(view, R.layout.simple_snackbar_layout);
        ((TextView) convert.findViewById(R.id.simple_snackbar_content)).setText(content);

        return EasySnackBar.make(view, convert, duration, false);
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

        TextView text_View_action = view.findViewById(R.id.simple_snackbar_action);
        text_View_action.setVisibility(View.VISIBLE);
        text_View_action.setText(action);
        text_View_action.setOnClickListener(onClickListener);

        return easySnackBar;
    }

    /**
     * 将自定义layout文件转换为view对象
     *
     * @param view  view
     * @return  返回自定义layout的view对象
     */
    private static View convertToContentView(View view, int layout) {
        return EasySnackBar.convertToContentView(view, layout);
    }
}
