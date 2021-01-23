package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;

/**
 * 导入关注列表弹窗
 */
public class ImportFollowDialog extends AlertDialog implements View.OnClickListener {
    private EditText import_follow_editText;
    private LinearLayout import_follow_linearLayout_cookie;
    private EditText import_follow_editText_cookie;

    private boolean expansionState = true;

    public ImportFollowDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_follow_dialog);

        initView();
    }

    private void initView() {
        import_follow_editText = findViewById(R.id.import_follow_editText);

        Button import_follow_button_cancel = findViewById(R.id.import_follow_button_cancel);
        import_follow_button_cancel.setOnClickListener(this);

        Button import_follow_button_confirm = findViewById(R.id.import_follow_button_confirm);
        import_follow_button_confirm.setOnClickListener(this);

        ImageView import_follow_imageView_expansion = findViewById(R.id.import_follow_imageView_expansion);
        import_follow_imageView_expansion.setOnClickListener(this);

        import_follow_linearLayout_cookie = findViewById(R.id.import_follow_linearLayout_cookie);
        import_follow_editText_cookie = findViewById(R.id.import_follow_editText_cookie);

        //获取window
        Window window = this.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        //点击输入框弹出软键盘
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.import_follow_button_confirm:
                //获取输入内容
                String mid = import_follow_editText.getText().toString().trim();

                if (mid.equals("")) {
                    Toast.makeText(getContext(), "请输入正确的用户ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (priorityListener != null) {
                    try {
                        //将输入内容转换为long类型
                        long vmid = Long.parseLong(mid);
                        String cookie = import_follow_editText_cookie.getText().toString();

                        priorityListener.setActivityText(vmid, cookie.equals("") ? null : cookie);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "输入内容需要为全数字，请检查输入内容", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.import_follow_imageView_expansion:
                if (expansionState) {
                    import_follow_linearLayout_cookie.setVisibility(View.VISIBLE);
                    expansionState = false;
                } else {
                    import_follow_linearLayout_cookie.setVisibility(View.GONE);
                    expansionState = true;
                }

                break;
            case R.id.import_follow_button_cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 将mid回传给PreferenceActivity
     */
    public interface PriorityListener {
        void setActivityText(long mid, String cookie);
    }

    private PriorityListener priorityListener;

    public void setPriorityListener(PriorityListener priorityListener) {
        this.priorityListener = priorityListener;
    }
}