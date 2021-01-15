package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;

public class AddVideoDialog extends AlertDialog implements View.OnClickListener {
    private final Context context;

    private RecyclerView video_add_recyclerView;

    protected AddVideoDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_add_dialog);

        initView();
    }

    private void initView() {
        BindingUtils bindingUtils = new BindingUtils(getWindow().getDecorView(), context);
        bindingUtils
                .setOnClickListener(R.id.video_add_dialog_imageView_close, this)
                .setOnClickListener(R.id.video_add_dialog_imageView_add, this);

        video_add_recyclerView = findViewById(R.id.video_add_recyclerView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_add_dialog_imageView_close:
                dismiss();
                break;
            case R.id.video_add_dialog_imageView_add:

                break;
            default:
                break;
        }
    }
}
