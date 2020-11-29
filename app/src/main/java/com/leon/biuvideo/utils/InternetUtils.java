package com.leon.biuvideo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

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
}
