package com.leon.biuvideo.ui.mainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.testAdapters.RvTestAdapter;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.AbstractSimpleLoadDataThread;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.DownloadManagerFragment;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.home.HistoryFragment;
import com.leon.biuvideo.ui.home.MyFollowsFragment;
import com.leon.biuvideo.ui.home.OrderFragment;
import com.leon.biuvideo.ui.home.RecommendFragment;
import com.leon.biuvideo.ui.home.SettingsFragment;
import com.leon.biuvideo.ui.mainFragments.homeModels.WeatherModelInterface;
import com.leon.biuvideo.ui.otherFragments.PopularFragment;
import com.leon.biuvideo.ui.views.CardTitle;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LocationUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.WeatherUtil;
import com.leon.biuvideo.values.Actions;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.apis.AmapAPIs;
import com.leon.biuvideo.values.apis.AmapKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.FutureTask;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 主页Fragment，第一显示的Fragment
 */
public class HomeFragment extends BaseSupportFragment implements View.OnClickListener {
    private RecyclerView homeRecyclerView;
    private LocationUtil locationUtil;

    private Handler handler;
    private WeatherModelInterface weatherModel;
    private WeatherUtil weatherUtil;

    @Override
    protected int setLayout() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initView() {
        bindingUtils
                .setOnClickListener(R.id.home_my_orders, this)
                .setOnClickListener(R.id.home_my_favorites, this)
                .setOnClickListener(R.id.home_my_follows, this)
                .setOnClickListener(R.id.home_my_history, this)
                .setOnClickListener(R.id.home_my_downloadManager, this)
                .setOnClickListener(R.id.home_setting, this)
                .setOnClickListener(R.id.home_popular, this);

        ((CardTitle) findView(R.id.home_fragment_cardTitle_recommend)).setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(RecommendFragment.newInstance());
            }
        });

        homeRecyclerView = findView(R.id.home_recommend_recyclerView);

        initValue();
    }

    private void initValue() {
        initBroadcastReceiver();

        List<RvTestBean> rvTestBeanList = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            RvTestBean rvTestBean = new RvTestBean();

            rvTestBean.title = "Title" + (i + 1);
            rvTestBean.view = (random.nextInt(5000) + 5000);

            rvTestBeanList.add(rvTestBean);
        }

        RvTestAdapter rvTestAdapter = new RvTestAdapter(rvTestBeanList, context);
        homeRecyclerView.setAdapter(rvTestAdapter);

        weatherUtil = new WeatherUtil();

        AbstractSimpleLoadDataThread abstractSimpleLoadDataThread = new AbstractSimpleLoadDataThread() {
            @Override
            public void load() {
                // 初始化天气模块
                if (weatherModel == null) {
                    weatherModel = new WeatherModelInterface();
                    weatherModel.onInitialize(view, context);
                }
                // 获取当前天气
                getCurrentWeather();
            }
        };

        SimpleThreadPool simpleThreadPool = abstractSimpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(abstractSimpleLoadDataThread), "initHomeValues");

        // 在主线程中更新天气信息
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle data = msg.getData();
                boolean weatherModelStatus = data.getBoolean("weatherModelStatus");
                Weather currentWeather = (Weather) data.getSerializable("currentWeather");

                if (currentWeather != null && weatherModelStatus) {
                    // 更新天气数据
                    weatherModel.onRefresh(currentWeather);
                }

                weatherModel.setDisplayState(weatherModelStatus);

                simpleThreadPool.cancelTask("initHomeValues");
                return true;
            }
        });
    }

    /**
     * 获取当前天气，并向handler发送消息
     */
    private void getCurrentWeather() {
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();

        // 检查天气模块是否设置为开启状态
        boolean weatherModelStatus = PreferenceUtils.getFeaturesStatus(FeaturesName.WEATHER_MODEL);
        if (weatherModelStatus) {
            String adcode = PreferenceUtils.getAdcode();

            if (adcode == null) {
                // 关闭天气模块的开关
                PreferenceUtils.setFeaturesStatus(FeaturesName.WEATHER_MODEL, false);
                bundle.putBoolean("weatherModelStatus", false);
                bundle.putSerializable("currentWeather", null);
            } else {
                Weather currentWeather = weatherUtil.getCurrentWeather(adcode);
                bundle.putBoolean("weatherModelStatus", true);
                bundle.putSerializable("currentWeather", currentWeather);
            }
        } else {
            bundle.putBoolean("weatherModelStatus", false);
        }

        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_my_orders:
                ((NavFragment) getParentFragment()).startBrotherFragment(OrderFragment.getInstance());
                break;
            case R.id.home_my_favorites:
                ((NavFragment) getParentFragment()).startBrotherFragment(FavoritesFragment.getInstance());
                break;
            case R.id.home_my_follows:
                ((NavFragment) getParentFragment()).startBrotherFragment(MyFollowsFragment.getInstance());
                break;
            case R.id.home_my_history:
                ((NavFragment) getParentFragment()).startBrotherFragment(HistoryFragment.getInstance());
                break;
            case R.id.home_my_downloadManager:
                ((NavFragment) getParentFragment()).startBrotherFragment(DownloadManagerFragment.getInstance());
                break;
            case R.id.home_setting:
                ((NavFragment) getParentFragment()).startBrotherFragment(SettingsFragment.getInstance());
                break;
            case R.id.home_popular:
                ((NavFragment) getParentFragment()).startBrotherFragment(PopularFragment.getInstance());
                break;
            default:
                break;
        }
    }

    /**
     * 初始化广播接收者
     */
    private void initBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.WeatherModel);
        intentFilter.addAction(Actions.CurrentWeather);

        LocalReceiver localReceiver = new LocalReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    /**
     * @Author Leon
     * @Time 2021/3/7
     * @Desc 广播接收者，主要用于显示、刷新或隐藏天气模块
     */
    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Actions.WeatherModel)) {
                boolean status = intent.getBooleanExtra("status", false);
                if (status) {
                    weatherModel.setDisplayState(true);

                    // 获取经纬度
                    if (locationUtil == null) {
                        locationUtil = new LocationUtil(context);
                    }

                    locationUtil.location();
                    String[] address = locationUtil.getAddress();
                    PreferenceUtils.setAddress(address);

                    // 根据当前位置，设置adcode
                    setAdcode(address);

                    SimpleSingleThreadPool.executor(new Runnable() {
                        @Override
                        public void run() {
                            getCurrentWeather();
                        }
                    });
                } else {
                    weatherModel.setDisplayState(false);
                }
            } else if (action.equals(Actions.CurrentWeather)) {
                Weather currentWeather = (Weather) intent.getSerializableExtra("currentWeather");
                if (currentWeather != null) {
                    weatherModel.onRefresh(currentWeather);
                }
            }
        }

        /**
         * 获取当前adcode，将其设置到配置文件中
         *
         * @param address   当前位置
         */
        private void setAdcode(String[] address) {
            SimpleSingleThreadPool.executor(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> params = new HashMap<>(3);
                    params.put("key", AmapKey.amapKey);
                    params.put("address", address[0] + address[1] + address[2]);
                    params.put("city", address[2]);

                    JSONObject response = HttpUtils.getResponse(AmapAPIs.amapGeocode, params);
                    if ("1".equals(response.getString("status"))) {
                        JSONObject geocode = (JSONObject) response.getJSONArray("geocodes").get(0);
                        String adcode = geocode.getString("adcode");

                        PreferenceUtils.setAdcode(adcode);
                    } else {
                        SimpleSnackBar.make(view, "初始化位置失败", SimpleSnackBar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
