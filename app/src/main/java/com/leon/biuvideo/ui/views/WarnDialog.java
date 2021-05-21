package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.BindingUtils;

/**
 * @Author Leon
 * @Time 2020/12/30
 * @Desc 提醒/警告通用弹窗
 */
public class WarnDialog extends AlertDialog implements View.OnClickListener {
    private String title;
    private String content;

    private Window window;
    private final Context context;

    public WarnDialog(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    public WarnDialog(@NonNull Context context, String title, String content) {
        super(context);

        this.context = context;
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_warn);

        window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        initView();
    }

    private void initView() {
        window.setWindowAnimations(R.style.paning_anim_style);
        BindingUtils bindingUtils = new BindingUtils(window.getDecorView(), context);
        bindingUtils
                .setText(R.id.warn_dialog_textView_title, title)
                .setText(R.id.warn_dialog_textView_content, content)
                .setOnClickListener(R.id.warn_dialog_textView_confirm, this)
                .setOnClickListener(R.id.warn_dialog_textView_cancel, this);
    }

    private OnWarnActionListener onWarnActionListener;

    public interface OnWarnActionListener {
        /**
         * 确定键
         */
        void onConfirm();

        /**
         * 取消键
         */
        void onCancel();
    }

    public void setOnWarnActionListener(OnWarnActionListener onWarnActionListener) {
        this.onWarnActionListener = onWarnActionListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.warn_dialog_textView_cancel:
                if (onWarnActionListener != null) {
                    onWarnActionListener.onCancel();
                }
                break;
            case R.id.warn_dialog_textView_confirm:
                if (onWarnActionListener != null) {
                    onWarnActionListener.onConfirm();
                }
                break;
            default:
                break;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}