package com.leon.biuvideo.ui.mainFragments.homeModels;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FeaturesName;

import java.io.Serializable;

/**
 * @Author Leon
 * @Time 2021/3/8
 * @Desc 用于控制主页的天气模块
 */
public class WeatherModelInterface implements HomeModelInterface {
    private boolean isInitialized = false;

    private LinearLayout homeModel;
    private ImageView weatherModelWeatherIcon;
    private TextView weatherModelWeatherTem;
    private TagView weatherModelTagView;
    private TextView weatherModelLocation;
    private TextView weatherModelWeatherStr;

    @Override
    public void onInitialize(View view, Context context) {
        // 如果isInitialized为false，就进行初始化
        if (isInitialized) {
            return;
        }

        this.isInitialized = true;

        homeModel = view.findViewById(R.id.home_model);
        weatherModelWeatherIcon = view.findViewById(R.id.weatherModel_weatherIcon);
        weatherModelWeatherTem = view.findViewById(R.id.weatherModel_weatherTem);
        weatherModelWeatherStr = view.findViewById(R.id.weatherModel_weatherStr);
        weatherModelTagView = view.findViewById(R.id.weatherModel_tagView);
        weatherModelLocation = view.findViewById(R.id.weatherModel_location);

        setDisplayState(PreferenceUtils.getFeaturesStatus(FeaturesName.WEATHER_MODEL));
    }

    @Override
    public void onRefresh(Serializable serializable) {
        Weather currentWeather = (Weather) serializable;

        weatherModelWeatherIcon.setImageResource(currentWeather.weatherIconId);
        String tem = currentWeather.temperature + "°";
        weatherModelWeatherTem.setText(tem);
        weatherModelWeatherStr.setText(currentWeather.weather);
        weatherModelTagView.setRightValue(ValueUtils.generateTime(ValueUtils.formatStrTime(currentWeather.reporttime), "HH:mm", false));
        weatherModelLocation.setText(currentWeather.city);
    }

    @Override
    public void setDisplayState(boolean isDisplay) {
        homeModel.setVisibility(isDisplay ? View.VISIBLE : View.GONE);
    }
}
