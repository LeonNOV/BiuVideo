package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
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
    private String banner = null;
    private UserInfo userInfo = null;
    private Map<String, Integer> statMap = null;

    private final Context context;

    public UserInfoParser(Context context) {
        this.context = context;
    }

    public interface OnSuccessListener{
        /**
         * 获取完数据后的回调方法
         *
         * @param userInfo  用户基本信息
         * @param banner    横幅图片
         * @param bCoins    B币数量
         * @param statMap   动态数、关注数、粉丝数
         */
        void onCallback(UserInfo userInfo, String banner, int bCoins, Map<String, Integer> statMap);
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
                if (onSuccessListener != null && userInfo != null) {
                    onSuccessListener.onCallback(userInfo, banner, bCoins, statMap);
                }
            }
        });

        // 获取横幅图片
        UserInfoThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                params.put("mid", PreferenceUtils.getUserId());
                params.put("photo", "true");

                JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_BANNER, Headers.of(HttpUtils.getAPIRequestHeader()), params);
                JSONObject data = responseObject.getJSONObject("data");

                if (data != null) {
                    banner = data.getJSONObject("space").getString("l_img");

                    if (onSuccessListener != null && banner != null) {
                        onSuccessListener.onCallback(userInfo, banner, bCoins, statMap);
                    }
                }
            }
        });

        // 获取B币数量
        UserInfoThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.USER_WALLET, Headers.of(HttpUtils.getAPIRequestHeader()), null);
                bCoins = response.getJSONObject("data").getJSONObject("accountInfo").getIntValue("defaultBp");

                if (onSuccessListener != null && bCoins != -1) {
                    onSuccessListener.onCallback(userInfo, banner, bCoins, statMap);
                }
            }
        });

        // 获取动态数、关注数、粉丝数
        UserInfoThreadPool.executor(new Runnable() {
            @Override
            public void run() {

                statMap = new HashMap<>(3);
                JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.USER_STAT, Headers.of(HttpUtils.getAPIRequestHeader()), null);
                JSONObject data = response.getJSONObject("data");

                statMap.put("following", data.getIntValue("following"));
                statMap.put("follower", data.getIntValue("follower"));
                statMap.put("dynamicCount", data.getIntValue("dynamic_count"));

                if (onSuccessListener != null && statMap != null) {
                    onSuccessListener.onCallback(userInfo, banner, bCoins, statMap);
                }
            }
        });
    }

    /**
     * 关闭线程池
     */
    public void shutDownThreadPool() {
        UserInfoThreadPool.USER_INFO_THREAD_POOL.shutdown();
    }

    /**
     * 获取用户基本数据
     * @return  UserInfo
     */
    private UserInfo getBaseData() {
        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_BASE_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), null);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            UserInfo userInfo = new UserInfo();

            userInfo.mid = data.getLongValue("mid");
            userInfo.userName = data.getString("name");
            userInfo.sex = data.getString("sex");
            userInfo.userFace = data.getString("face");

            String sign = data.getString("sign");
            userInfo.sign = "".equals(sign) ? context.getString(R.string.default_sign) : sign;
            userInfo.moral = data.getIntValue("moral");
            userInfo.birthday = ValueUtils.generateTime(data.getLongValue("birthday"), "yyyy-MM-dd", true);

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
            userInfo.isVerify = official.getIntValue("type") == 0;
            int role = official.getIntValue("role");
            userInfo.role = role == 0 ? Role.NONE : role > 2 ? Role.OFFICIAL : Role.PERSON;
            userInfo.verifyTitle = official.getString("title");
            userInfo.verifyDesc = official.getString("desc");


            return userInfo;
        }

        return null;
    }

    private static class UserInfoThreadPool  {
        private static final ThreadPoolExecutor USER_INFO_THREAD_POOL;

        static {
            USER_INFO_THREAD_POOL = new ThreadPoolExecutor(4, 4, 0,TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
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
