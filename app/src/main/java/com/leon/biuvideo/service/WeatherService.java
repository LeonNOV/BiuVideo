package com.leon.biuvideo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.utils.LocationUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.WeatherUtil;
import com.leon.biuvideo.values.Actions;
import com.leon.biuvideo.values.FeaturesName;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author Leon
 * @Time 2021/3/4
 * @Desc 后台天气服务，每半小时获取一次天气数据，以广播的形式发送数据
 */
public class WeatherService extends Service {
    private Context context;
    private WeatherUtil weatherUtil;
    private Timer timer;
    private LocationUtil locationUtil;
    private LocalBroadcastManager localBroadcastManager;

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
                    if (PreferenceUtils.getFeaturesStatus(FeaturesName.WEATHER_MODEL)) {
                        // 获取定位服务开启状态和手动设置位置状态
                        if (PreferenceUtils.getLocationServiceStatus() || PreferenceUtils.getManualSetLocationStatus()) {
                            // 如果已开启定位，则获取天气的同时获取当前位置信息
                            if (PreferenceUtils.getLocationServiceStatus()) {
                                if (locationUtil == null) {
                                    locationUtil = new LocationUtil(context);
                                    locationUtil.location();
                                }

                                String[] address = locationUtil.getAddress();
                                String adcode = LocationUtil.getAdcode(address);

                                // 设置当前位置和adcode
                                PreferenceUtils.setAddress(address);
                                PreferenceUtils.setAdcode(adcode);
                            }

                            // 获取当前天气
                            Weather currentWeather = weatherUtil.getCurrentWeather(PreferenceUtils.getAdcode());
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
        if (localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
        }

        Intent weatherModelIntent = new Intent(Actions.CURRENT_WEATHER);
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
