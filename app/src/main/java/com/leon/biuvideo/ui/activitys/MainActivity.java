package com.leon.biuvideo.ui.activitys;

import android.view.*;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.ui.dialogs.AboutDialog;
import com.leon.biuvideo.ui.fragments.FavoriteFragment;
import com.leon.biuvideo.ui.fragments.HomeFragment;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private Toolbar main_toolBar;
    private ImageButton toolBar_imageButton_menu, navigation_imageButton_back;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initView();

        //设置网络
        initInternet();
    }

    private void initView() {
        drawer_layout = findViewById(R.id.drawer_layout);

        navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);

        main_toolBar = findViewById(R.id.main_toolBar);

        //添加菜单栏
        main_toolBar.inflateMenu(R.menu.more_menu);

        //设置菜单栏条目的监听
        main_toolBar.setOnMenuItemClickListener(this);

        //设置菜单按钮图标
        main_toolBar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.more_icon));

        toolBar_imageButton_menu = findViewById(R.id.toolBar_imageButton_menu);
        toolBar_imageButton_menu.setOnClickListener(this);

        navigation_imageButton_back = navigation_view.getHeaderView(0).findViewById(R.id.navigation_imageButton_back);
        navigation_imageButton_back.setOnClickListener(this);
    }

    /**
     * 设置网络
     */
    private void initInternet() {
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

    //侧滑菜单栏的监听
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                switchFragment(new HomeFragment());
                break;
            case R.id.item2:
                switchFragment(new FavoriteFragment());
                break;
//            case R.id.item3:
//                switchFragment(new HomeFragment());
//                Toast.makeText(this, "点击了第三个条目", Toast.LENGTH_SHORT).show();
//                break;
            default: break;
        }
        drawer_layout.closeDrawer(navigation_view);

        return true;
    }

    /**
     * fragment的切换
     *
     * @param targetFragment    要切换到的fragment
     */
    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if(currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.main_fragment, targetFragment);
            transaction.commit();
        } else {
            transaction.hide(currentFragment).show(targetFragment).commit();
        }

        currentFragment = targetFragment;
    }

    //设置main中的打开侧滑菜单按钮的监听和侧滑菜单中的返回按钮
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

                //设置Dialog显示内容
                ArrayList<AboutBean> aboutBeans = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    AboutBean aboutBean = new AboutBean();

                    aboutBean.title = "标题" + i;
                    aboutBean.license = "许可许可许可" + i;
                    aboutBean.desc = "内容内容内容内容内容内容内容内容" + i;

                    aboutBeans.add(aboutBean);
                }

                //显示Dialog
                AboutDialog aboutDialog = new AboutDialog(MainActivity.this, aboutBeans);
                aboutDialog.setOnClickBottomListener(new AboutDialog.OnClickBottomListener() {
                    @Override
                    public void onCloseClick() {
                        aboutDialog.dismiss();
                    }
                });

                aboutDialog.show();
                break;
        }

        return true;
    }
}