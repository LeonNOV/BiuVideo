package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.ui.dialogs.ThanksListDialog;
import com.leon.biuvideo.ui.dialogs.FeedbackDialog;
import com.leon.biuvideo.ui.dialogs.ImportFollowDialog;
import com.leon.biuvideo.ui.dialogs.LicenseDialog;
import com.leon.biuvideo.ui.dialogs.SetHeroDialog;
import com.leon.biuvideo.ui.dialogs.WarnDialog;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.FollowParser;
import com.leon.biuvideo.values.ThanksList;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * 设置fragment
 */
public class PreferenceFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView preference_textView_cache_size;
    private SwitchCompat preference_switch_visitState;

    @Override
    public int setLayout() {
        return R.layout.fragment_preference;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        bindingUtils
//                .setOnCheckedChangeListener(R.id.preference_switch_isImport, this)
                .setOnCheckedChangeListener(R.id.preference_switch_visitState, this)
                .setOnClickListener(R.id.preference_textView_set_hero, this)
                .setOnClickListener(R.id.preference_textView_import, this)
                .setOnClickListener(R.id.preference_textView_cache, this)
                .setOnClickListener(R.id.preference_textView_open_source_license, this)
                .setOnClickListener(R.id.preference_textView_thanks_list, this)
                .setOnClickListener(R.id.preference_switch_cleanImport, this)
                .setOnClickListener(R.id.preference_textView_feed_back, this);

        preference_textView_cache_size = findView(R.id.preference_textView_cache_size);
        preference_switch_visitState = findView(R.id.preference_switch_visitState);
    }

    @Override
    public void initValues() {
        //获取缓存大小(最小单位：byte)
        File cacheDir = context.getCacheDir();

        //初始化缓存大小
        String cacheSize = ValueFormat.sizeFormat(getCacheSize(cacheDir), true);
        preference_textView_cache_size.setText(cacheSize);

        //初始化preference_switch_visitState
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        boolean isVisit = initValues.getBoolean("isVisit", true);
        preference_switch_visitState.setChecked(isVisit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preference_textView_set_hero:
                //显示SetHeroDialog
                SetHeroDialog setHeroDialog = new SetHeroDialog(context);
                setHeroDialog.show();

                break;
            case R.id.preference_textView_import:
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Snackbar.make(view, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //导入指定ID的关注列表
                ImportFollowDialog importFollowDialog = new ImportFollowDialog(context);
                importFollowDialog.show();

                importFollowDialog.setPriorityListener(new ImportFollowDialog.PriorityListener() {
                    @Override
                    public void setActivityText(long mid, String cookie) {
                        //隐藏importFollowDialog
                        importFollowDialog.dismiss();

                        Snackbar.make(view, "正在导入数据中，请不要随意进行任何操作", Snackbar.LENGTH_LONG).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, Long> importMap = FollowParser.getFollowings(context, mid, cookie);

                                if (importMap == null) {
                                    Snackbar.make(view, "导入成功0个，导入失败0个", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(view, "导入成功" + importMap.get("successNum") + "个，导入失败" + importMap.get("failNum") + "个", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }).start();
                    }
                });

                break;
            case R.id.preference_textView_cache: //清除缓存
                //创建弹窗
                WarnDialog warnDialog = new WarnDialog(context, "清除缓存", "是否要清除缓存？如果选择清除则之前加载过的数据将要重新加载一遍！");
                warnDialog.setOnConfirmListener(new WarnDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        warnDialog.dismiss();

                        //删除缓存
                        cleanCache(context.getCacheDir());

                        //刷新显示的缓存大小
                        preference_textView_cache_size.setText("0B");
                    }
                });
                warnDialog.show();

                break;
            case R.id.preference_textView_open_source_license:
                //显示开源许可
                LicenseDialog licenseDialog = new LicenseDialog(context);
                licenseDialog.show();

                break;
            case R.id.preference_textView_thanks_list:
                //设置Dialog显示内容
                ArrayList<AboutBean> aboutBeans = new ArrayList<>();
                for (int i = 0; i < ThanksList.titles.length; i++) {
                    AboutBean aboutBean = new AboutBean();
                    aboutBean.title = ThanksList.titles[i];
                    aboutBean.desc = ThanksList.desc[i];
                    aboutBean.orgUrl = ThanksList.orgUrl[i];
                    aboutBeans.add(aboutBean);
                }

                //显示Dialog
                ThanksListDialog thanksListDialog = new ThanksListDialog(context, aboutBeans);
                thanksListDialog.show();

                break;
            case R.id.preference_textView_feed_back:
                //显示反馈提交界面
                FeedbackDialog feedbackDialog = new FeedbackDialog(context);
                feedbackDialog.show();
                break;
            case R.id.preference_switch_cleanImport:
                FavoriteUserDatabaseUtils favoriteUserDatabaseUtils = new FavoriteUserDatabaseUtils(context);
                favoriteUserDatabaseUtils.removeFavorite();

                Snackbar.make(view, "已删除所有来自账户中的数据", Snackbar.LENGTH_SHORT).show();
                favoriteUserDatabaseUtils.close();

                sendLocalBroadcast();
            default:
                break;
        }
    }

    /**
     * 获取缓存文件大小
     *
     * @param cacheFile 应用cache文件夹路径
     * @return 返回cache文件夹大小(byte)
     */
    private long getCacheSize(File cacheFile) {
        long size = 0;

        File[] files = cacheFile.listFiles();
        for (File file : files) {

            //判断是否还存在有文件夹
            if (file.isDirectory()) {
                size += getCacheSize(file);
            } else {
                size += file.length();
            }
        }

        return size;
    }

    /**
     * 删除缓存
     *
     * @param cacheFile 缓存路径
     */
    private void cleanCache(File cacheFile) {
        if (cacheFile.isDirectory()) {
            String[] list = cacheFile.list();

            for (String s : list) {
                cleanCache(new File(cacheFile, s));
            }
        } else {
            cacheFile.delete();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = initValues.edit();

        editor.putBoolean("isVisit", isChecked);

        editor.apply();
    }

    /**
     * 清除用户关注数据后发送本地广播
     */
    private void sendLocalBroadcast() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent("cleanUserFavoriteUp");
        localBroadcastManager.sendBroadcast(intent);
    }
}
