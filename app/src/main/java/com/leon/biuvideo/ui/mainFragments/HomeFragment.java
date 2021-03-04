package com.leon.biuvideo.ui.mainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.testAdapters.RvTestAdapter;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.SimpleLoadDataThread;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.dialogs.WarnDialog;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.home.OrderFragment;
import com.leon.biuvideo.ui.home.RecommendFragment;
import com.leon.biuvideo.ui.home.SettingsFragment;
import com.leon.biuvideo.ui.otherFragments.PopularFragment;
import com.leon.biuvideo.ui.views.CardTitle;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LocationUtil;
import com.leon.biuvideo.utils.PermissionUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleThread;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.WeatherUtil;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.apis.AmapAPIs;
import com.leon.biuvideo.values.apis.AmapKey;

import java.io.Serializable;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.FutureTask;

/**
 * 主页面
 */
public class HomeFragment extends BaseSupportFragment implements View.OnClickListener {
    private RecyclerView home_recyclerView;
    private LocationUtil locationUtil;

    private Handler handler;
    private WeatherModel weatherModel;
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
                .setOnClickListener(R.id.home_my_downloaded, this)
                .setOnClickListener(R.id.home_setting, this)
                .setOnClickListener(R.id.home_popular, this);

        ((CardTitle) findView(R.id.home_fragment_cardTitle_recommend)).setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(RecommendFragment.newInstance());
            }
        });

        home_recyclerView = findView(R.id.home_recyclerView);

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
        home_recyclerView.setAdapter(rvTestAdapter);

        weatherUtil = new WeatherUtil();

        SimpleLoadDataThread simpleLoadDataThread = new SimpleLoadDataThread() {
            @Override
            public void load() {
                // 初始化天气模块
                if (weatherModel == null) {
                    weatherModel = new WeatherModel();
                    weatherModel.onInitialize(view, context);
                }
                // 获取当前天气
                getCurrentWeather();
            }
        };

        SimpleThreadPool simpleThreadPool = simpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(simpleLoadDataThread), "initHomeValues");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle data = msg.getData();
                boolean weatherModelStatus = data.getBoolean("weatherModelStatus");
                Weather currentWeather = (Weather) data.getSerializable("currentWeather");

                if (currentWeather != null && weatherModelStatus) {
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
        boolean weatherModelStatus = PreferenceUtils.getFeaturesStatus(context, FeaturesName.WEATHER_MODEL);
        if (weatherModelStatus) {
            String adcode = PreferenceUtils.getAdcode(context);

            if (adcode == null) {
                bundle.putBoolean("weatherModelStatus", false);
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
                ((NavFragment) getParentFragment()).startBrotherFragment(OrderFragment.newInstance());
                break;
            case R.id.home_my_favorites:
                ((NavFragment) getParentFragment()).startBrotherFragment(FavoritesFragment.newInstance());
                break;
            case R.id.home_my_follows:
                WarnDialog warnDialog = new WarnDialog(context, "Test", "TestTestTestTestTestTestTestTestTestTest");
                warnDialog.setOnWarnActionListener(new WarnDialog.OnWarnActionListener() {
                    @Override
                    public void onConfirm() {
                        warnDialog.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        warnDialog.dismiss();
                    }
                });
                warnDialog.show();
//                Toast.makeText(context, "点击了-我关注的人", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_my_history:
                break;
            case R.id.home_my_downloaded:
                Toast.makeText(context, "点击了-下载记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_setting:
                ((NavFragment) getParentFragment()).startBrotherFragment(SettingsFragment.newInstance());
                break;
            case R.id.home_popular:
                ((NavFragment) getParentFragment()).startBrotherFragment(PopularFragment.newInstance());
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
        intentFilter.addAction("WeatherModel");

        LocalReceiver localReceiver = new LocalReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    /**
     * 广播接收者，主要用于显示或隐藏天气模块
     */
    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("WeatherModel")) {
                boolean status = intent.getBooleanExtra("status", false);
                if (status) {
                    weatherModel.setDisplayState(true);

                    // 获取经纬度
                    if (locationUtil == null) {
                        locationUtil = new LocationUtil(context);
                    }

                    locationUtil.location();
                    String[] address = locationUtil.getAddress();
                    PreferenceUtils.setAddress(context, address);

                    // 根据当前位置，设置adcode
                    setAdcode(address);

                    SimpleThread.executor(new Runnable() {
                        @Override
                        public void run() {
                            getCurrentWeather();
                        }
                    });
                } else {
                    weatherModel.setDisplayState(false);
                }
            }
        }
    }

    /**
     * 获取当前adcode，将其设置到配置文件中
     *
     * @param address   当前位置
     */
    private void setAdcode(String[] address) {
        SimpleThread.executor(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("key", AmapKey.amapKey);
                params.put("address", address[0] + address[1] + address[2]);
                params.put("city", address[2]);

                JSONObject response = HttpUtils.getResponse(AmapAPIs.amapGeocode, params);
                if (response.getString("status").equals("1")) {
                    JSONObject geocode = (JSONObject) response.getJSONArray("geocodes").get(0);
                    String adcode = geocode.getString("adcode");

                    PreferenceUtils.setAdcode(context, adcode);
                } else {
                    SimpleSnackBar.make(view, "初始化位置失败", SimpleSnackBar.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 用于控制主页的天气模块
     */
    private static class WeatherModel implements HomeModel {
        private boolean isInitialized = false;

        private LinearLayout home_model;
        private ImageView weatherModel_weatherIcon;
        private TextView weatherModel_weatherTem;
        private TagView weatherModel_tagView;
        private TextView weatherModel_location;
        private TextView weatherModel_weatherStr;
        private Weather currentWeather;

        @Override
        public void onInitialize(View view, Context context) {
            // 如果isInitialized为false，就进行初始化
            if (isInitialized) {
                return;
            }

            this.isInitialized = true;

            home_model = view.findViewById(R.id.home_model);
            weatherModel_weatherIcon = view.findViewById(R.id.weatherModel_weatherIcon);
            weatherModel_weatherTem = view.findViewById(R.id.weatherModel_weatherTem);
            weatherModel_weatherStr = view.findViewById(R.id.weatherModel_weatherStr);
            weatherModel_tagView = view.findViewById(R.id.weatherModel_tagView);
            weatherModel_location = view.findViewById(R.id.weatherModel_location);

            setDisplayState(PreferenceUtils.getFeaturesStatus(context, FeaturesName.WEATHER_MODEL));
        }

        @Override
        public void onRefresh(Serializable serializable) {
            currentWeather = (Weather) serializable;

            weatherModel_weatherIcon.setImageResource(currentWeather.weatherIconId);
            weatherModel_weatherTem.setText(currentWeather.temperature + "°");
            weatherModel_weatherStr.setText(currentWeather.weather);
            weatherModel_tagView.setRightValue(ValueUtils.generateTime(ValueUtils.formatStrTime(currentWeather.reporttime), "HH:mm", false));
            weatherModel_location.setText(currentWeather.city);
        }

        @Override
        public void setDisplayState(boolean isDisplay) {
            home_model.setVisibility(isDisplay ? View.VISIBLE : View.GONE);
        }
    }
}
