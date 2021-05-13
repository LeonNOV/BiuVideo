package com.leon.biuvideo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.views.SimpleSnackBar;

import java.util.HashMap;
import java.util.Map;

public class InternetUtils {

    /**
     * 判断网络状态
     *
     * @param context   Context对象
     * @return  返回网络状态
     */
    public static InternetState internetState(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {

                //判断是否有网络
                if (mNetworkInfo.isAvailable()) {
                    int type = mNetworkInfo.getType();

                    //是否为未知状态
                    if (type != -1) {
                        //是否为WIFI
                        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (mWiFiNetworkInfo != null) {
                            if (mWiFiNetworkInfo.isConnectedOrConnecting()) {
                                return InternetState.INTERNET_WIFI;
                            } else {
                                //判断是否为移动网络
                                NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                if (mMobileNetworkInfo != null) {
                                    if (mMobileNetworkInfo.isConnectedOrConnecting()) {
                                        return InternetState.INTERNET_MOBILE;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return InternetState.INTERNET_NoAvailable;
    }

    /**
     * 检查网络是否可用
     *
     * @return  true：可用状态；false：不可用状态
     */
    public static boolean checkNetwork(View view) {
        Context context = view.getContext();

        InternetState internetState = internetState(context);
        boolean stat = internetState == InternetState.INTERNET_MOBILE || internetState == InternetState.INTERNET_WIFI;

        if (!stat) {
            SimpleSnackBar.make(view, context.getString(R.string.networkWarn), SimpleSnackBar.LENGTH_SHORT).show();
        }

        return stat;
    }

    /**
     * 检查网络是否可用
     *
     * @return  true：可用状态；false：不可用状态
     */
    public static boolean checkNetwork(Context context) {
        InternetState internetState = internetState(context);

        return internetState == InternetState.INTERNET_MOBILE || internetState == InternetState.INTERNET_WIFI;
    }

    public enum InternetState {
        INTERNET_NoAvailable(0),
        INTERNET_WIFI(1),
        INTERNET_MOBILE(2);

        public int value;

        InternetState(int value) {
            this.value = value;
        }
    }

    /**
     * 判断当前IP是否为中国IP
     *
     * @param context   context
     * @return  返回判断状态
     */
    public static boolean isCN (Context context) {
        final String ipApi = "http://ip-api.com/json/";

        // 获取当前IP
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo.getIpAddress();

        String ipStr = (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                ((ipAddress >> 24) & 0xFF);

        // 获取响应体
        Map<String, String> params = new HashMap<>();
        params.put("fields", "countryCode");
        JSONObject response = HttpUtils.getResponse(ipApi + ipStr, params);

        return response.getString("countryCode").equals("CN");
    }
}
