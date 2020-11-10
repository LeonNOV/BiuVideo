package com.leon.biuvideo.ui.activitys;

import android.graphics.drawable.ColorDrawable;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private ImageView toolBar_imageView_menu, navigation_imageView_back, toolBar_imageView_more;

    private PopupWindow popupWindow;
    private Button main_more_help, main_more_about, main_more_thanks;

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
                drawer_layout.openDrawer(Gravity.LEFT);
                break;
            case R.id.toolBar_imageView_more:
                View popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.main_popup_window, null);
                main_more_help = popupView.findViewById(R.id.main_more_help);
                main_more_help.setOnClickListener(this);

                main_more_about = popupView.findViewById(R.id.main_more_about);
                main_more_about.setOnClickListener(this);

                main_more_thanks = popupView.findViewById(R.id.main_more_thanks);
                main_more_thanks.setOnClickListener(this);

                popupWindow = new PopupWindow(popupView, ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

                popupWindow.showAsDropDown(view, -10, 0);

                break;
            case R.id.navigation_imageView_back:
                drawer_layout.closeDrawer(navigation_view);
                break;
            case R.id.main_more_help:
                Toast.makeText(MainActivity.this, "help", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

                break;
            case R.id.main_more_about:
                Toast.makeText(MainActivity.this, "about", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

                break;
            case R.id.main_more_thanks:

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

                popupWindow.dismiss();

                break;
            default:
                break;
        }
    }
}