package com.leon.biuvideo.ui.activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.*;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.ui.dialogs.AboutDialog;
import com.leon.biuvideo.ui.fragments.FavoriteFragment;
import com.leon.biuvideo.ui.fragments.HomeFragment;
import com.leon.biuvideo.ui.fragments.PlayListFragment;
import com.leon.biuvideo.utils.InternetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 主activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private Toolbar main_toolBar;
    private ImageButton toolBar_imageButton_menu, navigation_imageButton_back;

    private List<Fragment> fragmentList;

    private Fragment homeFragment;
    private Fragment favoriteFragment;
    private Fragment playListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //初始化
        init();

        initView();

        //初始化网络
        initInternet();
    }

    /**
     * app参数初始化
     */
    private void init() {
        //检查权限有没有获取
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //创建对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("读写权限");
                builder.setMessage("由于保存媒体资源时需要用到\"读写权限\"，否则将无法正常使用");
                builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "请自行开启对应权限，否则应用将无法正常使用", Toast.LENGTH_SHORT).show();
                        builder.create().dismiss();
                    }
                });

                builder.create().show();
            } else {
                //申请读写权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
            }
        }

        //检查网络状态
        InternetUtils.InternetState internetState = InternetUtils.InternetState(getApplicationContext());
        switch (internetState) {
            case INTERNET_NoAvailable:
                Toast.makeText(getApplicationContext(), "没有网络哦~~~", Toast.LENGTH_SHORT).show();
                break;
//            case INTERNET_WIFI:
//                Toast.makeText(getApplicationContext(), "WIFI", Toast.LENGTH_SHORT).show();
//                break;
            case INTERNET_MOBILE:
                Toast.makeText(getApplicationContext(), "现处于移动网络状态下，请注意流量的使用", Toast.LENGTH_SHORT).show();
                break;
            default:
//                Toast.makeText(getApplicationContext(), "未知类型", Toast.LENGTH_SHORT).show();
                break;
        }

        //初始化fragment
        fragmentList = new ArrayList<>();
        //默认显示HomeFragment
        fragmentList.add(new HomeFragment());
    }

    /**
     * 初始化控件
     */
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
     * 初始化网络
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

    /**
     * 侧滑菜单栏的监听
     *
     * @param item  监听的item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:

                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }

                switchFragment(homeFragment);
                break;
            case R.id.item2:

                if (favoriteFragment == null) {
                    favoriteFragment = new FavoriteFragment();
                }

                switchFragment(favoriteFragment);
                break;
            case R.id.item3:

                if (playListFragment == null) {
                    playListFragment = new PlayListFragment();
                }

                switchFragment(playListFragment);
                break;
            default: break;
        }
        drawer_layout.closeDrawer(navigation_view);

        return true;
    }

    /**
     * fragment的切换
     *
     * @param fragment  要添加的fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //判断该fragment是否已经被添加过
        if (!fragment.isAdded()) {
            //添加到 fragmentList
            fragmentList.add(fragment);
            fragmentTransaction.add(R.id.main_fragment, fragment).commit();
        } else {
            for (Fragment frag : fragmentList) {
                if (frag != fragment) {
                    //隐藏其他fragment
                    fragmentTransaction.hide(frag);
                } else {
                    fragmentTransaction.show(fragment);
                }
            }

            fragmentTransaction.commit();
        }
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

    /**
     * 权限回调
     *
     * @param requestCode   请求码
     * @param permissions   文件读写权限
     * @param grantResults  授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1024) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "权限申请成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}