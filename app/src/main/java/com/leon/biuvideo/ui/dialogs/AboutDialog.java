package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.AboutDialogAdapter;
import com.leon.biuvideo.beans.AboutBean;

import java.util.List;

/**
 * 关于弹窗
 */
public class AboutDialog extends AlertDialog {
    private RecyclerView aboutDialog_recyclerView;
    private Button aboutDialog_button;
    private final Context context;

    private List<AboutBean> aboutBeans;

    public AboutDialog(@NonNull Context context, List<AboutBean> aboutBeans) {
        super(context);
        this.context = context;
        this.aboutBeans = aboutBeans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_dialog);

        initView();
        initEvent();
    }

    private void initView() {
        aboutDialog_recyclerView = findViewById(R.id.aboutDialog_recyclerView);
        aboutDialog_button = findViewById(R.id.aboutDialog_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        AboutDialogAdapter dialogAdapter = new AboutDialogAdapter(aboutBeans, context);

        aboutDialog_recyclerView.setLayoutManager(layoutManager);
        aboutDialog_recyclerView.setAdapter(dialogAdapter);

        //获取window
        Window window = this.getWindow();

        //添加缩放动画
        window.setWindowAnimations(R.style.music_list_dialog);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        //获取屏幕宽高
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (widthPixels * 0.8f);
        attributes.height = (int) (heightPixels * 0.8f);
    }

    private void initEvent() {
        aboutDialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onCloseClick();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }

    public interface OnClickBottomListener {
        void onCloseClick();
    }

    public OnClickBottomListener onClickBottomListener;

    public void setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
    }
}