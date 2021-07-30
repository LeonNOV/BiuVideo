package com.leon.biuvideo.ui.mainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.RecommendAdapter;
import com.leon.biuvideo.adapters.homeAdapters.WatchLaterAdapter;
import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.beans.homeBeans.WatchLater;
import com.leon.biuvideo.beans.resourcesBeans.VideoRecommend;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.DownloadManagerFragment;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.home.FollowsFragment;
import com.leon.biuvideo.ui.home.HistoryFragment;
import com.leon.biuvideo.ui.home.OrderFragment;
import com.leon.biuvideo.ui.home.RecommendFragment;
import com.leon.biuvideo.ui.home.SettingsFragment;
import com.leon.biuvideo.ui.home.WatchLaterFragment;
import com.leon.biuvideo.ui.mainFragments.homeModels.WeatherModelInterface;
import com.leon.biuvideo.ui.otherFragments.PopularFragment;
import com.leon.biuvideo.ui.views.CardTitle;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
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

import me.yokeyword.fragmentation.SupportFragment;

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

    private WeatherModelInterface weatherModel;
    private WeatherUtil weatherUtil;

    private LoadingRecyclerView homeRecommendLoadingRecyclerView;
    private LoadingRecyclerView homeWatchLaterLoadingRecyclerView;

    /**
     * 所有的推荐内容（已打乱）
     */
    private List<VideoRecommend> videoRecommendList;

    /**
     * 稍后观看数据
     */
    private List<WatchLater> watchLaterList;

    /**
     * 主页显示的推荐内容
     */
    private final List<VideoRecommend> homeVideoRecommendList = new ArrayList<>(10);
    private final List<WatchLater> homeWatchLaterList = new ArrayList<>(10);
    private TextView homeMyDownloadManager;
    private WatchLaterAdapter watchLaterAdapter;

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
                .setOnClickListener(R.id.home_setting, this)
                .setOnClickListener(R.id.home_popular, this)
                .setText(R.id.home_weekday, ValueUtils.getWeekday())
                .setText(R.id.home_date, ValueUtils.generateTime(System.currentTimeMillis(), "MM/dd", false));

        homeMyDownloadManager = findView(R.id.home_my_downloadManager);
        homeMyDownloadManager.setOnClickListener(this);

        ((CardTitle) findView(R.id.home_cardTitle_recommend)).setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(new RecommendFragment(videoRecommendList));
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

        initHandler();
        initValue();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (homeMyDownloadManager.getVisibility() == View.GONE) {
            if (PreferenceUtils.getEasterEggStat()) {
                homeMyDownloadManager.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initHandler() {
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                switch (msg.what) {
                    case 0:
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
                        break;
                    case 1:
                        // 设置推荐数据
                        int recommendColumns = PreferenceUtils.getRecommendColumns();
                        homeRecommendLoadingRecyclerView.setRecyclerViewLayoutManager(new GridLayoutManager(context, recommendColumns));
                        RecommendAdapter recommendAdapter = new RecommendAdapter(homeVideoRecommendList, recommendColumns == 1 ? RecommendAdapter.SINGLE_COLUMN : RecommendAdapter.DOUBLE_COLUMN, getMainActivity(), context);
                        recommendAdapter.setHasStableIds(true);
                        homeRecommendLoadingRecyclerView.setRecyclerViewAdapter(recommendAdapter);
                        homeRecommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        break;
                    case 2:
                        if (watchLaterList != null && watchLaterList.size() > 0) {
                            // 设置稍后再看数据
                            if (watchLaterAdapter == null) {
                                watchLaterAdapter = new WatchLaterAdapter(getMainActivity(), context);
                                watchLaterAdapter.setHasStableIds(true);
                                homeWatchLaterLoadingRecyclerView.setRecyclerViewAdapter(watchLaterAdapter);
                            } else {
                                watchLaterAdapter.removeAll();
                            }

                            watchLaterAdapter.append(homeWatchLaterList);
                            homeWatchLaterLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        } else {
                            homeWatchLaterLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initValue() {
        initBroadcastReceiver();

        weatherUtil = new WeatherUtil();

        // 初始化天气模块
        if (weatherModel == null) {
            weatherModel = new WeatherModelInterface();
            weatherModel.onInitialize(view, context);
        }

        if (!InternetUtils.checkNetwork(_mActivity.getWindow().getDecorView())) {
            homeRecommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
            homeWatchLaterLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
        } else {
            getWeatherData();
        }

        getRecommend();
        getWatchLater();
    }

    /**
     * 获取天气数据
     */
    private void getWeatherData () {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                Message message = receiveDataHandler.obtainMessage(0);
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
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     * 获取推荐数据
     */
    private void getRecommend() {
        // 设置状态为加载数据中
        homeRecommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        RecommendParser recommendParser = new RecommendParser();

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取推荐内容
                videoRecommendList = recommendParser.parseData();

                // 获取前十个数据作为主页的数据
                for (int i = 0; i < HOME_DATA_COUNT; i++) {
                    homeVideoRecommendList.add(videoRecommendList.get(i));
                }

                Message message = receiveDataHandler.obtainMessage(1);
                receiveDataHandler.sendMessage(message);
            }
        });

    }

    /**
     * 获取稍后再看数据数据
     */
    private void getWatchLater() {
        homeWatchLaterLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        WatchLaterParser watchLaterParser = new WatchLaterParser();

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取稍后观看数据
                watchLaterList = watchLaterParser.parseData();
                if (watchLaterList != null && watchLaterList.size() == 0) {
                    watchLaterList = null;
                }

                for (int i = 0; i < HOME_DATA_COUNT; i++) {
                    if (watchLaterList != null && watchLaterList.size() >= (i + 1)) {
                        homeWatchLaterList.add(watchLaterList.get(i));
                    } else {
                        break;
                    }
                }

                Message message = receiveDataHandler.obtainMessage(2);
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        SupportFragment targetFragment = null;

        switch (v.getId()) {
            case R.id.home_my_orders:
                targetFragment = OrderFragment.getInstance();
                break;
            case R.id.home_my_favorites:
                targetFragment = FavoritesFragment.getInstance();
                break;
            case R.id.home_my_follows:
                targetFragment = FollowsFragment.getInstance(false, PreferenceUtils.getUserId());
                break;
            case R.id.home_my_history:
                targetFragment = HistoryFragment.getInstance();
                break;
            case R.id.home_my_downloadManager:
                ((NavFragment) getParentFragment()).startBrotherFragment(DownloadManagerFragment.getInstance());
                return;
            case R.id.home_setting:
                ((NavFragment) getParentFragment()).startBrotherFragment(SettingsFragment.getInstance());
                return;
            case R.id.home_popular:
                targetFragment = PopularFragment.getInstance();
                break;
            default:
                break;
        }

        if (InternetUtils.checkNetwork(_mActivity.getWindow().getDecorView())) {
            if (targetFragment != null) {
                ((NavFragment) getParentFragment()).startBrotherFragment(targetFragment);
            }
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
        intentFilter.addAction(Actions.LOGIN_SUCCESS);
        intentFilter.addAction(Actions.USER_LOGOUT);

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

                    getWeatherData();
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

                    homeRecommendLoadingRecyclerView.setRecyclerViewAdapter(new RecommendAdapter(homeVideoRecommendList, recommendColumns == 1 ? RecommendAdapter.SINGLE_COLUMN : RecommendAdapter.DOUBLE_COLUMN, getMainActivity(), context));
                    homeRecommendLoadingRecyclerView.setRecyclerViewLayoutManager(new GridLayoutManager(context, recommendColumns));

                    // 设置状态为已完成加载数据
                    homeRecommendLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                    break;
                case Actions.LOGIN_SUCCESS:
                    // 登录成功后获取稍后再看数据
                    getWatchLater();
                    break;
                case Actions.USER_LOGOUT:
                    watchLaterList.clear();
                    homeVideoRecommendList.clear();

                    watchLaterAdapter.removeAll();
                    homeWatchLaterLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                    break;
                default:
                    break;
            }
        }

        /**
         * 获取当前adcode，将其设置到配置文件中
         *
         * @param address 当前位置
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
