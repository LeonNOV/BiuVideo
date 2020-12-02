package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;

/**
 * 导入关注列表时显示的弹窗
 */
public class ImportStateDialog extends AlertDialog {
    public static final String STR_SUCCESS = "导入成功";
    public static final String STR_ERROR = "导入失败";
    public static final int IMG_SUCCESS = R.drawable.icon_success;
    public static final int IMG_ERROR = R.drawable.icon_error;

    private ImageView waiting_dialog_imageView_import_state;
    private TextView waiting_dialog_textView_import_str_state;

    public ImportStateDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_state_dialog);

        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        waiting_dialog_imageView_import_state = findViewById(R.id.import_state_dialog_imageView_import_state);
        waiting_dialog_textView_import_str_state = findViewById(R.id.import_state_dialog_textView_import_str_state);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * 根据导入状态修改image和str
     *
     * @param state 导入状态；true：导入成功；false：导入失败
     */
    public void setResourceState(boolean state) {
        if (state) {
            waiting_dialog_imageView_import_state.setImageResource(IMG_SUCCESS);
            waiting_dialog_textView_import_str_state.setText(STR_SUCCESS);
        } else {
            waiting_dialog_imageView_import_state.setImageResource(IMG_ERROR);
            waiting_dialog_textView_import_str_state.setText(STR_ERROR);
        }

        waiting_dialog_imageView_import_state.setVisibility(View.VISIBLE);
        waiting_dialog_textView_import_str_state.setVisibility(View.VISIBLE);
    }
}