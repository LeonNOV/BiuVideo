package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 开源声明弹窗
 */
public class LicenseDialog extends AlertDialog {
    public LicenseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_dialog);

        initView();
    }

    private void initView() {
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView license_dialog_textView = findViewById(R.id.license_dialog_textView);
        license_dialog_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //获取LICENSE内容
        String content = readLicense();
        System.out.println(content);
        license_dialog_textView.setText(content);
    }

    /**
     * 读取根目录下的LICENSE文件
     *
     * @return
     */
    private String readLicense() {
        try {
            InputStream inputStream = getContext().getAssets().open("LICENSE");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String temp;
            StringBuilder content = new StringBuilder();
            while ((temp = bufferedReader.readLine()) != null) {
                content.append(temp + "\n");
            }

            bufferedReader.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
