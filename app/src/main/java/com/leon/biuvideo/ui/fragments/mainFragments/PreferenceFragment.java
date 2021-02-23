package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.ui.dialogs.ExportDialog;
import com.leon.biuvideo.ui.dialogs.ThanksListDialog;
import com.leon.biuvideo.ui.dialogs.FeedbackDialog;
import com.leon.biuvideo.ui.dialogs.ImportFollowDialog;
import com.leon.biuvideo.ui.dialogs.SetHeroDialog;
import com.leon.biuvideo.ui.dialogs.WarnDialog;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.dataBaseUtils.BackupLocalData;
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
    private SwitchCompat preference_switch_visitState, preference_switch_imgOriginalMode;
    private SharedPreferences initValues;

    // 用来标记是否已初始化完毕
    private boolean isInitialized = false;

    @Override
    public int setLayout() {
        return R.layout.fragment_preference;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        bindingUtils
//                .setOnCheckedChangeListener(R.id.preference_switch_isImport, this)
                .setOnClickListener(R.id.preference_textView_set_hero, this)
                .setOnClickListener(R.id.preference_textView_set_color, this)
                .setOnClickListener(R.id.preference_textView_import, this)
                .setOnClickListener(R.id.preference_textView_cache, this)
                .setOnClickListener(R.id.preference_textView_open_source_license, this)
                .setOnClickListener(R.id.preference_textView_thanks_list, this)
                .setOnClickListener(R.id.preference_switch_cleanImport, this)
                .setOnClickListener(R.id.preference_textView_feed_back, this)
                .setOnClickListener(R.id.preference_textView_exportUserData, this);

        preference_textView_cache_size = findView(R.id.preference_textView_cache_size);

        preference_switch_visitState = findView(R.id.preference_switch_visitState);
        preference_switch_visitState.setOnCheckedChangeListener(this);

        preference_switch_imgOriginalMode = findView(R.id.preference_switch_imgOriginalMode);
        preference_switch_imgOriginalMode.setOnCheckedChangeListener(this);
    }

    @Override
    public void initValues() {
        //获取缓存大小(最小单位：byte)
        File cacheDir = context.getCacheDir();

        //初始化缓存大小
        String cacheSize = ValueUtils.sizeFormat(getCacheSize(cacheDir), true);
        preference_textView_cache_size.setText(cacheSize);

        //初始化preference_switch_visitState
        initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        boolean isVisit = initValues.getBoolean("isVisit", true);
        preference_switch_visitState.setChecked(isVisit);

        boolean imgOriginalMode = initValues.getBoolean("imgOriginalMode", false);
        preference_switch_imgOriginalMode.setChecked(imgOriginalMode);

        isInitialized = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preference_textView_set_hero:
                //显示SetHeroDialog
                SetHeroDialog setHeroDialog = new SetHeroDialog(context);
                setHeroDialog.show();

                break;
            case R.id.preference_textView_set_color:
                SimpleSnackBar.make(view, "该功能将会在后期版本中上线，请谅解(。・∀・)ノ", SimpleSnackBar.LENGTH_LONG).show();
//                Intent intent = new Intent(context, ChooseThemeColorActivity.class);
//                context.startActivity(intent);

                break;
            case R.id.preference_textView_import:
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    SimpleSnackBar.make(view, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
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

                        SimpleSnackBar.make(view, "正在导入数据中，请不要随意进行任何操作", SimpleSnackBar.LENGTH_LONG).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, Long> importMap = FollowParser.getFollowings(context, mid, cookie);

                                if (importMap == null) {
                                    SimpleSnackBar.make(view, "导入成功0个，导入失败0个", SimpleSnackBar.LENGTH_LONG).show();
                                } else {
                                    SimpleSnackBar.make(view, "导入成功" + importMap.get("successNum") + "个，导入失败" + importMap.get("failNum") + "个", SimpleSnackBar.LENGTH_LONG).show();
                                    sendLocalBroadcast();
                                }
                            }
                        }).start();
                    }
                });

                break;
            case R.id.preference_textView_cache: //清除缓存
                //创建弹窗
                WarnDialog cleanCacheWarnDialog = new WarnDialog(context, "清除缓存", "是否要清除缓存？如果选择清除则之前加载过的数据将要重新加载一遍！");
                cleanCacheWarnDialog.setOnClickListener(new WarnDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        cleanCacheWarnDialog.dismiss();

                        //删除缓存
                        cleanCache(context.getCacheDir());

                        //刷新显示的缓存大小
                        preference_textView_cache_size.setText("0B");
                    }

                    @Override
                    public void onCancel() {
                        cleanCacheWarnDialog.dismiss();
                    }
                });
                cleanCacheWarnDialog.show();

                break;
            case R.id.preference_textView_open_source_license:
                // 跳转到开源许可页面
                Intent licenseIntent = new Intent();
                licenseIntent.setAction("android.intent.action.VIEW");
                licenseIntent.setData(Uri.parse("https://gitee.com/leon_xf/biu-video/blob/master/LICENSE"));
                startActivity(licenseIntent);

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
                WarnDialog cleanImportWarnDialog = new WarnDialog(context, "清除用户数据", "是否要清除当前用户（不包括本地）已关注的数据？");
                cleanImportWarnDialog.setOnClickListener(new WarnDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        FavoriteUserDatabaseUtils favoriteUserDatabaseUtils = new FavoriteUserDatabaseUtils(context);
                        boolean removeFavorite = favoriteUserDatabaseUtils.removeFavorite();

                        if (removeFavorite) {
                            SimpleSnackBar.make(v, "已删除所有来自账户中的数据", SimpleSnackBar.LENGTH_SHORT).show();
                            sendLocalBroadcast();
                        } else {
                            SimpleSnackBar.make(v, "清除失败，数据还没有导入进来哦~", SimpleSnackBar.LENGTH_SHORT).show();
                        }

                        favoriteUserDatabaseUtils.close();

                        cleanImportWarnDialog.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        cleanImportWarnDialog.dismiss();
                    }
                });

                cleanImportWarnDialog.show();
            case R.id.preference_textView_exportUserData:
                ExportDialog exportDialog = new ExportDialog(context);
                exportDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BackupLocalData backupLocalData = new BackupLocalData(context);
                        backupLocalData.execute();

                        exportDialog.dismiss();
                        SimpleSnackBar.make(v, "备份完成", SimpleSnackBar.LENGTH_SHORT).show();
                    }
                }).start();

                break;
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
        // 如果没有初始化完毕就拦截当前的监听事件
        if (!isInitialized) {
            return;
        }

        switch (buttonView.getId()) {
            case R.id.preference_switch_imgOriginalMode:
                if (isChecked) {
                    WarnDialog imgOriginalModeWarnDialog = new WarnDialog(context, "原图模式", "是否要开启原图模式？如果开启将会产生比平常更多的流量。");
                    imgOriginalModeWarnDialog.setOnClickListener(new WarnDialog.OnClickListener() {
                        @Override
                        public void onConfirm() {
                            setImgOriginalMode(true);
                            imgOriginalModeWarnDialog.dismiss();
                        }

                        @Override
                        public void onCancel() {
                            buttonView.setChecked(false);
                            imgOriginalModeWarnDialog.dismiss();
                        }
                    });
                    imgOriginalModeWarnDialog.show();
                } else {
                    setImgOriginalMode(false);
                }

                break;
            case R.id.preference_switch_visitState:
                SharedPreferences.Editor editor = initValues.edit();
                editor.putBoolean("isVisit", isChecked).apply();

                break;
            default:
                break;
        }
    }

    /**
     * 设置原图模式状态
     *
     * @param isChecked     状态
     */
    private void setImgOriginalMode(boolean isChecked) {
        SharedPreferences.Editor editor = initValues.edit();
        editor.putBoolean("imgOriginalMode", isChecked).apply();
    }


    /**
     * 清除用户关注数据后发送本地广播
     */
    private void sendLocalBroadcast() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent("updateFavoriteUp");
        localBroadcastManager.sendBroadcast(intent);
    }
}
