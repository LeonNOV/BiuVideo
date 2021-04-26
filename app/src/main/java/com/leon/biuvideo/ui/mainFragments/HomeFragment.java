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
import androidx.recyclerview.widget.GridLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.RecommendAdapter;
import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.beans.homeBeans.Recommend;
import com.leon.biuvideo.beans.homeBeans.WatchLater;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.DownloadManagerFragment;
import com.leon.biuvideo.ui.home.HistoryFragment;
import com.leon.biuvideo.ui.home.RecommendFragment;
import com.leon.biuvideo.ui.home.SettingsFragment;
import com.leon.biuvideo.ui.home.WatchLaterFragment;
import com.leon.biuvideo.ui.mainFragments.homeModels.WeatherModelInterface;
import com.leon.biuvideo.ui.otherFragments.biliUserFragments.BiliUserFragment;
import com.leon.biuvideo.ui.otherFragments.PopularFragment;
import com.leon.biuvideo.ui.resourcesFragment.audio.AudioFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.bangumi.BangumiFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.contribution.VideoFragment;
import com.leon.biuvideo.ui.views.CardTitle;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.LocationUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.WeatherUtil;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.RecommendParser;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.WatchLaterParser;
import com.leon.biuvideo.values.Actions;
import com.leon.biuvideo.values.FeaturesName;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 主页Fragment，第一显示的Fragment
 */
public class HomeFragment extends BaseSupportFragment implements View.OnClickListener {
    /**
     * 主页数据显示个数
     */
    private static final int HOME_DATA_COUNT = 10;

    private LocationUtil locationUtil;

    private Handler handler;
    private WeatherModelInterface weatherModel;
    private WeatherUtil weatherUtil;

    private LoadingRecyclerView homeRecommendLoadingRecyclerView;
    private LoadingRecyclerView homeWatchLaterLoadingRecyclerView;

    /**
     * 所有的推荐内容（已打乱）
     */
    private List<Recommend> recommendList;

    /**
     * 稍后观看数据
     */
    private List<WatchLater> watchLaterList;

    /**
     * 主页显示的推荐内容
     */
    private final List<Recommend> homeRecommendList = new ArrayList<>(10);
    private final List<WatchLater> homeWatchLaterList = new ArrayList<>(10);

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
                .setOnClickListener(R.id.home_popular, this)
                .setText(R.id.home_weekday, ValueUtils.getWeekday())
                .setText(R.id.home_date, ValueUtils.generateTime(System.currentTimeMillis(), "MM/dd", false));

        ((CardTitle) findView(R.id.home_cardTitle_recommend)).setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(new RecommendFragment(recommendList));
            }
        });

        ((CardTitle) findView(R.id.home_cardTitle_watchLater)).setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(new WatchLaterFragment(watchLaterList));
            }
        });

        homeRecommendLoadingRecyclerView = findView(R.id.home_recommend_loadingRecyclerView);
        homeWatchLaterLoadingRecyclerView = findView(R.id.home_watchLater_loadingRecyclerView);

        initValue();
    }

    private void initValue() {
        initBroadcastReceiver();

        // 设置状态为加载数据中
//        homeRecommendLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING);
//        homeWatchLaterLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING);

        // 开启单线程加载网络数据
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取推荐内容
//                recommendList = getRecommendData();

                // 获取稍后观看数据
//                watchLaterList = getWatchLaterData();

                // 各获取前十个数据作为主页的数据
//                for (int i = 0; i < HOME_DATA_COUNT; i++) {
//                    homeRecommendList.add(recommendList.get(i));
//                    homeWatchLaterList.add(watchLaterList.get(i));
//                }

                // 设置数据
//                Handler mHandler = new Handler(Looper.getMainLooper());
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 设置推荐数据
//                        int recommendColumns = PreferenceUtils.getRecommendColumns();
//                        homeRecommendLoadingRecyclerView.setRecyclerViewLayoutManager(new GridLayoutManager(context, recommendColumns));
//                        homeRecommendLoadingRecyclerView.setRecyclerViewAdapter(new RecommendAdapter(homeRecommendList, recommendColumns == 1 ? RecommendAdapter.SINGLE_COLUMN : RecommendAdapter.DOUBLE_COLUMN, context));
//                        homeRecommendLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING_FINISH);
//
//                        // 设置稍后再看数据
//                        homeWatchLaterLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
//                        homeWatchLaterLoadingRecyclerView.setRecyclerViewAdapter(new WatchLaterAdapter(homeWatchLaterList, context));
//                        homeWatchLaterLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING_FINISH);
//                    }
//                });
            }

            /**
             * 获取稍后再看数据数据
             *
             * @return  WatchLater集合
             */
            private List<WatchLater> getWatchLaterData() {
                WatchLaterParser watchLaterParser = new WatchLaterParser();
                return watchLaterParser.parseData();
            }

            /**
             * 获取推荐数据
             *
             * @return  Recommend集合
             */
            private List<Recommend> getRecommendData() {
                RecommendParser recommendParser = new RecommendParser(null);
                return recommendParser.parseData();
            }
        });

        weatherUtil = new WeatherUtil();

        // 初始化天气模块
        if (weatherModel == null) {
            weatherModel = new WeatherModelInterface();
            weatherModel.onInitialize(view, context);
        }

        // 在主线程中更新天气信息
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle data = msg.getData();
                boolean weatherModelStatus = data.getBoolean("weatherModelStatus");
                Weather currentWeather = (Weather) data.getSerializable("currentWeather");

                // 如果配置文件中未开启天气模块，则在此处停止运行此方法
                if (PreferenceUtils.getFeaturesStatus(FeaturesName.WEATHER_MODEL)) {
                    if (currentWeather != null && weatherModelStatus) {
                        // 更新天气数据
                        weatherModel.onRefresh(currentWeather);
                    }

                    // 定位服务或已手动设置位置，则显示天气模块
                    weatherModel.setDisplayState(PreferenceUtils.getLocationServiceStatus() || PreferenceUtils.getManualSetLocationStatus());
                }

                return true;
            }
        });

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取当前天气
                getCurrentWeather();
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
                // 26801
                // 36167
                ((NavFragment) getParentFragment()).startBrotherFragment(new BangumiFragment("26801"));
//                ((NavFragment) getParentFragment()).startBrotherFragment(OrderFragment.getInstance());
                break;
            case R.id.home_my_favorites:
                ((NavFragment) getParentFragment()).startBrotherFragment(new BiliUserFragment("2946474"));
//                ((NavFragment) getParentFragment()).startBrotherFragment(FavoritesFragment.getInstance());
                break;
            case R.id.home_my_follows:
                ((NavFragment) getParentFragment()).startBrotherFragment(new VideoFragment("BV1Rx411876f"));
//                ((NavFragment) getParentFragment()).startBrotherFragment(FollowsFragment.getInstance());
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
        intentFilter.addAction(Actions.WEATHER_MODEL);
        intentFilter.addAction(Actions.CURRENT_WEATHER);
        intentFilter.addAction(Actions.REFRESH_RECOMMEND_STYLE);

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
            switch (action) {
                case Actions.WEATHER_MODEL:
                    // 获取天气模块开关状态
                    boolean weatherModelStatus = intent.getBooleanExtra("weatherModelStatus", false);

                    // 判断天气模块是否为开启状态
                    if (weatherModelStatus && PreferenceUtils.getFeaturesStatus(FeaturesName.WEATHER_MODEL)) {

                        // 如果定位服务状态和手动设置位置状态有一个为true就显示天气模块
                        weatherModel.setDisplayState(PreferenceUtils.getLocationServiceStatus() || PreferenceUtils.getManualSetLocationStatus());
                    } else {
                        weatherModel.setDisplayState(false);
                        return;
                    }

                    // 如果已开启定位服务，则通过GPS/NetWork来获取位置
                    if (PreferenceUtils.getLocationServiceStatus()) {
                        if (locationUtil == null) {
                            locationUtil = new LocationUtil(context);
                        }

                        locationUtil.location();
                        String[] address = locationUtil.getAddress();
                        PreferenceUtils.setAddress(address);

                        // 根据当前位置，设置adcode
                        setAdcode(address);
                    }

                    SimpleSingleThreadPool.executor(new Runnable() {
                        @Override
                        public void run() {
                            getCurrentWeather();
                        }
                    });
                    break;
                case Actions.CURRENT_WEATHER:
                    Weather currentWeather = (Weather) intent.getSerializableExtra("currentWeather");
                    if (currentWeather != null) {
                        weatherModel.onRefresh(currentWeather);
                    }
                    break;
                case Actions.REFRESH_RECOMMEND_STYLE:
                    homeRecommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

                    // 刷新推荐试图样式
                    int recommendColumns = intent.getIntExtra("recommendColumns", 2);

                    homeRecommendLoadingRecyclerView.setRecyclerViewAdapter(new RecommendAdapter(homeRecommendList, recommendColumns == 1 ? RecommendAdapter.SINGLE_COLUMN : RecommendAdapter.DOUBLE_COLUMN, context));
                    homeRecommendLoadingRecyclerView.setRecyclerViewLayoutManager(new GridLayoutManager(context, recommendColumns));

                    // 设置状态为已完成加载数据
                    homeRecommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                    break;
                default:
                    break;
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
                    String adcode = LocationUtil.getAdcode(address);

                    if (adcode == null) {
                        SimpleSnackBar.make(view, "初始化位置失败", SimpleSnackBar.LENGTH_SHORT).show();
                    } else {
                        PreferenceUtils.setAdcode(adcode);
                    }
                }
            });
        }
    }
}