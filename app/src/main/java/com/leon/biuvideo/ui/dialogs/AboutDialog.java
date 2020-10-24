package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.AboutDialogAdapter;
import com.leon.biuvideo.beans.AboutBean;

import java.util.List;

public class AboutDialog extends AlertDialog {
    private RecyclerView aboutDialog_recyclerView;
    private Button aboutDialog_button;
    private Context context;

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