package com.leon.biuvideo.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.dialogs.FeedbackDialog;
import com.leon.biuvideo.ui.dialogs.ThanksListDialog;
import com.leon.biuvideo.ui.dialogs.WarnDialog;
import com.leon.biuvideo.ui.views.BottomSheetTopBar;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LocationUtil;
import com.leon.biuvideo.utils.PermissionUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.Actions;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.ThanksList;
import com.leon.biuvideo.values.apis.AmapAPIs;
import com.leon.biuvideo.values.apis.AmapKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 设置页面
 */
public class SettingsFragment extends BaseSupportFragment implements View.OnClickListener {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch settings_fragment_imgOriginalModel_switch, settings_fragment_weatherModel_switch;

    private BottomSheetDialog bottomSheetDialog;
    private EditText set_location_keyword;
    private TextView settings_fragment_location;
    private RecyclerView set_location_result;
    private TextView settings_fragment_cacheSize;
    private SimpleTopBar settings_fragment_topBar;
    private SharedPreferences preferences;
    private PermissionUtil permissionUtil;
    private LocationUtil locationUtil;

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.settings_fragment;
    }

    @Override
    protected void initView() {
        preferences = context.getSharedPreferences(PreferenceUtils.PREFERENCE_NAME, Context.MODE_PRIVATE);

        settings_fragment_topBar = findView(R.id.settings_fragment_topBar);
        settings_fragment_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        bindingUtils
                .setOnClickListener(R.id.settings_fragment_cleanCache, this)
                .setOnClickListener(R.id.settings_fragment_setLocation, this)
                .setOnClickListener(R.id.settings_fragment_imgOriginalMode, this)
                .setOnClickListener(R.id.settings_fragment_weatherModel, this)
                .setOnClickListener(R.id.settings_fragment_open_source_license, this)
                .setOnClickListener(R.id.settings_fragment_thanks_list, this)
                .setOnClickListener(R.id.settings_fragment_feedback, this);

        settings_fragment_imgOriginalModel_switch = findView(R.id.settings_fragment_imgOriginalModel_switch);
        settings_fragment_imgOriginalModel_switch.setChecked(preferences.getBoolean("imgOriginalModel", false));

        settings_fragment_weatherModel_switch = findView(R.id.settings_fragment_weatherModel_switch);
        settings_fragment_weatherModel_switch.setChecked(preferences.getBoolean("weatherModel", false));

        settings_fragment_location = findView(R.id.settings_fragment_location);
        settings_fragment_location.setText(Arrays.toString(PreferenceUtils.getAddress(context)));

        settings_fragment_cacheSize = findView(R.id.settings_fragment_cacheSize);
        settings_fragment_cacheSize.setText(ValueUtils.sizeFormat(FileUtils.getCacheSize(context.getCacheDir()), true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_fragment_imgOriginalMode:
                setSwitchStatus(settings_fragment_imgOriginalModel_switch, FeaturesName.IMG_ORIGINAL_MODEL);
                break;
            case R.id.settings_fragment_weatherModel:
                setSwitchStatus(settings_fragment_weatherModel_switch, FeaturesName.WEATHER_MODEL);
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
                        settings_fragment_cacheSize.setText("0B");
                    }

                    @Override
                    public void onCancel() {
                        cleanCacheWarnDialog.dismiss();
                    }
                });
                cleanCacheWarnDialog.show();
                break;
            case R.id.settings_fragment_setLocation:
                showBottomSheet();
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
                ArrayList<AboutBean> aboutBeans = new ArrayList<>();
                for (int i = 0; i < ThanksList.titles.length; i++) {
                    AboutBean aboutBean = new AboutBean();
                    aboutBean.title = ThanksList.titles[i];
                    aboutBean.desc = ThanksList.desc[i];
                    aboutBean.orgUrl = ThanksList.orgUrl[i];
                    aboutBeans.add(aboutBean);
                }

                //显示Dialog
                ThanksListDialog thanksListDialog = new ThanksListDialog(context, aboutBeans);
                thanksListDialog.show();
                break;
            case R.id.settings_fragment_feedback:
                //显示反馈提交界面
                FeedbackDialog feedbackDialog = new FeedbackDialog(context);
                feedbackDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 设置开关空间
     *
     * @param mSwitch   switch对象
     * @param featuresName {@link FeaturesName}
     */
    private void setSwitchStatus(@SuppressLint("UseSwitchCompatOrMaterialCode") Switch mSwitch, FeaturesName featuresName) {
        if (!mSwitch.isChecked()) {
            // 如果定位权限已授予且是要打开天气模块，就不显示弹窗
            if (PermissionUtil.verifyPermission(context, PermissionUtil.Permission.LOCATION) && featuresName == FeaturesName.WEATHER_MODEL) {
                mSwitch.setChecked(true);
                PreferenceUtils.setFeaturesStatus(context, featuresName, true);
                sendBroadcast(true);
                return;
            }

            String title;
            String content;
            if (featuresName == FeaturesName.WEATHER_MODEL) {
                title = "授权定位服务";
                content = "是否要开启天气模块？如果开启需要您授予定位服务。";
            } else {
                title = "开启原图模式";
                content = "是否要开启原图模式？如果开启将会产生比平常更多的流量。";
            }

            WarnDialog warnDialog = new WarnDialog(context, title, content);
            warnDialog.setOnWarnActionListener(new WarnDialog.OnWarnActionListener() {
                @Override
                public void onConfirm() {
                    mSwitch.setChecked(true);
                    PreferenceUtils.setFeaturesStatus(context, featuresName, true);
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
            PreferenceUtils.setFeaturesStatus(context, featuresName, false);

            if (featuresName == FeaturesName.WEATHER_MODEL) {
                sendBroadcast(false);
            }
        }
    }

    /**
     * 显示BottomSheet
     */
    private void showBottomSheet() {
        View view = View.inflate(context, R.layout.settings_fragment_set_location_bottom_sheet, null);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        ((BottomSheetTopBar) view.findViewById(R.id.set_location_topBar)).setOnCloseListener(new BottomSheetTopBar.OnCloseListener() {
            @Override
            public void OnClose() {
                bottomSheetDialog.dismiss();
            }
        });

        set_location_keyword = view.findViewById(R.id.set_location_keyword);
        set_location_result = view.findViewById(R.id.set_location_result);

        view.findViewById(R.id.set_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = set_location_keyword.getText().toString();
                // 获取结果
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> params = new HashMap<>();
                        params.put("key", AmapKey.amapKey);
                        params.put("keywords", keyword);
                        params.put("subdistrict", "0");
                        params.put("filter", PreferenceUtils.getAdcode(context));
                        
                        JSONArray districts = HttpUtils.getResponse(AmapAPIs.amapDistrict, params).getJSONArray("districts");
                        Map<String, String> districtsMap = new HashMap<>();
                        for (Object district : districts) {
                            JSONObject object = (JSONObject) district;
                            districtsMap.put(object.getString("adcode"), object.getString("name"));
                        }

                        Log.d("Fuck-blue", districtsMap.toString());
                    }
                }).start();

                // 搜索完之后隐藏软键盘
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        // 设置底部透明
        FrameLayout bottom = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottom != null) {
            bottom.setBackgroundResource(android.R.color.transparent);
        }
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetDialog.show();
    }

    /**
     * 发送本地广播
     */
    private void sendBroadcast(boolean status) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);

        Intent weatherModelIntent = new Intent(Actions.WeatherModel);
        weatherModelIntent.putExtra("status", status);

        localBroadcastManager.sendBroadcast(weatherModelIntent);
    }

    /**
     * 定位权限回调
     *
     * @param requestCode   请求码
     * @param permissions   权限名称
     * @param grantResults  授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1025) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                SimpleSnackBar.make(view, "获取权限成功", SimpleSnackBar.LENGTH_SHORT).show();

                // 权限申请成功后发送广播
                sendBroadcast(true);

                // 设置settings_fragment_location的文字为当前的位置
                setAddress();
            } else {
                SimpleSnackBar.make(view, "获取权限失败", SimpleSnackBar.LENGTH_SHORT).show();
                settings_fragment_weatherModel_switch.setChecked(false);
                PreferenceUtils.setFeaturesStatus(context, FeaturesName.WEATHER_MODEL, false);
                sendBroadcast(false);
            }
        }
    }

    /**
     * 一般在第一次申请到定位权限后调用该方法
     * <br/>
     * 对设置界面的`settings_fragment_location`设置文字内容
     */
    private void setAddress() {
        LocationUtil locationUtil = new LocationUtil(context);
        locationUtil.location();
        settings_fragment_location.setText(Arrays.toString(locationUtil.getAddress()));
    }
}