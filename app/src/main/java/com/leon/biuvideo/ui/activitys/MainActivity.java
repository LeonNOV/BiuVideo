package com.leon.biuvideo.ui.activitys;

import android.content.Intent;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.mainFragments.FavoriteFragment;
import com.leon.biuvideo.ui.fragments.mainFragments.HomeFragment;
import com.leon.biuvideo.ui.fragments.mainFragments.PlayListFragment;
import com.leon.biuvideo.ui.views.RoundPopupWindow;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.InternetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 主activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private ImageView toolBar_imageView_menu, navigation_imageView_back, toolBar_imageView_more;
    private InternetUtils.InternetState internetState;

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
        //检查网络状态
        internetState = InternetUtils.internetState(getApplicationContext());
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

        // 获取权限
        FileUtils.verifyPermissions(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        drawer_layout = findViewById(R.id.drawer_layout);

        navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);

        toolBar_imageView_menu = findViewById(R.id.toolBar_imageView_menu);
        toolBar_imageView_menu.setOnClickListener(this);

        toolBar_imageView_more = findViewById(R.id.toolBar_imageView_more);
        toolBar_imageView_more.setOnClickListener(this);

        navigation_imageView_back = navigation_view.getHeaderView(0).findViewById(R.id.navigation_imageView_back);
        navigation_imageView_back.setOnClickListener(this);
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
     * @param item 监听的item
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
            default:
                break;
        }
        drawer_layout.closeDrawer(navigation_view);

        return true;
    }

    /**
     * fragment的切换
     *
     * @param fragment 要添加的fragment
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
            case R.id.toolBar_imageView_menu:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.toolBar_imageView_more:
                RoundPopupWindow roundPopupWindow = new RoundPopupWindow(getApplicationContext(), toolBar_imageView_more);
                roundPopupWindow
                        .setContentView(R.layout.main_popup_window)
                        .setOnClickListener(R.id.main_more_help, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "help", Toast.LENGTH_SHORT).show();
                                roundPopupWindow.dismiss();
                            }
                        })
                        .setOnClickListener(R.id.main_more_preference, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转至PreferenceActivity
                                Intent intent = new Intent(MainActivity.this, PreferenceActivity.class);
                                startActivity(intent);

                                roundPopupWindow.dismiss();
                            }
                        })
                        .setLocation(RoundPopupWindow.SHOW_AS_DROP_DOWN)
                        .create();

                        break;
            case R.id.navigation_imageView_back:
                drawer_layout.closeDrawer(navigation_view);
                break;
            default:
                break;
        }
    }
}