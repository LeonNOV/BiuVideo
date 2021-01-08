package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;

/**
 * 提醒/警告通用弹窗
 */
public class WarnDialog extends AlertDialog implements View.OnClickListener {
    private String title;
    private String content;

    public WarnDialog(@NonNull Context context) {
        super(context);
    }

    public WarnDialog(@NonNull Context context, String title, String content) {
        super(context);

        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warn_dialog);

        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        initView();
    }

    private void initView() {
        TextView logout_textView_cancel = findViewById(R.id.warn_dialog_textView_confirm);
        logout_textView_cancel.setOnClickListener(this);

        TextView logout_textView_submit = findViewById(R.id.warn_dialog_textView_cancel);
        logout_textView_submit.setOnClickListener(this);

        TextView warn_dialog_textView_title = findViewById(R.id.warn_dialog_textView_title);
        warn_dialog_textView_title.setText(title);

        TextView warn_dialog_textView_content = findViewById(R.id.warn_dialog_textView_content);
        warn_dialog_textView_content.setText(content);
    }

    private OnConfirmListener onConfirmListener;

    public interface OnConfirmListener {
        void onConfirm();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.warn_dialog_textView_cancel:
                dismiss();
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