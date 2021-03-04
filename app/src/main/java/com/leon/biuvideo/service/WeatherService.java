package com.leon.biuvideo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.utils.PermissionUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.WeatherUtil;
import com.leon.biuvideo.values.FeaturesName;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 后台天气服务，每半小时获取一次天气数据，以广播的形式发送数据
 */
public class WeatherService extends Service {
    private Context context;
    private WeatherUtil weatherUtil;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new WeatherBinder();
    }

    /**
     * 添加计数器
     */
    private void addTimer() {
        if (timer == null) {
            timer = new Timer();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {

                    // 如果天气模块已开启就获取当前天气信息
                    if (PreferenceUtils.getFeaturesStatus(context, FeaturesName.WEATHER_MODEL)) {
                        // 检查定位服务是否已授予
                        if (PermissionUtil.verifyPermission(context, PermissionUtil.Permission.LOCATION)) {
                            // 获取当前天气
                            Weather currentWeather = weatherUtil.getCurrentWeather(PreferenceUtils.getAdcode(context));
                            // 发送广播，通知已更新天气数据
                            sendBroadcast(currentWeather);
                        }
                    }
                }
            };

            // 第一次执行在5毫秒后，后面的执行在半小时后
            timer.schedule(timerTask, 5, 30 * 60 * 1000);
        }
    }

    /**
     * 发送本地广播
     */
    private void sendBroadcast(Weather currentWeather) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);

        Intent weatherModelIntent = new Intent("currentWeatherData");
        weatherModelIntent.putExtra("currentWeather", currentWeather);

        localBroadcastManager.sendBroadcast(weatherModelIntent);
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }

        weatherUtil = null;
        super.onDestroy();
    }

    public class WeatherBinder extends Binder {
        public void init() {
            if (weatherUtil == null) {
                weatherUtil = new WeatherUtil();
            }

            addTimer();
        }
    }
}
