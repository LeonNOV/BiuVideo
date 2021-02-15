package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.orderBeans.LocalVideoFolder;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.InitValueUtils;

public class NewVideoFolderDialog extends AlertDialog implements View.OnClickListener {
    private final Context context;
    private Window window;

    private EditText editText;

    protected NewVideoFolderDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    private OnConfirmListener onConfirmListener;

    public interface OnConfirmListener {
        void onConfirm(LocalVideoFolder localVideoFolder);
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_video_folder);

        window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        initView();
    }

    private void initView() {
        editText = findViewById(R.id.new_video_folder_dialog_editText_value);

        BindingUtils bindingUtils = new BindingUtils(window.getDecorView(), context);
        bindingUtils
                .setOnClickListener(R.id.new_video_folder_dialog_textView_cancel, this)
                .setOnClickListener(R.id.new_video_folder_dialog_textView_confirm, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_video_folder_dialog_textView_confirm:
                String folderName = editText.getText().toString();
                if (folderName.equals("")) {

                    // 显示有问题，暂用Toast显示
                    Toast.makeText(context, "请输入完整的文件名称", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (onConfirmListener != null) {
                        LocalVideoFolder localVideoFolder = new LocalVideoFolder();
                        localVideoFolder.videoCount = 0;
                        localVideoFolder.folderName = folderName;
                        localVideoFolder.creator = String.valueOf(InitValueUtils.getUID(context));
                        localVideoFolder.createTime = System.currentTimeMillis();
                        onConfirmListener.onConfirm(localVideoFolder);
                        dismiss();
                    }
                }

                break;
            case R.id.new_video_folder_dialog_textView_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        super.dismiss();
    }
}
