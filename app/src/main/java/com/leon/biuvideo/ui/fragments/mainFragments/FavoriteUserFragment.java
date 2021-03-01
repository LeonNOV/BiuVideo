package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.FavoriteUserAdapter;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.dialogs.LoadingDialog;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.FollowParser;
import com.sun.easysnackbar.EasySnackBar;

import java.util.List;
import java.util.Map;

/**
 * MainActivity中的收藏片段
 */
public class FavoriteUserFragment extends BaseFragment {
    private RecyclerView favorite_recyclerView;

    private FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;
    private FavoriteUserAdapter favoriteUserAdapter;
    private List<Favorite> favorites;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_favorite_up;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        favorite_recyclerView = findView(R.id.main_favorite_up_fragment_recyclerView);
        FloatingActionButton main_favorite_up_fragment_refresh = findView(R.id.main_favorite_up_fragment_refresh);
        main_favorite_up_fragment_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog loadingDialog = new LoadingDialog(context);
                loadingDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
                        String cookie = initValues.getString("cookie", null);
                        long mid = initValues.getLong("mid", 0);
                        if (cookie != null) {
                            Map<String, Long> importMap = FollowParser.getFollowings(context, mid, cookie);

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 切换到主线程，更新其UI
                                    onResume();
                                }
                            });

                            SimpleSnackBar.make(v, "导入成功" + importMap.get("successNum") + "条数据，导入失败" + importMap.get("failNum") + "条数据", SimpleSnackBar.LENGTH_LONG).show();
                        } else {
                            SimpleSnackBar.make(v, "还未登录账号哦~", SimpleSnackBar.LENGTH_LONG).show();
                        }

                        loadingDialog.dismiss();
                    }
                }).start();
            }
        });

        EasySnackBar easySnackBar = SimpleSnackBar.make(view, "建议不要频繁的刷新关注列表数据，否则本机网络IP会被封禁", SimpleSnackBar.LENGTH_INDEFINITE);
        SimpleSnackBar.setAction(easySnackBar, "我知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easySnackBar.dismiss();

                if (favorites != null && favorites.size() == 0) {
                    SimpleSnackBar.make(v, "现在还没有任何数据，试着刷新一下吧", SimpleSnackBar.LENGTH_SHORT).show();
                }
            }
        });
        easySnackBar.show();

        initBroadcast();
    }

    @Override
    public void initValues() {
        if (favoriteUserDatabaseUtils == null) {
            favoriteUserDatabaseUtils = new FavoriteUserDatabaseUtils(context);
        }

        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        boolean isVisit = initValues.getBoolean("isVisit", true);

        favorites = favoriteUserDatabaseUtils.queryFavorites(isVisit);

        if (favorites != null) {
            favoriteUserAdapter = new FavoriteUserAdapter(favorites, context);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            favorite_recyclerView.setLayoutManager(layoutManager);
            favorite_recyclerView.setAdapter(favoriteUserAdapter);
        }
    }

    @Override
    public void onResume() {
        if (favoriteUserAdapter != null) {
            SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
            boolean isVisit = initValues.getBoolean("isVisit", true);

            favorites = favoriteUserDatabaseUtils.queryFavorites(isVisit);
            favoriteUserAdapter.refresh(favorites);
        }

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (favoriteUserDatabaseUtils != null) {
            favoriteUserDatabaseUtils.close();
        }
        super.onDestroyView();
    }

    /**
     * 初始化接收者
     */
    public void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateFavoriteUp");

        DataChangeReceiver dataChangeReceiver = new DataChangeReceiver();

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(dataChangeReceiver, intentFilter);
    }

    class DataChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("updateFavoriteUp")) {
                initValues();
            }
        }
    }
}
