package com.leon.biuvideo.ui.fragments.mainFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.dialogs.AboutDialog;
import com.leon.biuvideo.ui.dialogs.FeedbackDialog;
import com.leon.biuvideo.ui.dialogs.ImportFollowDialog;
import com.leon.biuvideo.ui.dialogs.ImportStateDialog;
import com.leon.biuvideo.ui.dialogs.LicenseDialog;
import com.leon.biuvideo.ui.dialogs.SetHeroDialog;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;
import com.leon.biuvideo.utils.parseDataUtils.FollowParse;
import com.leon.biuvideo.values.ThanksList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置fragment
 */
public class PreferenceFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView preference_textView_cache_size;
    private SwitchCompat preference_switch_visitState;
    private FollowParse followParse;

    @Override
    public int setLayout() {
        return R.layout.fragment_preference;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        bindingUtils
                .setOnClickListener(R.id.preference_textView_set_hero, this)
                .setOnClickListener(R.id.preference_textView_import, this)
                .setOnCheckedChangeListener(R.id.preference_switch_visitState, this)
                .setOnClickListener(R.id.preference_textView_cache, this)
                .setOnClickListener(R.id.preference_textView_open_source_license, this)
                .setOnClickListener(R.id.preference_textView_thanks_list, this)
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
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(context, "正在导入数据中，请不要随意进行任何操作", Toast.LENGTH_SHORT).show();

                        ImportStateDialog importStateDialog = new ImportStateDialog(context);
                        importStateDialog.show();

                        boolean insertState = getFollowings(mid, cookie);

                        //修改等待对话框的显示资源
                        importStateDialog.setResourceState(insertState);
                    }
                });

                break;
            case R.id.preference_textView_cache: //清除缓存
                //创建弹窗
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("清除缓存")
                        .setMessage("是否要清除缓存？如果选择清除则之前加载过的数据将要重新加载一遍！")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                //删除缓存
                                cleanCache(context.getCacheDir());

                                //刷新显示的缓存大小
                                preference_textView_cache_size.setText("0B");
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();

                break;
            case R.id.preference_textView_open_source_license:
                //显示开源许可
                LicenseDialog licenseDialog = new LicenseDialog(context);
                licenseDialog.show();

                break;
            case R.id.preference_textView_thanks_list:
                //显示感谢列表

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
                AboutDialog aboutDialog = new AboutDialog(context, aboutBeans);
                aboutDialog.setOnClickBottomListener(new AboutDialog.OnClickBottomListener() {
                    @Override
                    public void onCloseClick() {
                        aboutDialog.dismiss();
                    }
                });
                aboutDialog.show();

                break;
            case R.id.preference_textView_feed_back:
                //显示反馈提交界面
                FeedbackDialog feedbackDialog = new FeedbackDialog(context);
                feedbackDialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 获取关注列表并进行添加
     *
     * @param mid   用户ID
     * @param cookie 用户cookie
     * @return  返回插入状态
     */
    private boolean getFollowings(long mid, String cookie) {
        if (followParse == null) {
            followParse = new FollowParse(context, mid);
        }

        if (mid != 0) {
            //获取总数
            int total = followParse.getTotal();
            if (total == 0) {
                return true;
            }

            //由于官方的限制
            //在没有cookie的情况下，关注列表最多只能获取100条
            if (total > 100 && cookie.equals("")) {
                total = 100;
            }

            //获取数据
            int pn = 1;
            int currentTotal = 0;

            SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.FavoriteUp);
            FavoriteDatabaseUtils favoriteDatabaseUtils = (FavoriteDatabaseUtils) sqLiteHelperFactory.getInstance();

            while (currentTotal != total) {
                List<Favorite> favorites = followParse.parseFollow(pn);
                if (favorites != null) {
                    currentTotal += favorites.size();

                    //将数据添加至favorites_up
                    for (Favorite favorite : favorites) {
                        boolean insertState = favoriteDatabaseUtils.addFavorite(favorite);

                        if (!insertState) {
                            return false;
                        }
                    }

                    pn++;
                } else {
                    return false;
                }
            }

            favoriteDatabaseUtils.close();

            return true;
        } else {
            return false;
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
}
