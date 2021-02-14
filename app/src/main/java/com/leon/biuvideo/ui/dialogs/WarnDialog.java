package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;

/**
 * 提醒/警告通用弹窗
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
        setContentView(R.layout.warn_dialog);

        window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        initView();
    }

    private void initView() {
        BindingUtils bindingUtils = new BindingUtils(window.getDecorView(), context);
        bindingUtils
                .setText(R.id.warn_dialog_textView_title, title)
                .setText(R.id.warn_dialog_textView_content, content)
                .setOnClickListener(R.id.warn_dialog_textView_confirm, this)
                .setOnClickListener(R.id.warn_dialog_textView_cancel, this);
    }

    private OnConfirmListener onConfirmListener;

    public interface OnConfirmListener {
        void onConfirm();
        void onCancel();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.warn_dialog_textView_cancel:
                if (onConfirmListener != null) {
                    onConfirmListener.onCancel();
                }
                break;
            case R.id.warn_dialog_textView_confirm:
                if (onConfirmListener != null) {
                    onConfirmListener.onConfirm();
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