package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.AboutDialogAdapter;
import com.leon.biuvideo.beans.AboutBean;

import java.util.List;

/**
 * 关于弹窗
 */
public class ThanksListDialog extends AlertDialog {
    private final Context context;
    private final List<AboutBean> aboutBeans;

    public ThanksListDialog(@NonNull Context context, List<AboutBean> aboutBeans) {
        super(context);
        this.context = context;
        this.aboutBeans = aboutBeans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_dialog);

        initView();
    }

    private void initView() {
        RecyclerView aboutDialog_recyclerView = findViewById(R.id.aboutDialog_recyclerView);
        aboutDialog_recyclerView.setAdapter(new AboutDialogAdapter(aboutBeans, context));

        TextView aboutDialog_textView_close = findViewById(R.id.aboutDialog_textView_close);
        aboutDialog_textView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //获取window
        Window window = this.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        //获取屏幕宽高
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (widthPixels * 0.9f);
        attributes.height = (int) (heightPixels * 0.9f);
    }
}