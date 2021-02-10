package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ThemeColorChangeBroadcastReceiver extends BroadcastReceiver {
    private ChangeThemeColorListener changeThemeColorListener;
    private static LocalBroadcastManager localBroadcastManager;

    public interface ChangeThemeColorListener {
        void changThemeColor(int position);
    }

    public void setChangeThemeColorListener(ChangeThemeColorListener changeThemeColorListener) {
        this.changeThemeColorListener = changeThemeColorListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("themeColorChanged")) {
            if (changeThemeColorListener != null) {
                int position = intent.getIntExtra("position", 12);
                changeThemeColorListener.changThemeColor(position);
            }
        }
    }

    /**
     * 初始化广播
     * @param context   context
     */
    public void initBroadcast(Context context) {
        if (localBroadcastManager == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("themeColorChanged");

            localBroadcastManager = LocalBroadcastManager.getInstance(context);
            localBroadcastManager.registerReceiver(this, intentFilter);
        }
    }
}
