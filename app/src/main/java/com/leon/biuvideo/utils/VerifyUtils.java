package com.leon.biuvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class VerifyUtils {
    /**
     * 检查是否已登录账号
     *
     * @param context   context
     * @return  登陆状态
     */
    public static boolean isLogin(Context context) {
        SharedPreferences initValue = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        String cookie = initValue.getString("cookie", null);

        return cookie != null;
    }

    /**
     * 当前账号是否为VIP
     *
     * @param context   context
     * @return  vip状态
     */
    public static boolean isVIP(Context context) {
        SharedPreferences initValue = context.getSharedPreferences("initValue", Context.MODE_PRIVATE);
        return initValue.getBoolean("isVIP", false);
    }
}
