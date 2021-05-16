package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;

/**
 * @Author Leon
 * @Time 2021/5/16
 * @Desc 开屏弹窗
 */
public class OpenScreenDialog extends AlertDialog {
    private final Context context;

    public OpenScreenDialog(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_screen_dialog);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setWindowAnimations(R.style.paning_anim_style);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);

        initView();
    }

    private void initView() {
        WebView openScreenDesc = findViewById(R.id.open_screen_desc);
        findViewById(R.id.open_screen_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle data = msg.getData();

                String encodedHtml = data.getString("desc", "出错了~~~");
                openScreenDesc.loadData(encodedHtml, "text/html", "base64");

                return true;
            }
        });

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                String encodedHtml = Base64.encodeToString(FileUtils.getAssetsContent(context, "openScreenDesc.html").getBytes(), Base64.NO_PADDING);

                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("desc", encodedHtml);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }
}
