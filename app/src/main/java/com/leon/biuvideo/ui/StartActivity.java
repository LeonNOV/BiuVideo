package com.leon.biuvideo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;

/**
 * @Author Leon
 * @Time 2021/3/15
 * @Desc
 */
public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_start);

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 两秒后进入主页
                try {
                    Thread.sleep(2000);

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
