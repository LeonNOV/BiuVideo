package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.dialogs.AboutBiuVideoDialog;
import com.leon.biuvideo.ui.dialogs.AboutDialog;
import com.leon.biuvideo.ui.dialogs.FeedbackDialog;
import com.leon.biuvideo.ui.dialogs.ImportFollowDialog;
import com.leon.biuvideo.ui.dialogs.LicenseDialog;
import com.leon.biuvideo.ui.dialogs.SetHeroDialog;
import com.leon.biuvideo.ui.dialogs.ImportStateDialog;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.dataBaseUtils.Tables;
import com.leon.biuvideo.utils.parseDataUtils.FollowParseUtils;
import com.leon.biuvideo.values.ThanksList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置/首选项Activity
 */
public class PreferenceActivity extends AppCompatActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ImageView preference_imageView_back;
    private TextView
            preference_textView_import,
            preference_textView_set_hero,
            preference_textView_cache,
            preference_textView_cache_size,
            preference_textView_about_biu_video,
            preference_textView_open_source_license,
            preference_textView_thanks_list,
            preference_textView_feed_back;

    private SwitchCompat preference_switch_visitState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        initView();
        initValue();
    }

    private void initView() {
        preference_imageView_back = findViewById(R.id.preference_imageView_back);
        preference_imageView_back.setOnClickListener(this);

        preference_textView_set_hero = findViewById(R.id.preference_textView_set_hero);
        preference_textView_set_hero.setOnClickListener(this);

        preference_textView_import = findViewById(R.id.preference_textView_import);
        preference_textView_import.setOnClickListener(this);

        preference_switch_visitState  = findViewById(R.id.preference_switch_visitState);
        preference_switch_visitState.setOnCheckedChangeListener(this);

        preference_textView_cache = findViewById(R.id.preference_textView_cache);
        preference_textView_cache.setOnClickListener(this);

        preference_textView_cache_size = findViewById(R.id.preference_textView_cache_size);

        preference_textView_about_biu_video = findViewById(R.id.preference_textView_about_biu_video);
        preference_textView_about_biu_video.setOnClickListener(this);

        preference_textView_open_source_license = findViewById(R.id.preference_textView_open_source_license);
        preference_textView_open_source_license.setOnClickListener(this);

        preference_textView_thanks_list = findViewById(R.id.preference_textView_thanks_list);
        preference_textView_thanks_list.setOnClickListener(this);

        preference_textView_feed_back = findViewById(R.id.preference_textView_feed_back);
        preference_textView_feed_back.setOnClickListener(this);

    }

    private void initValue() {
        //获取缓存大小(最小单位：byte)
        File cacheDir = getCacheDir();

        //初始化缓存大小
        String cacheSize = ValueFormat.sizeFormat(getCacheSize(cacheDir));
        preference_textView_cache_size.setText(cacheSize);

        //初始化preference_switch_visitState
        SharedPreferences initValues = getSharedPreferences("initValues", Context.MODE_PRIVATE);
        boolean isVisit = initValues.getBoolean("isVisit", true);
        preference_switch_visitState.setChecked(isVisit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.preference_imageView_back:
                this.finish();
                break;
            case R.id.preference_textView_set_hero:
                //显示SetHeroDialog
                SetHeroDialog setHeroDialog = new SetHeroDialog(PreferenceActivity.this);
                setHeroDialog.show();

                break;
            case R.id.preference_textView_import:
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetwork) {
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    return;
                }

                //导入指定ID的关注列表
                ImportFollowDialog importFollowDialog = new ImportFollowDialog(PreferenceActivity.this);
                importFollowDialog.show();

                importFollowDialog.setPriorityListener(new ImportFollowDialog.PriorityListener() {
                    @Override
                    public void setActivityText(long mid, String cookie) {

                        //隐藏importFollowDialog
                        importFollowDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "正在导入数据中，请不要随意进行任何操作", Toast.LENGTH_SHORT).show();

                        ImportStateDialog importStateDialog = new ImportStateDialog(PreferenceActivity.this);
                        importStateDialog.show();

                        boolean insertState = getFollowings(mid, cookie);

                        //修改等待对话框的显示资源
                        importStateDialog.setResourceState(insertState);
                    }
                });

                break;
            case R.id.preference_textView_cache: //清除缓存
                //创建弹窗
                AlertDialog.Builder builder = new AlertDialog.Builder(PreferenceActivity.this)
                        .setTitle("清除缓存")
                        .setMessage("是否要清除缓存？如果选择清除则之前加载过的数据将要重新加载一遍！")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                //删除缓存
                                cleanCache(getCacheDir());

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
            case R.id.preference_textView_about_biu_video:

                AboutBiuVideoDialog aboutBiuVideoDialog = new AboutBiuVideoDialog(PreferenceActivity.this);
                aboutBiuVideoDialog.show();

                Fuck.blue("CurrentFocus:" + aboutBiuVideoDialog.getCurrentFocus());

                break;
            case R.id.preference_textView_open_source_license:
                //显示开源许可
                LicenseDialog licenseDialog = new LicenseDialog(PreferenceActivity.this);
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
                AboutDialog aboutDialog = new AboutDialog(PreferenceActivity.this, aboutBeans);
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
                FeedbackDialog feedbackDialog = new FeedbackDialog(PreferenceActivity.this);
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
        if (mid != 0) {
            //获取总数
            int total = FollowParseUtils.getTotal(mid);
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

            SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.FavoriteUp);
            FavoriteDatabaseUtils favoriteDatabaseUtils = (FavoriteDatabaseUtils) sqLiteHelperFactory.getInstance();

            while (currentTotal != total) {
                List<Favorite> favorites = FollowParseUtils.parseFollow(mid, cookie, pn);
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
        SharedPreferences initValues = getSharedPreferences("initValues", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = initValues.edit();

        editor.putBoolean("isVisit", isChecked);

        editor.apply();
    }
}