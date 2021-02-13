package com.leon.biuvideo.ui.views;

import android.view.View;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.sun.easysnackbar.EasySnackBar;

public class SimpleSnackBar {
    public static final int LENGTH_SHORT = -1;
    public static final int LENGTH_LONG = 0;

    private final static int LAYOUT_ID = R.layout.simple_snackbar_layout;
    private final static int LAYOUT_WITH_ACTION_ID = R.layout.simple_snackbar_with_action_layout;

    /**
     * 创建一个Snackbar
     *
     * @param view  view
     * @param content   显示的文字内容
     * @param duration  显示时长
     */
    public static EasySnackBar make(View view, String content, int duration) {
        View convert = convertToContentView(view, LAYOUT_ID);
        ((TextView) convert.findViewById(R.id.simple_snackbar_content)).setText(content);

        return EasySnackBar.make(view, convert, duration, false);
    }

    /**
     * 创建带有点击事件的snackbar
     *
     * @param view  view
     * @param content   显示文字
     * @param action    点击事件文字
     * @param onClickListener   点击事件
     * @param duration  显示时长
     */
    public static EasySnackBar make(View view, String content, String action, View.OnClickListener onClickListener, int duration) {
        View convert = convertToContentView(view, LAYOUT_WITH_ACTION_ID);

        TextView textView_content = convert.findViewById(R.id.simple_snackbar_with_action_content);
        textView_content.setText(content);

        TextView text_View_action = convert.findViewById(R.id.simple_snackbar_with_action_action);
        text_View_action.setText(action);
        text_View_action.setOnClickListener(onClickListener);

        return EasySnackBar.make(view, convert, duration, false);
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
