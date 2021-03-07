package com.leon.biuvideo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.leon.biuvideo.R;
import com.leon.biuvideo.service.WeatherService;
import com.leon.biuvideo.utils.PreferenceUtils;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {

    private WeatherConnection weatherConnection;
    private Intent weatherServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findFragment(NavFragment.class) == null) {
            loadRootFragment(R.id.fl_container, NavFragment.newInstance());
        }

        // 与WeatherService建立连接
        weatherConnection = new WeatherConnection();
        weatherServiceIntent = new Intent(getApplicationContext(), WeatherService.class);
        bindService(weatherServiceIntent, weatherConnection, Context.BIND_AUTO_CREATE);

        // 初始化PreferenceUtil类中的PREFERENCE
        PreferenceUtils.PREFERENCE = getSharedPreferences(PreferenceUtils.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void unbind() {
        // 解绑服务
        unbindService(weatherConnection);

        // 停止服务
        stopService(weatherServiceIntent);
    }

    @Override
    protected void onDestroy() {
        unbind();
        super.onDestroy();
    }

    /**
     * @Author Leon
     * @Time 2021/3/1
     * @Desc 天气服务连接类
     */
    private static class WeatherConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WeatherService.WeatherBinder weatherBinder = (WeatherService.WeatherBinder) service;
            weatherBinder.init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}