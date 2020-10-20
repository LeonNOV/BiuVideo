package com.leon.biuvideo.ui;

import android.content.Context;
import android.view.*;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.FavoriteFragment;
import com.leon.biuvideo.ui.fragments.VideoFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {
    private static final String TAG = "LeonLogCat-blue";

    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private Toolbar main_toolBar;
    private ImageButton toolBar_imageButton_menu, navigation_imageButton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initView();
        initValues();
    }

    private void initView() {

        drawer_layout = findViewById(R.id.drawer_layout);

        navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);

        main_toolBar = findViewById(R.id.main_toolBar);
        main_toolBar.inflateMenu(R.menu.more_menu);
        main_toolBar.setOnMenuItemClickListener(this);
        main_toolBar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.more_icon));

        toolBar_imageButton_menu = findViewById(R.id.toolBar_imageButton_menu);
        toolBar_imageButton_menu.setOnClickListener(this);

        navigation_imageButton_back = navigation_view.getHeaderView(0).findViewById(R.id.navigation_imageButton_back);
        navigation_imageButton_back.setOnClickListener(this);
    }

    private void initValues() {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.item1:
                fragment = new VideoFragment();
                break;
            case R.id.item2:
                fragment = new FavoriteFragment();
                break;
            case R.id.item3:
                fragment = new FavoriteFragment();
                Toast.makeText(this, "点击率第三个条目", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        drawer_layout.closeDrawer(navigation_view);

        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolBar_imageButton_menu:
                drawer_layout.openDrawer(Gravity.LEFT);
                break;
            case R.id.navigation_imageButton_back:
                drawer_layout.closeDrawer(navigation_view);
                break;
        }
    }

    /**
     * 选项菜单栏监听
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.more_menu_about:
                Toast.makeText(this, "点击了菜单栏的关于", Toast.LENGTH_SHORT).show();

                //显示Dialog

                break;
        }

        return true;
    }
}