package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 用户登录广播接收者
 */
public class UserLoginLocalReceiver extends BroadcastReceiver {
    private OnUserLoginListener onUserLoginListener;

    public void setOnUserLoginListener(OnUserLoginListener onUserLoginListener) {
        this.onUserLoginListener = onUserLoginListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("UserLogin") || intent.getAction().equals("UserLogout")) {
            if (onUserLoginListener != null) {
                onUserLoginListener.onUserLogin();
            }
        }
    }
}
