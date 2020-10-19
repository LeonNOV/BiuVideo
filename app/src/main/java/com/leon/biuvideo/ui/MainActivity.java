package com.leon.biuvideo.ui;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.view.ViewPage;
import com.leon.biuvideo.utils.HeroImages;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.parseUtils.ViewParseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "LeonLogCat-blue";
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private ImageView hero_imageView;
    private ImageButton biu_button_menu;
    private EditText main_editText_value;
    private Button main_button_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initView();
        initValues();
    }

    private void initView() {
        drawer_layout = findViewById(R.id.drawer_layout);

        navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);

        biu_button_menu = findViewById(R.id.biu_button_menu);
        biu_button_menu.setOnClickListener(this);

        hero_imageView = findViewById(R.id.hero_imageView);

        main_editText_value = findViewById(R.id.main_editText_value);

        main_button_confirm = findViewById(R.id.main_button_confirm);
        main_button_confirm.setOnClickListener(this);
    }

    private void initValues() {
        //设置每天打开的hero
        setHero();

        //设置网络
        setInternet();
    }

    /**
     * 设置网络
     */
    private void setInternet() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    /**
     * 设置hero
     */
    private void setHero() {
        SharedPreferences initValues = getSharedPreferences("initValues", MODE_PRIVATE);
        SharedPreferences.Editor edit = initValues.edit();

        //获取当前时间
        long startTime = initValues.getLong("startTime", 0);
        int heroIndex = initValues.getInt("heroIndex", 0);

        //判断是否为第一次启动
        if (startTime == 0 && heroIndex == 0) {
            heroIndex = 0;
        } else {
            //判断是否已过去一天，如果已过则设置新的hero
            if (System.currentTimeMillis() - startTime >= 86400000) {
                //达到最后一个则将index重置为0
                if (heroIndex == HeroImages.heroImages.length - 1) {
                    heroIndex = 0;
                } else {
                    //设置新的index
                    heroIndex++;
                }
            }
        }

        hero_imageView.setImageResource(HeroImages.heroImages[heroIndex]);
        edit.putLong("startTime", getTime());
        edit.putInt("heroIndex", heroIndex);
        edit.apply();
    }

    /**
     * 获取的年月日
     *
     * @return  返回年月日
     */
    private long getTime() {
        Date nowDate = new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(nowDate);

        try {
            Date ymd_long = sdf.parse(ymd);
            return ymd_long.getTime();
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "时间解析出错", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.biu_button_menu:
                drawer_layout.openDrawer(Gravity.LEFT);
                break;
            case R.id.navigation_imageButton_back:
                drawer_layout.closeDrawer(navigation_view);
                break;
            case R.id.main_button_confirm:
                String value = main_editText_value.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("bvid", value);

                String response = HttpUtils.GETByParam(Paths.view, map);
                ViewPage viewPage = ViewParseUtils.parseView(response);

//                Log.d(TAG, "onClick: " + viewPage.toString());

                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                intent.putExtra("viewPage", viewPage);
                startActivity(intent);
                this.finish();

                break;
            default:break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "点击了item1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "点击了item2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item3:
                Toast.makeText(getApplicationContext(), "点击了item3", Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
        return true;
    }
}