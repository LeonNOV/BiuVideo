package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2020/12/8
 * @Desc 用户数据解析类
 */
public class UserInfoParser {
    private OnSuccessListener onSuccessListener;
    private int bCoins = -1;
    private String banner;
    private UserInfo userInfo;

    public interface OnSuccessListener{
        void onCallback (UserInfo userInfo, String banner, int bCoins);
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    /**
     * 通过Cookie获取用户基本信息
     */
    public void parseData() {
        // 获取基本数据
        UserInfoThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                userInfo = getBaseData();
                if (onSuccessListener != null) {
                    onSuccessListener.onCallback(userInfo, banner, bCoins);
                }
            }
        });

        // 获取横幅图片
        UserInfoThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                banner = getBannerImg();
                if (onSuccessListener != null) {
                    onSuccessListener.onCallback(userInfo, banner, bCoins);
                }
            }
        });

        // 获取B币数量
        UserInfoThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                bCoins = getWalletInfo();
                if (onSuccessListener != null) {
                    onSuccessListener.onCallback(userInfo, banner, bCoins);
                }
            }
        });
    }

    /**
     * 获取用户基本数据
     * @return  UserInfo
     */
    private UserInfo getBaseData() {
        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_BASH_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), null);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            UserInfo userInfo = new UserInfo();

            userInfo.mid = data.getLongValue("mid");
            userInfo.userName = data.getString("name");
            userInfo.sex = data.getString("sex");
            userInfo.userFace = data.getString("face");
            userInfo.sign = data.getString("sign");
            userInfo.moral = data.getIntValue("moral");

            JSONObject levelExp = data.getJSONObject("level_exp");
            userInfo.currentExp = levelExp.getIntValue("current_exp");
            userInfo.currentLevel = levelExp.getIntValue("current_level");
            userInfo.totalExp = levelExp.getIntValue("next_exp");

            userInfo.coins = data.getIntValue("coins");

            userInfo.isVip = data.getIntValue("status") == 1;
            if (userInfo.isVip) {
                userInfo.vipDueDate = ValueUtils.generateTime(data.getLongValue("due_date"), "yyyy-MM-dd HH:mm:ss", false);
                userInfo.vipLabel = data.getJSONObject("label").getString("text");
            } else {
                userInfo.vipDueDate = "普通会员，无期限";
                userInfo.vipLabel = "普通会员";
            }

            JSONObject official = data.getJSONObject("official");
            int role = official.getIntValue("role");
            userInfo.role = role == -1 ? Role.NONE : role > 1 ? Role.OFFICIAL : Role.PERSON;
            userInfo.verifyDesc = official.getString("title");

            return userInfo;
        }

        return null;
    }

    /**
     * 获取BANNER图片
     *
     * @return sImg：经过裁剪的图片；lImg：原图
     */
    private String getBannerImg() {
        Map<String, String> params = new HashMap<>();
        params.put("mid", PreferenceUtils.getUserId());
        params.put("photo", "true");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_BANNER, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            return  data.getJSONObject("space").getString("l_img");
        }

        return null;
    }

    /**
     * 获取B币数量
     *
     * @return  B币数量
     */
    private int getWalletInfo() {
        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.USER_WALLET, Headers.of(HttpUtils.getAPIRequestHeader()), null);
        return response.getJSONObject("data").getJSONObject("accountInfo").getIntValue("defaultBp");
    }

    private static class UserInfoThreadPool  {
        private static final ThreadPoolExecutor USER_INFO_THREAD_POOL;

        static {
            USER_INFO_THREAD_POOL = new ThreadPoolExecutor(5, 5, 0,TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r);
                }
            });
        }

        public static void executor (Runnable runnable) {
            USER_INFO_THREAD_POOL.execute(runnable);
        }
    }
}
