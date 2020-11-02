package com.leon.biuvideo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetUtils {

    //判断网络状态
    public static InternetState InternetState(Context context) {
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

    public enum InternetState {
        INTERNET_NoAvailable(0),
        INTERNET_WIFI(1),
        INTERNET_MOBILE(2);

        public int value;

        InternetState(int value) {
            this.value = value;
        }
    }
}
