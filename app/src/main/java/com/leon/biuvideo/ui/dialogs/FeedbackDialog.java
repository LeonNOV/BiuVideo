package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.Fuck;

public class FeedbackDialog extends AlertDialog {

    public FeedbackDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back_dialog);

        initView();
    }

    private void initView() {
        EditText feed_back_editText = findViewById(R.id.feed_back_editText);
        Button feed_back_button_submit = findViewById(R.id.feed_back_button_submit);
        feed_back_button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedBack = feed_back_editText.getText().toString();
                if (!feedBack.equals("")) {
                    //处理反馈的提交
                    Fuck.blue(feedBack);
                    dismiss();
                    Toast.makeText(getContext(), "感谢您的反馈", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "不反馈点儿啥吗?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Window window = this.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }
}
