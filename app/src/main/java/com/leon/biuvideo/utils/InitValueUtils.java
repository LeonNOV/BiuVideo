package com.leon.biuvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class InitValueUtils {
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

    /**
     * 获取当前用户ID，如果未登录则默认UID为0
     *
     * @return  UID
     */
    public static long getUID(Context context) {
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        return initValues.getLong("mid", 0);
    }
}
