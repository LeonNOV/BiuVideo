package com.leon.biuvideo.utils;

import androidx.annotation.DrawableRes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.values.apis.AmapAPIs;
import com.leon.biuvideo.values.apis.ApiKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 天气工具类
 */
public class WeatherUtil {

    private Weather weather;

    private JSONObject getWeatherResponse (String cityCode, String extensions) {
        Map<String, String> params = new HashMap<>();
        params.put("key", ApiKeys.AMAP_KEY);
        params.put("city", cityCode);

        //base/all
        params.put("extensions", extensions);

        return HttpUtils.getResponse(AmapAPIs.AMAP_WEATHER, params);
    }

    /**
     * 获取实时天气
     */
    public Weather getCurrentWeather (String cityCode) {
        JSONObject weatherResponse = getWeatherResponse(cityCode, "base");

        weather = new Weather();
        if (weatherResponse.getString("status").equals("1")) {
            weather.status = 1;
            weather.infocode = weatherResponse.getString("infocode");

            // 只获取第一个
            JSONObject lives = (JSONObject) weatherResponse.getJSONArray("lives").get(0);
            weather = parseWeather(true, lives);
        } else {
            weather.status = 0;
        }

        return weather;
    }

    /**
     * 获取当天、第二天、第三天的预报数据
     *
     * @param cityCode  城市编码
     * @return  Weather
     */
    public Weather getWeatherInfos(String cityCode) {
        JSONObject weatherResponse = getWeatherResponse(cityCode, "all");

        Weather weather = new Weather();
        if (weatherResponse.getString("status").equals("1")) {
            weather.status = 1;
            weather.infocode = weatherResponse.getString("infocode");

            // 只获取第一个
            JSONObject forecast = (JSONObject) weatherResponse.getJSONArray("forecasts").get(0);
            weather = parseWeather(false, forecast);
        } else {
            weather.status = 0;
        }

        return weather;
    }

    private Weather parseWeather (boolean isCurrent, JSONObject responseFirstItem) {
        if (isCurrent) {
            weather.province = responseFirstItem.getString("province");
            weather.city = responseFirstItem.getString("city");
            weather.adcode = responseFirstItem.getString("adcode");
            weather.weather = responseFirstItem.getString("weather");
            weather.weatherIconId = getWeatherType(weather.weather);
            weather.temperature = responseFirstItem.getString("temperature");
            weather.humidity = responseFirstItem.getString("humidity");
            weather.reporttime = responseFirstItem.getString("reporttime");

        } else {
            weather.province = responseFirstItem.getString("province");
            weather.city = responseFirstItem.getString("city");
            weather.adcode = responseFirstItem.getString("adcode");
            weather.reporttime = responseFirstItem.getString("reporttime");

            // 预报数据list结构，元素cast,按顺序为当天、第二天、第三天的预报数据
            JSONArray casts = responseFirstItem.getJSONArray("casts");
            weather.casts = new ArrayList<>();
            for (Object cast : casts) {
                Weather.Cast castsItem = new Weather.Cast();
                JSONObject jsonObject = (JSONObject) cast;
                castsItem.date = jsonObject.getString("date");
                castsItem.week= jsonObject.getString("week");

                castsItem.dayweather = jsonObject.getString("dayweather");
                castsItem.nightweather = jsonObject.getString("nightweather");

                castsItem.daytemp = jsonObject.getString("daytemp");
                castsItem.nighttemp = jsonObject.getString("nighttemp");

                castsItem.daywind = jsonObject.getString("daywind");
                castsItem.nightwind = jsonObject.getString("nightwind");

                castsItem.daypower = jsonObject.getString("daypower");
                castsItem.nightpower = jsonObject.getString("nightpower");
            }
        }

        return weather;
    }

    private @DrawableRes int getWeatherType(String weather) {
        int weatherIconId = R.drawable.weather_unknown;

        switch (weather) {
            case "晴":
                weatherIconId = R.drawable.weather_sunny;
                break;
            case "少云":
                weatherIconId = R.drawable.weather_few_clouds;
                break;
            case "阴":
                weatherIconId = R.drawable.weather_cloudy;
                break;
            case "热":
                weatherIconId = R.drawable.weather_heat;
                break;
            case "冷":
                weatherIconId = R.drawable.weather_cold;
                break;
            case "未知":
                weatherIconId = R.drawable.weather_unknown;
                break;
            default:
                weatherIconId = getWeatherType2(weather);
                break;
        }

        return weatherIconId;
    }

    private int getWeatherType2(String weather) {
        String[] sandDusts = {"浮尘", "扬沙", "沙尘暴", "强沙尘暴"};

        int weatherIconId = R.drawable.weather_unknown;

        if (weather.endsWith("多云")) {
            weatherIconId = R.drawable.weather_partly_cloudy;
        } else if (weather.endsWith("风") || weather.equals("平静") || weather.equals("热带风暴")) {
            weatherIconId = R.drawable.weather_windy;
        } else if (weather.endsWith("霾")) {
            weatherIconId = R.drawable.weather_smog;
        } else if (weather.endsWith("雾")) {
            weatherIconId = R.drawable.weather_foggy;
        } else if (weather.contains("雷阵雨")) {
            weatherIconId = R.drawable.weather_storm;
        } else if (weather.startsWith("雨雪") || weather.endsWith("雨夹雪")){
            weatherIconId = R.drawable.weather_sleet;
        } else if (weather.endsWith("雨")) {
            weatherIconId = R.drawable.weather_rain;
        } else if (weather.endsWith("雪")) {
            weatherIconId = R.drawable.weather_snow;
        } else {
            for (String sandDust : sandDusts) {
                if (weather.equals(sandDust)) {
                    weatherIconId = R.drawable.weather_sand_dust;
                    break;
                }
            }
        }

        return weatherIconId;
    }
}
