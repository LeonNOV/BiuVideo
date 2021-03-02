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
import com.leon.biuvideo.utils.PermissionUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.ThanksList;
import com.leon.biuvideo.values.apis.AmapAPIs;
import com.leon.biuvideo.values.apis.AmapKey;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 设置页面
 */
public class SettingsFragment extends BaseSupportFragment implements View.OnClickListener {
    public static final String IMG_ORIGINAL_MODEL = "imgOriginalModel";
    public static final String WEATHER_MODEL = "weatherModel";

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

    public static SettingsFragment newInstance() {
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
        settings_fragment_location.setText(PreferenceUtils.getLocation(context) + "," + "未选择");

        settings_fragment_cacheSize = findView(R.id.settings_fragment_cacheSize);
        settings_fragment_cacheSize.setText(ValueUtils.sizeFormat(FileUtils.getCacheSize(context.getCacheDir()), true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_fragment_imgOriginalMode:
                setSwitchStatus(settings_fragment_imgOriginalModel_switch, IMG_ORIGINAL_MODEL, true);
                break;
            case R.id.settings_fragment_weatherModel:

                setSwitchStatus(settings_fragment_weatherModel_switch, WEATHER_MODEL, false);
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
     * @param modelName {@value WEATHER_MODEL} {@value IMG_ORIGINAL_MODEL}
     * @param isImg 是否为“开启原图模式”
     */
    private void setSwitchStatus(@SuppressLint("UseSwitchCompatOrMaterialCode") Switch mSwitch, String modelName, boolean isImg) {
        if (!mSwitch.isChecked()) {
            String title;
            String content;
            if (isImg) {
                title = "原图模式";
                content = "是否要开启原图模式？如果开启将会产生比平常更多的流量。";
            } else {
                title = "开启天气模块";
                content = "是否要开启天气模块？如果开启需要您开启定位服务。";
            }

            WarnDialog warnDialog = new WarnDialog(context, title, content);
            warnDialog.setOnWarnActionListener(new WarnDialog.OnWarnActionListener() {
                @Override
                public void onConfirm() {
                    mSwitch.setChecked(true);
                    setPreferenceStatus(modelName, true);
                    warnDialog.dismiss();

                    if (!isImg) {
                        if (permissionUtil == null) {
                            permissionUtil = new PermissionUtil(context, SettingsFragment.this);
                        }

                        // 获取定位权限
                        permissionUtil.verifyPermission(PermissionUtil.Permission.LOCATION);
                        sendBroadcast(true);
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
            setPreferenceStatus(modelName, false);

            if (!isImg) {
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
     * 设置模块状态
     *
     * @param isChecked     状态
     */
    private void setPreferenceStatus(String modelName, boolean isChecked) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(modelName, isChecked).apply();
    }

    /**
     * 发送本地广播
     */
    private void sendBroadcast(boolean status) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);

        Intent weatherModelIntent = new Intent("WeatherModel");
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
            } else {
                SimpleSnackBar.make(view, "获取权限失败", SimpleSnackBar.LENGTH_SHORT).show();
                settings_fragment_weatherModel_switch.setChecked(false);
                setPreferenceStatus(WEATHER_MODEL, false);
                sendBroadcast(false);
            }
        }
    }
}
