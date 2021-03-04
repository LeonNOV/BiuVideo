package com.leon.biuvideo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.leon.biuvideo.utils.WeatherUtil;

/**
 * 后台天气服务，每半小时获取一次天气数据
 */
public class WeatherService extends Service {
    private WeatherUtil weatherUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void addTimer() {
    }
}
