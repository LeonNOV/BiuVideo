package com.leon.biuvideo.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ChooseThemeColorAdapter;
import com.leon.biuvideo.values.ThemeColors;

import java.util.ArrayList;
import java.util.List;

public class ChooseThemeColorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_theme_color);

        initView();
        initValues();
    }

    private void initView() {
        recyclerView = findViewById(R.id.choose_theme_color_recyclerView);
        ImageView choose_theme_color_close = findViewById(R.id.choose_theme_color_close);
        choose_theme_color_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initValues() {
        Context context = getApplicationContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // 获取颜色值及名称
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < ThemeColors.colorNames.length; i++) {
            colors.add(ThemeColors.colorNames[i] + "|" + ThemeColors.colorIds[i]);
        }

        ChooseThemeColorAdapter chooseThemeColorAdapter = new ChooseThemeColorAdapter(colors, context);
        chooseThemeColorAdapter.setColorItemClickListener(new ChooseThemeColorAdapter.ColorItemClickListener() {
            @Override
            public void onClick(int position) {
                // 修改initValues中的数据
                SharedPreferences initValue = getSharedPreferences(context.getResources().getString(R.string.preference), Context.MODE_PRIVATE);
                initValue.edit().putInt("themeColorPosition", position).apply();

                // 发送本地广播修改主题色
                sendLocalBroadcast(position);
            }
        });

        recyclerView.setAdapter(chooseThemeColorAdapter);
    }

    private void sendLocalBroadcast (int position) {
        if (localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        }

        Intent mediaIntent;
        mediaIntent = new Intent("themeColorChanged");
        mediaIntent.putExtra("position", position);

        localBroadcastManager.sendBroadcast(mediaIntent);
    }
}
