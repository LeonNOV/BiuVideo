package com.leon.biuvideo.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.SettingChoiceAddressAdapter;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.beans.District;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.WarnDialog;
import com.leon.biuvideo.ui.views.BottomSheetTopBar;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleBottomSheet;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LocationUtil;
import com.leon.biuvideo.utils.PermissionUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.Actions;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.Quality;
import com.leon.biuvideo.values.ThanksList;
import com.leon.biuvideo.values.apis.AmapAPIs;
import com.leon.biuvideo.values.apis.AmapKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 设置页面
 */
public class SettingsFragment extends BaseSupportFragment implements View.OnClickListener {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch settingsFragmentImgOriginalModelSwitch, settingsFragmentWeatherModelSwitch;

    private EditText setLocationKeyword;
    private TextView settingsFragmentLocation;
    private TextView settingsFragmentCacheSize;
    private PermissionUtil permissionUtil;

    private Handler handler;

    private SettingChoiceAddressAdapter settingChoiceAddressAdapter;
    private LoadingRecyclerView loadingRecyclerView;
    private LocalBroadcastManager localBroadcastManager;
    private SetLocationBottomSheet setLocationBottomSheet;
    private SetRecommendColumnBottomSheet setRecommendColumnBottomSheet;

    private TextView settingsFragmentPlayQuality;
    private TextView settingsFragmentDownloadQuality;

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.settings_fragment;
    }

    @Override
    protected void initView() {
        SharedPreferences preferences = context.getSharedPreferences(PreferenceUtils.PREFERENCE_NAME, Context.MODE_PRIVATE);

        SimpleTopBar settingsFragmentTopBar = findView(R.id.settings_fragment_topBar);
        settingsFragmentTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        settingsFragmentPlayQuality = findView(R.id.settings_fragment_play_quality);
        settingsFragmentPlayQuality.setText(Quality.convertQuality(PreferenceUtils.getPlayQuality()));

        if (PreferenceUtils.getEasterEggStat()) {
            settingsFragmentDownloadQuality = findView(R.id.settings_fragment_download_quality);
            settingsFragmentDownloadQuality.setText(Quality.convertQuality(PreferenceUtils.getDownloadQuality()));

            FrameLayout settingsFragmentDownloadQualityContainer = findView(R.id.settings_fragment_download_quality_container);
            settingsFragmentDownloadQualityContainer.setVisibility(View.VISIBLE);
            settingsFragmentDownloadQualityContainer.setOnClickListener(this);
        }

        bindingUtils
                .setOnClickListener(R.id.settings_fragment_cleanCache, this)
                .setOnClickListener(R.id.settings_fragment_setLocation, this)
                .setOnClickListener(R.id.settings_fragment_imgOriginalMode, this)
                .setOnClickListener(R.id.settings_fragment_weatherModel, this)
                .setOnClickListener(R.id.settings_fragment_play_quality_container, this)
                .setOnClickListener(R.id.settings_fragment_open_source_license, this)
                .setOnClickListener(R.id.settings_fragment_thanks_list, this)
                .setOnClickListener(R.id.settings_fragment_recommend_span_count, this)
                .setOnClickListener(R.id.settings_fragment_feedback, this);

        settingsFragmentImgOriginalModelSwitch = findView(R.id.settings_fragment_imgOriginalModel_switch);
        settingsFragmentImgOriginalModelSwitch.setChecked(preferences.getBoolean("imgOriginalModel", false));

        settingsFragmentWeatherModelSwitch = findView(R.id.settings_fragment_weatherModel_switch);
        settingsFragmentWeatherModelSwitch.setChecked(preferences.getBoolean("weatherModel", false));

        settingsFragmentLocation = findView(R.id.settings_fragment_location);
        settingsFragmentLocation.setText(Arrays.toString(PreferenceUtils.getAddress()));

        settingsFragmentCacheSize = findView(R.id.settings_fragment_cacheSize);
        settingsFragmentCacheSize.setText(ValueUtils.sizeFormat(FileUtils.getCacheSize(context.getCacheDir()), true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_fragment_imgOriginalMode:
                setSwitchStatus(settingsFragmentImgOriginalModelSwitch, FeaturesName.IMG_ORIGINAL_MODEL);
                break;
            case R.id.settings_fragment_weatherModel:
                setSwitchStatus(settingsFragmentWeatherModelSwitch, FeaturesName.WEATHER_MODEL);
                break;
            case R.id.settings_fragment_cleanCache:
                //创建弹窗
                WarnDialog cleanCacheWarnDialog = new WarnDialog(context, "清除缓存", "是否要清除缓存？如果选择清除则之前加载过的数据将要重新加载一遍！");
                cleanCacheWarnDialog.setOnWarnActionListener(new WarnDialog.OnWarnActionListener() {
                    @Override
                    public void onConfirm() {
                        cleanCacheWarnDialog.dismiss();

                        //删除缓存
                        FileUtils.cleanCache(context.getCacheDir());

                        //刷新显示的缓存大小
                        settingsFragmentCacheSize.setText(getText(R.string.setting_default_cacheSize));
                    }

                    @Override
                    public void onCancel() {
                        cleanCacheWarnDialog.dismiss();
                    }
                });
                cleanCacheWarnDialog.show();
                break;
            case R.id.settings_fragment_setLocation:
                if (setLocationBottomSheet == null) {
                    setLocationBottomSheet = new SetLocationBottomSheet();
                }

                setLocationBottomSheet.showSetLocationBottomSheet();
                break;
            case R.id.settings_fragment_play_quality_container:
                QualityBottomSheet playQualityBottomSheet = new QualityBottomSheet(context, true);
                playQualityBottomSheet.setOnClickQualityItemListener(new QualityBottomSheet.QualityBottomSheetAdapter.OnClickQualityItemListener() {
                    @Override
                    public void onClick(String quality, int qualityCode) {
                        PreferenceUtils.setPlayQuality(qualityCode);

                        settingsFragmentPlayQuality.setText(quality);
                        playQualityBottomSheet.dismiss();
                    }
                });
                playQualityBottomSheet.show();
                break;
            case R.id.settings_fragment_download_quality_container:
                QualityBottomSheet downloadQualityBottomSheet = new QualityBottomSheet(context, false);
                downloadQualityBottomSheet.setOnClickQualityItemListener(new QualityBottomSheet.QualityBottomSheetAdapter.OnClickQualityItemListener() {
                    @Override
                    public void onClick(String quality, int qualityCode) {
                        PreferenceUtils.setPlayQuality(qualityCode);

                        settingsFragmentDownloadQuality.setText(quality);
                        downloadQualityBottomSheet.dismiss();
                    }
                });
                downloadQualityBottomSheet.show();
                break;
            case R.id.settings_fragment_open_source_license:
                // 跳转到开源许可页面
                Intent licenseIntent = new Intent();
                licenseIntent.setAction("android.intent.action.VIEW");
                licenseIntent.setData(Uri.parse("https://gitee.com/leon_xf/biu-video/blob/master/LICENSE"));
                startActivity(licenseIntent);
                break;
            case R.id.settings_fragment_thanks_list:
                //设置Dialog显示内容
                /*ArrayList<AboutBean> aboutBeans = new ArrayList<>();
                for (int i = 0; i < ThanksList.titles.length; i++) {
                    AboutBean aboutBean = new AboutBean();
                    aboutBean.title = ThanksList.titles[i];
                    aboutBean.desc = ThanksList.desc[i];
                    aboutBean.orgUrl = ThanksList.orgUrl[i];
                    aboutBeans.add(aboutBean);
                }

                //显示Dialog
                ThanksListDialog thanksListDialog = new ThanksListDialog(context, aboutBeans);
                thanksListDialog.show();*/
                break;
            case R.id.settings_fragment_recommend_span_count:
                if (setRecommendColumnBottomSheet == null) {
                    setRecommendColumnBottomSheet = new SetRecommendColumnBottomSheet();
                }
                setRecommendColumnBottomSheet.showSetRecommendColumnBottomSheet();
                break;
            case R.id.settings_fragment_feedback:
                //显示反馈提交界面
//                FeedbackDialog feedbackDialog = new FeedbackDialog(context);
//                feedbackDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 设置开关空间
     *
     * @param mSwitch      switch对象
     * @param featuresName {@link FeaturesName}
     */
    private void setSwitchStatus(@SuppressLint("UseSwitchCompatOrMaterialCode") Switch mSwitch, FeaturesName featuresName) {
        if (!mSwitch.isChecked()) {
            // 如果定位权限已授予且是要打开天气模块，就不显示弹窗
            if (PermissionUtil.verifyPermission(context, PermissionUtil.Permission.LOCATION) && featuresName == FeaturesName.WEATHER_MODEL) {
                mSwitch.setChecked(true);
                PreferenceUtils.setFeaturesStatus(featuresName, true);
                sendBroadcast(true);
                return;
            }

            String title;
            String content;
            if (featuresName == FeaturesName.WEATHER_MODEL) {
                title = "开启天气模块";
                content = "您可以选择是否授予定位服务\n\n如果授予，则能根据您位置的变动来自动设置天气预报位置\n如果不授予则可以通过此页面的”设置位置“来手动定位。";
            } else {
                title = "开启原图模式";
                content = "是否要开启原图模式？如果开启将会产生比平常更多的流量。";
            }

            WarnDialog warnDialog = new WarnDialog(context, title, content);
            warnDialog.setOnWarnActionListener(new WarnDialog.OnWarnActionListener() {
                @Override
                public void onConfirm() {
                    mSwitch.setChecked(true);
                    PreferenceUtils.setFeaturesStatus(featuresName, true);
                    warnDialog.dismiss();

                    if (featuresName == FeaturesName.WEATHER_MODEL) {
                        // 检查定位权限是否已获取到,如果未授予就申请获取定位权限
                        if (!PermissionUtil.verifyPermission(context, PermissionUtil.Permission.LOCATION)) {
                            if (permissionUtil == null) {
                                permissionUtil = new PermissionUtil(context, SettingsFragment.this);
                            }

                            // 获取定位权限
                            permissionUtil.verifyPermission(PermissionUtil.Permission.LOCATION);
                        }
                    }
                }

                @Override
                public void onCancel() {
                    warnDialog.dismiss();
                }
            });
            warnDialog.show();

        } else {
            mSwitch.setChecked(false);
            PreferenceUtils.setFeaturesStatus(featuresName, false);

            if (featuresName == FeaturesName.WEATHER_MODEL) {
                sendBroadcast(false);
            }
        }
    }

    /**
     * 获取地址信息
     *
     * @param keyword 搜索关键字
     */
    private void getDistrict(String keyword) {
        Map<String, String> params = new HashMap<>(3);
        params.put("key", AmapKey.amapKey);
        params.put("keywords", keyword);
        params.put("subdistrict", "0");

        JSONArray districts = HttpUtils.getResponse(AmapAPIs.amapDistrict, params).getJSONArray("districts");
        List<District> districtListTemp = new ArrayList<>(districts.size());
        for (Object object : districts) {
            JSONObject jsonObject = (JSONObject) object;
            District district = new District();

            district.citycode = jsonObject.getString("citycode");
            district.adcode = jsonObject.getString("adcode");
            district.name = jsonObject.getString("name");
            district.level = jsonObject.getString("level");

            String[] centers = jsonObject.getString("center").split(",");
            district.longitude = Double.parseDouble(centers[0]);
            district.latitude = Double.parseDouble(centers[1]);

            // 根据经纬度获取位置信息
            district.address = LocationUtil.geoLocation(context, district.latitude, district.longitude);

            districtListTemp.add(district);
        }

        // 发送消息
        Message message = handler.obtainMessage();
        message.obj = districtListTemp;
        handler.sendMessage(message);
    }

    /**
     * 对设置界面的`settings_fragment_location`设置文字内容
     */
    private void setAddress() {
        String[] address = {"?", "?", "?"};
        if (PreferenceUtils.getLocationServiceStatus()) {
            LocationUtil locationUtil = new LocationUtil(context);
            locationUtil.location();
            address = locationUtil.getAddress();
        } else if (PreferenceUtils.getManualSetLocationStatus()) {
            address = PreferenceUtils.getAddress();
        }
        settingsFragmentLocation.setText(Arrays.toString(address));
    }

    /**
     * 发送本地广播
     */
    private void sendBroadcast(boolean weatherModelStatus) {
        if (localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
        }

        Intent weatherModelIntent = new Intent(Actions.WEATHER_MODEL);
        weatherModelIntent.putExtra("weatherModelStatus", weatherModelStatus);

        localBroadcastManager.sendBroadcast(weatherModelIntent);
    }

    /**
     * 发送本地广播
     */
    private void sendBroadcast(int recommendColumns) {
        if (localBroadcastManager == null) {
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
        }

        Intent weatherModelIntent = new Intent(Actions.REFRESH_RECOMMEND_STYLE);
        weatherModelIntent.putExtra("recommendColumns", recommendColumns);

        localBroadcastManager.sendBroadcast(weatherModelIntent);
    }

    /**
     * 定位权限回调
     *
     * @param requestCode  请求码
     * @param permissions  权限名称
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1025) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                SimpleSnackBar.make(view, "已授予定位服务", SimpleSnackBar.LENGTH_SHORT).show();

                PreferenceUtils.setLocationServiceStatus(true);

                // 权限申请成功后发送广播
                sendBroadcast(true);

                // 设置settings_fragment_location的文字为当前的位置
                setAddress();

            } else {
                SimpleSnackBar.make(view, "未授予定位服务，请通过”设置位置“来设置天气预报的位置", SimpleSnackBar.LENGTH_SHORT).show();
                settingsFragmentWeatherModelSwitch.setChecked(true);
                PreferenceUtils.setFeaturesStatus(FeaturesName.WEATHER_MODEL, true);
                sendBroadcast(true);
                PreferenceUtils.setLocationServiceStatus(false);
            }
        }
    }

    private class SetLocationBottomSheet {
        private SimpleBottomSheet setLocationBottomSheet;
        private View setLocationBottomSheetView;
        private BottomSheetDialog setLocationBottomSheetDialog;

        /**
         * 显示BottomSheet
         */
        private void showSetLocationBottomSheet() {
            // 初始化BottomSheet
            initSetLocationBottomSheet();
            setLocationBottomSheetDialog.show();

            handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    if (settingChoiceAddressAdapter.getItemCount() > 0) {
                        settingChoiceAddressAdapter.removeAll();
                    }

                    List<District> districtList = (List<District>) msg.obj;

                    if (districtList != null && districtList.size() > 0) {
                        settingChoiceAddressAdapter.append(districtList);
                        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                    } else {
                        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                    }

                    return true;
                }
            });

            setLocationBottomSheetView.findViewById(R.id.set_location_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String keyword;
                    String s = setLocationKeyword.getText().toString().replaceAll(" ", "");
                    if (s.matches("^[\\u4e00-\\u9fa5]*$") && s.length() > 0) {
                        keyword = s;

                        // 如果匹配成功就显示ProgressBar、隐藏NoData和recyclerView
                        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
                    } else {
                        Toast.makeText(context, "请输入正确的城市名称（不能含有中文以外的字符）", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 获取结果
                    SimpleSingleThreadPool.executor(new Runnable() {
                        @Override
                        public void run() {
                            // 获取地址信息
                            getDistrict(keyword);
                        }
                    });

                    // 搜索完之后隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) setLocationBottomSheetView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(setLocationBottomSheetView.getWindowToken(), 0);
                }
            });

            setLocationBottomSheetView.findViewById(R.id.set_location_clearKeyword).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocationKeyword.getText().clear();
                }
            });
        }

        /**
         * 初始化BottomSheet
         */
        private void initSetLocationBottomSheet() {
            if (setLocationBottomSheet == null) {
                setLocationBottomSheet = new SimpleBottomSheet(context, R.layout.settings_fragment_set_location_bottom_sheet);
                setLocationBottomSheetView = setLocationBottomSheet.initView();
                setLocationBottomSheetDialog = setLocationBottomSheet.bottomSheetDialog;
            }

            setLocationKeyword = setLocationBottomSheetView.findViewById(R.id.set_location_keyword);
            loadingRecyclerView = setLocationBottomSheetView.findViewById(R.id.set_location_data);

            if (settingChoiceAddressAdapter == null) {
                List<District> districtList = new ArrayList<>();
                settingChoiceAddressAdapter = new SettingChoiceAddressAdapter(districtList, context);
                settingChoiceAddressAdapter.setHasStableIds(true);
                settingChoiceAddressAdapter.setOnSettingChoiceAddressListener(new SettingChoiceAddressAdapter.OnSettingChoiceAddressListener() {
                    @Override
                    public void onSelectAddress(District district) {
                        // 设置手动设置状态为true
                        PreferenceUtils.setManualSetLocationStatus(true);

                        // 设置位置信息
                        PreferenceUtils.setAddress(district.address);

                        // 设置adcode
                        PreferenceUtils.setAdcode(district.adcode);

                        // 发送广播
                        sendBroadcast(true);

                        // 显示已选择的位置
                        setAddress();

                        SimpleSnackBar.make(view, "已手动设置位置为:" + district.address[0] + "," + district.address[1] + "," + district.address[2], SimpleSnackBar.LENGTH_LONG).show();

                        setLocationBottomSheetDialog.dismiss();
                    }
                });
                loadingRecyclerView.setRecyclerViewAdapter(settingChoiceAddressAdapter);
            }

            // 设置底部透明
            FrameLayout bottom = setLocationBottomSheetDialog.findViewById(R.id.design_bottom_sheet);
            if (bottom != null) {
                bottom.setBackgroundResource(android.R.color.transparent);
            }
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) setLocationBottomSheetView.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private class SetRecommendColumnBottomSheet implements View.OnClickListener {
        private static final int SINGLE_COLUMN = 1;
        private static final int DOUBLE_COLUMN = 2;

        private final int recommendColumns = PreferenceUtils.getRecommendColumns();
        private int recommendColumnTemp = recommendColumns;

        private final Drawable SELECTED_BG = context.getDrawable(R.drawable.ic_selected);
        private final Drawable UNSELECTED_BG = context.getDrawable(R.drawable.ripple_round_corners6dp_no_padding_bg);

        private View setRecommendColumnBottomSheetView;
        private BottomSheetDialog setRecommendColumnBottomSheetDialog;

        private FrameLayout setRecommendColumnShowSingleColumn;
        private FrameLayout setRecommendColumnShowDoubleColumn;
        private SimpleBottomSheet setRecommendColumnBottomSheet;

        private void showSetRecommendColumnBottomSheet() {
            if (setRecommendColumnBottomSheet == null) {
                setRecommendColumnBottomSheet = new SimpleBottomSheet(context, R.layout.settings_fragment_set_recommend_column_bottom_sheet);
                setRecommendColumnBottomSheetView = setRecommendColumnBottomSheet.initView();
                setRecommendColumnBottomSheetDialog = setRecommendColumnBottomSheet.bottomSheetDialog;
                setRecommendColumnBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // 如果选择的column和配置文件中的一样，则不发送广播
                        if (recommendColumnTemp == recommendColumns) {
                            return;
                        }

                        PreferenceUtils.setRecommendColumns(recommendColumnTemp);

                        // setRecommendColumnBottomSheetDialog消失后发送广播
                        sendBroadcast(recommendColumnTemp);
                    }
                });
            }

            setRecommendColumnShowSingleColumn = setRecommendColumnBottomSheetView.findViewById(R.id.set_recommend_column_showSingleColumn);
            setRecommendColumnShowSingleColumn.setOnClickListener(this);

            setRecommendColumnShowDoubleColumn = setRecommendColumnBottomSheetView.findViewById(R.id.set_recommend_column_showDoubleColumn);
            setRecommendColumnShowDoubleColumn.setOnClickListener(this);

            setOptionsStyle(recommendColumns);

            setRecommendColumnBottomSheetDialog.show();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set_recommend_column_showSingleColumn:
                    if (recommendColumnTemp == SINGLE_COLUMN) {
                        return;
                    }

                    recommendColumnTemp = SINGLE_COLUMN;

                    setOptionsStyle(SINGLE_COLUMN);
                    break;
                case R.id.set_recommend_column_showDoubleColumn:
                    if (recommendColumnTemp == DOUBLE_COLUMN) {
                        return;
                    }

                    recommendColumnTemp = DOUBLE_COLUMN;

                    setOptionsStyle(DOUBLE_COLUMN);
                    break;
                default:
                    break;
            }
        }

        private void setOptionsStyle(int recommendColumn) {
            if (recommendColumn == SINGLE_COLUMN) {
                setRecommendColumnShowSingleColumn.setBackground(SELECTED_BG);
                setRecommendColumnShowDoubleColumn.setBackground(UNSELECTED_BG);
            } else {
                setRecommendColumnShowDoubleColumn.setBackground(SELECTED_BG);
                setRecommendColumnShowSingleColumn.setBackground(UNSELECTED_BG);
            }
        }
    }
}