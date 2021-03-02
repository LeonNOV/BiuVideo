package com.leon.biuvideo.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.AmapAPIs;
import com.leon.biuvideo.values.apis.AmapKey;
import com.leon.biuvideo.values.apis.OtherAPIs;

import java.util.HashMap;
import java.util.Map;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findFragment(NavFragment.class) == null) {
            loadRootFragment(R.id.fl_container, NavFragment.newInstance());
        }

        // 获取当前省份、城市和adcode
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取公网IP
                HttpUtils publicIPHttpUtils = new HttpUtils(OtherAPIs.souhuIP, null);
                String data = publicIPHttpUtils.getData().split(" = ")[1].replaceAll(";", "");
                JSONObject publicIPResponse = JSONObject.parseObject(data);
                String cip = publicIPResponse.getString("cip");

                Map<String, String> params = new HashMap<>();
                params.put("key", AmapKey.amapKey);
                params.put("ip", cip);

                JSONObject IPInfoResponse = HttpUtils.getResponse(AmapAPIs.amapIp, params);
                if (IPInfoResponse.getString("status").equals("1")) {
                    SharedPreferences preference = getSharedPreferences("preference", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preference.edit();
                    editor
                            .putString("adcode", IPInfoResponse.getString("adcode"))
                            .putString("province", IPInfoResponse.getString("province"))
                            .putString("city", IPInfoResponse.getString("city"))
                            .apply();
                }
            }
        }).start();*/
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
}