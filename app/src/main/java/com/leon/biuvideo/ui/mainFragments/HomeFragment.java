package com.leon.biuvideo.ui.mainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.WeatherUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.FutureTask;

/**
 * 主页面
 */
public class HomeFragment extends BaseSupportFragment implements View.OnClickListener {
    RecyclerView home_recyclerView;
    private ImageView weatherIcon;
    private CardTitle home_fragment_cardTitle_recommend;
    private TextView home_weatherStr;
    private TextView home_weatherTem;
    private TextView home_location;
    private TagView home_tagView;
    private WeatherUtil weatherUtil;
    private Handler handler;
    private LinearLayout home_model;

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

        weatherIcon = findView(R.id.home_weatherIcon);

        home_fragment_cardTitle_recommend = findView(R.id.home_fragment_cardTitle_recommend);
        home_fragment_cardTitle_recommend.setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(RecommendFragment.newInstance());
            }
        });

        home_recyclerView = findView(R.id.home_recyclerView);
        home_weatherStr = findView(R.id.home_weatherStr);
        home_weatherTem = findView(R.id.home_weatherTem);
        home_location = findView(R.id.home_location);
        home_tagView = findView(R.id.home_tagView);
        home_model = findView(R.id.home_model);

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
                Weather currentWeather = weatherUtil.getCurrentWeather(PreferenceUtils.getAdcode(context));

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putSerializable("currentWeather", currentWeather);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        SimpleThreadPool simpleThreadPool = simpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(simpleLoadDataThread), "initHomeValues");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Weather currentWeather = (Weather) msg.getData().getSerializable("currentWeather");
                if (currentWeather != null) {
                    weatherIcon.setImageResource(currentWeather.weatherIconId);
                    home_weatherStr.setText(currentWeather.weather);
                    home_weatherTem.setText(currentWeather.temperature + "°");
                    home_location.setText(currentWeather.city);
                    home_tagView.setRightValue(ValueUtils.generateTime(ValueUtils.formatStrTime(currentWeather.reporttime), "HH:mm", false));
                }

                simpleThreadPool.cancelTask("initHomeValues");
                return true;
            }
        });
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
                Toast.makeText(context, "点击了-历史记录", Toast.LENGTH_SHORT).show();
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

    private void initBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WeatherModel");

        LocalReceicer localReceicer = new LocalReceicer();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceicer, intentFilter);
    }

    private class LocalReceicer extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("WeatherModel")) {
                boolean status = intent.getBooleanExtra("status", false);
                if (status) {
                    home_model.setVisibility(View.VISIBLE);
                } else {
                    home_model.setVisibility(View.GONE);
                }
            }
        }
    }
}
