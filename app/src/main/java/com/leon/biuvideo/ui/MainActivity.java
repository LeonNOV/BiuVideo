package com.leon.biuvideo.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mobstat.StatService;
import com.leon.biuvideo.R;
import com.leon.biuvideo.service.DownloadWatcher;
import com.leon.biuvideo.service.WeatherService;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.values.Partitions;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {
    private WeatherConnection weatherConnection;
    private Intent weatherServiceIntent;

    private final List<OnTouchListener> onTouchListenerList = new ArrayList<>();

    public interface OnTouchListener {
        /**
         * 触摸监听
         */
        void onTouchEvent(MotionEvent event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatService.start(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (findFragment(NavFragment.class) == null) {
            loadRootFragment(R.id.fl_container, NavFragment.newInstance());
        }

        // 与WeatherService建立连接
        weatherConnection = new WeatherConnection();
        weatherServiceIntent = new Intent(getApplicationContext(), WeatherService.class);
        bindService(weatherServiceIntent, weatherConnection, Context.BIND_AUTO_CREATE);

        DownloadWatcher downloadWatcher = new DownloadWatcher();
        downloadWatcher.init(getApplicationContext());

        // 初始化PreferenceUtil类中的PREFERENCE
        PreferenceUtils.PREFERENCE = getSharedPreferences(PreferenceUtils.PREFERENCE_NAME, Context.MODE_PRIVATE);

        // 初始化分区数据
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                Partitions.PARTITION = JSONObject.parseObject(FileUtils.getAssetsContent(getApplicationContext(), "partition.json"));
            }
        });
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (OnTouchListener onTouchListener : onTouchListenerList) {
            if (onTouchListener != null) {
                onTouchListener.onTouchEvent(ev);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 注册Touch事件
     *
     * @param onTouchListener   onTouchListener
     */
    public void registerTouchEvenListener (OnTouchListener onTouchListener) {
        onTouchListenerList.add(onTouchListener);
        Fuck.blue("Add Complete，OnTouchListenerList Now Size：" + onTouchListenerList.size());
    }

    /**
     * 取消注册Touch事件
     *
     * @param onTouchListener   onTouchListener
     */
    public void unregisterTouchEvenListener (OnTouchListener onTouchListener) {
        onTouchListenerList.remove(onTouchListener);
        Fuck.blue("Removal Complete，OnTouchListenerList Now Size：" + onTouchListenerList.size());
    }
}