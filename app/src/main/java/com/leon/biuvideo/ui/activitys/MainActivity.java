package com.leon.biuvideo.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.ui.dialogs.AboutBiuVideoDialog;
import com.leon.biuvideo.ui.dialogs.WarnDialog;
import com.leon.biuvideo.ui.fragments.mainFragments.FavoriteFragment;
import com.leon.biuvideo.ui.fragments.mainFragments.HistoryFragment;
import com.leon.biuvideo.ui.fragments.mainFragments.HomeFragment;
import com.leon.biuvideo.ui.fragments.mainFragments.OrderFragment;
import com.leon.biuvideo.ui.fragments.mainFragments.LocalOrderFragment;
import com.leon.biuvideo.ui.fragments.mainFragments.PreferenceFragment;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.UserInfoParser;
import com.leon.biuvideo.values.Tables;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 主activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;

    private ImageView navigation_header_user_face;
    private ImageView navigation_header_vip_state;
    private TextView
            navigation_header_user_name,
            navigation_header_user_level,
            navigation_header_user_ex,
            navigation_header_b_coin1,
            navigation_header_b_coin2,
            navigation_header_vip_due_date;
    private ProgressBar navigation_header_progress_level;

    //登录标识，true：已登录；false：未登录
    private boolean isLogin;
    private String cookie;//cookie信息
    private UserInfo userInfo;

    private FragmentTransaction fragmentTransaction;
    private List<Fragment> fragmentList;

    private Fragment homeFragment;
    private Fragment favoriteFragment;
    private Fragment localOrderFragment;
    private Fragment historyFragment;
    private Fragment preferenceFragment;
    private Fragment orderFragment;

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

        //初始化用户数据，在有cookie的情况下
        initUserInfo();
    }

    /**
     * app参数初始化
     */
    private void init() {
        //检查网络状态
        InternetUtils.InternetState internetState = InternetUtils.internetState(getApplicationContext());
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
        homeFragment = new HomeFragment();
        fragmentList.add(homeFragment);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_fragment, homeFragment).commit();

        // 获取权限
        FileUtils.verifyPermissions(this);

        initDownloadFailList();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        drawer_layout = findViewById(R.id.drawer_layout);

        navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);

        View headerView = navigation_view.getHeaderView(0);

        navigation_header_user_face = headerView.findViewById(R.id.navigation_header_user_face);
        navigation_header_user_face.setOnClickListener(this);

        ImageView navigation_header_logout = headerView.findViewById(R.id.navigation_header_logout);
        navigation_header_logout.setOnClickListener(this);

        navigation_header_vip_state = headerView.findViewById(R.id.navigation_header_vip_state);

        navigation_header_user_name = headerView.findViewById(R.id.navigation_header_user_name);
        navigation_header_user_level = headerView.findViewById(R.id.navigation_header_user_level);
        navigation_header_user_ex = headerView.findViewById(R.id.navigation_header_user_ex);
        navigation_header_b_coin1 = headerView.findViewById(R.id.navigation_header_money);
        navigation_header_b_coin2 = headerView.findViewById(R.id.navigation_header_b_coin);
        navigation_header_vip_due_date = headerView.findViewById(R.id.navigation_header_vip_due_date);

        navigation_header_progress_level = headerView.findViewById(R.id.navigation_header_progress_level);

        ImageView toolBar_imageView_menu = findViewById(R.id.toolBar_imageView_menu);
        toolBar_imageView_menu.setOnClickListener(this);
    }

    /**
     * 初始化网络
     */
    private void initInternet() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * 初始化用户数据
     */
    private void initUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("initValues", Activity.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", null);

        if (cookie == null) {
            //创建未登录弹窗
            //do something...

            Toast.makeText(this, "未获取到Cookie", Toast.LENGTH_SHORT).show();
        } else {
            UserInfoParser userInfoParser = new UserInfoParser(getApplicationContext());
            userInfo = userInfoParser.userInfoParse();
            isLogin = true;
            sharedPreferences.edit().putBoolean("isVIP", userInfo.isVip).apply();
            refreshUserInfo();
        }
    }

    /**
     * 刷新控件数据
     */
    private void refreshUserInfo() {
        if (userInfo.isVip) {
            navigation_header_vip_state.setImageResource(R.drawable.icon_vip);
        } else {
            navigation_header_vip_state.setImageResource(R.drawable.icon_normal_vip);
        }

        Glide.with(getApplicationContext()).load(userInfo.userFace).into(navigation_header_user_face);
        navigation_header_user_name.setText(userInfo.userName);
        navigation_header_user_level.setText("Lv：" + userInfo.currentLevel);
        navigation_header_user_ex.setText(userInfo.currentExp + "/" + userInfo.totalExp);
        navigation_header_b_coin1.setText(String.valueOf(userInfo.money));
        navigation_header_b_coin2.setText(String.valueOf(userInfo.bCoinBalance));
        navigation_header_vip_due_date.setText(userInfo.vipDueDate);

        navigation_header_progress_level.setMax(userInfo.totalExp);
        navigation_header_progress_level.setProgress(userInfo.currentExp);
    }

    /**
     * 重置用户信息
     */
    private void resetUserIfo() {
        navigation_header_vip_state.setImageResource(R.drawable.icon_normal_vip);
        navigation_header_user_face.setImageResource(R.drawable.icon_no_login);
        navigation_header_user_name.setText(R.string.navigation_userName);
        navigation_header_user_level.setText("Lv：--");
        navigation_header_user_ex.setText("--/--");
        navigation_header_b_coin1.setText(R.string.navigation_nothing);
        navigation_header_b_coin2.setText(R.string.navigation_nothing);
        navigation_header_vip_due_date.setText(R.string.navigation_vip_due_date);
        navigation_header_progress_level.setMax(0);
        navigation_header_progress_level.setProgress(0);

        //删除本地Cookie
//        SharedPreferences.Editor editor = getSharedPreferences("initValues", Activity.MODE_PRIVATE).edit();
//        editor.remove("cookie").apply();

        isLogin = false;
        cookie = null;
    }

    /**
     * 初始化未完成下载的媒体资源
     */
    private void initDownloadFailList() {
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(SimpleThreadPool.LoadTaskNum, SimpleThreadPool.LoadTask);
        simpleThreadPool.submit(new FutureTask<>(new SimpleInitFailListThread()));
    }

    /**
     * 侧滑菜单栏的监听
     *
     * @param item 监听的item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_menu_home:

                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }

                switchFragment(homeFragment);
                break;
            case R.id.navigation_menu_favorite:

                if (favoriteFragment == null) {
                    favoriteFragment = new FavoriteFragment();
                }

                switchFragment(favoriteFragment);
                break;
            case R.id.navigation_menu_localOrder:

                if (localOrderFragment == null) {
                    localOrderFragment = new LocalOrderFragment();
                }

                switchFragment(localOrderFragment);
                break;
            case R.id.navigation_menu_order:
                if (orderFragment == null) {
                    orderFragment = new OrderFragment();
                }

                switchFragment(orderFragment);
                break;
            case R.id.navigation_menu_history:

                if (historyFragment == null) {
                    historyFragment = new HistoryFragment();
                }

                switchFragment(historyFragment);
                break;
            case R.id.navigation_menu_preference:

                if (preferenceFragment == null) {
                    preferenceFragment = new PreferenceFragment();
                }

                switchFragment(preferenceFragment);
                break;
            case R.id.navigation_menu_download:
                Intent intent = new Intent(this, DownloadedActivity.class);
                startActivity(intent);

                break;
            case R.id.navigation_menu_help:

                //跳转至帮助文档页面
                Intent intentHelp = new Intent();
                intentHelp.setAction("android.intent.action.VIEW");
                intentHelp.setData(Uri.parse(getString(R.string.help_link)));
                startActivity(intentHelp);

                break;
            case R.id.navigation_menu_about:
                //显示`关于`弹窗
                AboutBiuVideoDialog aboutBiuVideoDialog = new AboutBiuVideoDialog(MainActivity.this);
                aboutBiuVideoDialog.show();
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
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //判断该fragment是否已经被添加过
        if (!fragment.isAdded()) {
            //添加到 fragmentList
            fragmentList.add(fragment);
            fragmentTransaction.add(R.id.main_fragment, fragment);
        }

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

    // 设置main中的打开侧滑菜单按钮的监听和侧滑菜单中的返回按钮
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_header_user_face:
                //跳转到登陆界面
                if (!isLogin) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, 1003);
                }

                break;
            case R.id.navigation_header_logout:
                WarnDialog warnDialog = new WarnDialog(MainActivity.this);

                if (isLogin) {
                    warnDialog.setTitle("提示");
                    warnDialog.setContent("是否要退出当前账户？");
                    warnDialog.setOnConfirmListener(new WarnDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            //清除当前存储的Cookie
                            resetUserIfo();

                            // 发送本地广播，用户已退出
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
                            Intent intent = new Intent("UserLogout");
                            localBroadcastManager.sendBroadcast(intent);

                            warnDialog.dismiss();
                        }
                    });

                } else {
                    warnDialog.setTitle("提示");
                    warnDialog.setContent("还未进行登录，是否要登录账户？");
                    warnDialog.setOnConfirmListener(new WarnDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivityForResult(intent, 1003);

                            warnDialog.dismiss();
                        }
                    });
                }

                warnDialog.show();

                break;
            case R.id.toolBar_imageView_menu:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1003 && resultCode == 1004) {
            initUserInfo();

            // 发送本地广播，用户已登录
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            Intent intent = new Intent("UserLogin");
            localBroadcastManager.sendBroadcast(intent);
        }
    }

    private class SimpleInitFailListThread implements Callable<String> {
        @Override
        public String call() {
            // 处理未完成下载的媒体资源
            SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.DownloadDetailsForVideo);
            DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();
            downloadRecordsDatabaseUtils.setFailed();

            // 删除未完成下载的媒体资源文件
            List<DownloadedDetailMedia> downloadedDetailMedia = downloadRecordsDatabaseUtils.queryDownloadFailMedia();
            File mediaFile;
            String folderPath = FileUtils.createFolder(FileUtils.ResourcesFolder.VIDEOS);
            for (DownloadedDetailMedia detailMedia : downloadedDetailMedia) {
                String fileName = detailMedia.fileName;
                if (detailMedia.isVideo) {
                    mediaFile = new File(folderPath + "/" + fileName + ".mp4");
                } else {
                    mediaFile = new File(folderPath + "/" + fileName + ".mp3");
                }

                if (mediaFile.exists()) {
                    mediaFile.delete();
                }
            }

            downloadRecordsDatabaseUtils.close();
            return null;
        }
    }
}