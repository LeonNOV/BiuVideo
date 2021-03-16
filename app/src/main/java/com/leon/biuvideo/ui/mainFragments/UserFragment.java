package com.leon.biuvideo.ui.mainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.otherFragments.LoginFragment;
import com.leon.biuvideo.ui.user.UserInfoFragment;
import com.leon.biuvideo.ui.views.CardTitle;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.UserInfoParser;
import com.leon.biuvideo.values.Actions;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 用户页面
 */
public class UserFragment extends BaseSupportFragment {
    private Handler handler;
    private UserDataView userDataView;

    @Override
    protected int setLayout() {
        return R.layout.user_fragment;
    }

    @Override
    protected void initView() {
        // 获取用户数据
        getAccountInfo();

        // 初始化控件
        userDataView = new UserDataView();
        userDataView.initDataView();

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                // 刷新用户信息
                UserInfo userInfo = (UserInfo) msg.obj;

                // 设置数据
                userDataView.setData(userInfo);

                return true;
            }
        });
        initBroadcastReceiver();
    }

    /**
     * 使用单线程，获取用户数据
     */
    private void getAccountInfo() {
        // 判断是否已登录
        if (!PreferenceUtils.getLoginStatus()) {
//            SimpleSnackBar.make(view, "登录之后才能查看账户信息", SimpleSnackBar.LENGTH_SHORT).show();
            return;
        }

        // 获取用户数据
        UserInfoParser userInfoParser = new UserInfoParser();
        userInfoParser.parseData();
        userInfoParser.setOnSuccessListener(new UserInfoParser.OnSuccessListener() {
            @Override
            public void onCallback(UserInfo userInfo, String banner, int bCoins, Map<String, Integer> statMap) {
                if (userInfo != null && banner != null && bCoins != -1 && statMap != null) {
                    userInfoParser.shutDownThreadPool();

                    userInfo.banner = banner;
                    userInfo.bCoinBalance = bCoins;
                    userInfo.follows = statMap.get("following");
                    userInfo.fans = statMap.get("follower");
                    userInfo.dynamics = statMap.get("dynamicCount");


                    Message message = handler.obtainMessage();
                    message.obj = userInfo;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 初始化本地广播接收器
     */
    private void initBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.LOGIN_SUCCESS);

        LocalReceiver localReceiver = new LocalReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    private class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Actions.LOGIN_SUCCESS.equals(action)) {
                boolean loginStatus = intent.getBooleanExtra("loginStatus", false);

                if (loginStatus) {
                    // 获取用户数据
                    getAccountInfo();
                }
            }
        }
    }

    /**
     * 操作控件类
     */
    private class UserDataView implements View.OnClickListener {
        private ImageView userVipMark;
        private TagView userFavTagView;
        private TagView userFansTagView;
        private LinearLayout userVerify;
        private ImageView userVerifyMark;
        private TextView userVerifyDesc;
        private LinearLayout userBaseInfoLinearLayout;
        private LinearLayout userAccountInfoLinearLayout;
        private LoadingRecyclerView userFavoriteLoadingRecyclerView;
        private LoadingRecyclerView userBangumiLoadingRecyclerView;
        private LoadingRecyclerView userCoinLoadingRecyclerView;
        private TagView userBaseInfoLevel;
        private TagView userBaseInfoGender;
        private TagView userBaseInfoName;
        private TagView userBaseInfoUid;
        private ImageView userBanner;
        private TextView userTopName;
        private CircleImageView userFace;
        private TextView userAccountInfLevel;
        private TextView userAccountInfVipValid;
        private TextView userAccountInfVipMark;
        private TextView userAccountInfCoin;
        private TextView userAccountInfMoney;
        private ProgressBar userAccountInfExProgress;
        private TextView userAccountInfEx;

        /**
         * 初始化所有控件
         */
        public void initDataView () {
            initTopViews();
            initBaseInfoViews();
            initAccountViews();
            initLoadingRecyclerView();

            findView(R.id.user_logout).setOnClickListener(this);

            userBaseInfoLinearLayout = findView(R.id.user_baseInfo_linearLayout);
            userAccountInfoLinearLayout = findView(R.id.user_accountInfo_linearLayout);
        }

        /**
         * 初始化顶部视图
         */
        private void initTopViews() {
            userBanner = findView(R.id.user_banner);
            userFace = findView(R.id.user_face);
            userFace.setOnClickListener(this);

            userVipMark = findView(R.id.user_vip_mark);
            userTopName = findView(R.id.user_top_name);
            userFavTagView = findView(R.id.user_top_fav);
            userFansTagView = findView(R.id.user_top_fans);
        }

        /**
         * 初始化基本信息视图
         */
        private void initBaseInfoViews() {
            userVerify = findView(R.id.user_baseInfo_verify);
            userVerifyMark = findView(R.id.user_baseInfo_verify_mark);
            userVerifyDesc = findView(R.id.user_baseInfo_verify_desc);

            userBaseInfoUid = findView(R.id.user_baseInfo_uid);
            userBaseInfoName = findView(R.id.user_baseInfo_name);
            userBaseInfoGender = findView(R.id.user_baseInfo_gender);
            userBaseInfoLevel = findView(R.id.user_baseInfo_level);

            TextView userInfoCheckAll = findView(R.id.user_baseInfo_check_all);
        }

        /**
         * 初始化账户信息视图
         */
        private void initAccountViews() {
            userAccountInfLevel = findView(R.id.user_accountInf_level);
            userAccountInfEx = findView(R.id.user_accountInf_ex);
            userAccountInfExProgress = findView(R.id.user_accountInf_ex_progress);
            userAccountInfMoney = findView(R.id.user_accountInf_money);
            userAccountInfCoin = findView(R.id.user_accountInf_coin);
            userAccountInfVipMark = findView(R.id.user_accountInf_vipMark);
            userAccountInfVipValid = findView(R.id.user_accountInf_vipValid);
        }

        /**
         * 初始化LoadingRecyclerView
         */
        private void initLoadingRecyclerView() {
            CardTitle userFavoritesCardTitle = findView(R.id.user_favorite_cardTitle);
            userFavoritesCardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
                @Override
                public void onClickAction() {
                    ((NavFragment) getParentFragment()).startBrotherFragment(FavoritesFragment.getInstance());
                }
            });

            CardTitle userBangumiCardTitle = findView(R.id.user_bangumi_cardTitle);
            userBangumiCardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
                @Override
                public void onClickAction() {
                    Toast.makeText(context, "订阅的番剧", Toast.LENGTH_SHORT).show();
                }
            });

            CardTitle userCoinCardTitle = findView(R.id.user_coin_cardTitle);
            userCoinCardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
                @Override
                public void onClickAction() {
                    Toast.makeText(context, "最近投币的视频", Toast.LENGTH_SHORT).show();
                }
            });

            userFavoriteLoadingRecyclerView = findView(R.id.user_favorite_loadingRecyclerView);
            userBangumiLoadingRecyclerView = findView(R.id.user_bangumi_loadingRecyclerView);
            userCoinLoadingRecyclerView = findView(R.id.user_coin_loadingRecyclerView);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_baseInfo_check_all:
                    ((NavFragment) getParentFragment()).startBrotherFragment(UserInfoFragment.getInstance());
                    break;
                case R.id.user_face:
                    ((NavFragment) getParentFragment()).startBrotherFragment(LoginFragment.getInstance());
                    break;
                case R.id.user_logout:
                    Toast.makeText(context, "退出操作", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        public void setData(UserInfo userInfo) {
            // 如果未登录，则不设置数据
            if (PreferenceUtils.getLoginStatus()) {
            }
        }
    }
}
