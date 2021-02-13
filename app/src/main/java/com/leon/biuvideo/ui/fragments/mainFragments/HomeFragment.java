package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.activitys.SearchResultActivity;
import com.leon.biuvideo.ui.activitys.UserActivity;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.HeroImages;
import com.leon.biuvideo.utils.IDUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.sun.easysnackbar.BaseTransientBar;
import com.sun.easysnackbar.EasySnackBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 主fragment
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ImageView home_fragment_imageView_hero;
    private EditText home_fragment_editText_keyword;

    //spinner索引
    private int spinnerIndex;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_home;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        home_fragment_imageView_hero = findView(R.id.home_fragment_imageView_hero);
        home_fragment_imageView_hero.setOnClickListener(this);

        Spinner home_spinner = findView(R.id.home_spinner);
        home_spinner.setOnItemSelectedListener(this);

        home_fragment_editText_keyword = findView(R.id.home_fragment_editText_keyword);

        bindingUtils
                .setOnClickListener(R.id.home_button_confirm, this)
                .setOnClickListener(R.id.home_fragment_imageView_clear, this);
    }

    @Override
    public void initValues() {
        //判断是否为自动更换
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        boolean isAutoChange = initValues.getBoolean("isAutoChange", true);

        if (isAutoChange) {
            //设置每天打开的hero
            setHero();
        } else {
            //设置自定义的hero
            int customHeroIndex = initValues.getInt("customHeroIndex", 0);
            home_fragment_imageView_hero.setImageResource(HeroImages.heroImages[customHeroIndex]);
        }
    }

    /**
     * 设置hero
     */
    private void setHero() {
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = initValues.edit();

        //获取当前时间
        long startTime = initValues.getLong("startTime", 0);
        int heroIndex = initValues.getInt("heroIndex", 0);

        //判断是否为第一次启动
        if (startTime != 0 && heroIndex != 0) {
            //判断是否已过去一天，如果已过则设置新的hero
            if (System.currentTimeMillis() - startTime >= 86400000) {
                //达到最后一个则将index重置为0
                if (heroIndex == HeroImages.heroImages.length - 1) {
                    heroIndex = 0;
                } else {
                    //设置新的index
                    heroIndex++;
                }
            }
        }

        home_fragment_imageView_hero.setImageResource(HeroImages.heroImages[heroIndex]);
        edit.putLong("startTime", getTime());
        edit.putInt("heroIndex", heroIndex);
        edit.apply();
    }

    /**
     * 获取的年月日
     *
     * @return 返回年月日
     */
    private long getTime() {
        Date nowDate = new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        String ymd = sdf.format(nowDate);

        try {
            Date ymd_long = sdf.parse(ymd);
            return ymd_long.getTime();
        } catch (ParseException e) {
            Snackbar.make(view, "时间解析出错", Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_fragment_imageView_clear:
                home_fragment_editText_keyword.getText().clear();
                break;
            case R.id.home_fragment_imageView_hero:
                //抖一抖！！！
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.hero_anim);
                animation.setDuration(1000);
                animation.setRepeatMode(Animation.INFINITE);
                home_fragment_imageView_hero.startAnimation(animation);

                SimpleSnackBar.make(view, "显示文本", "被点击文本", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Do something...
                    }
                }, SimpleSnackBar.LENGTH_SHORT).show();
                break;
            case R.id.home_button_confirm:
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Snackbar.make(view, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                switch (spinnerIndex) {
                    case 0:
                        //获取输入内容
                        String keywordUnCoded = home_fragment_editText_keyword.getText().toString();
                        if (!keywordUnCoded.equals("")) {

                            Intent intent = new Intent(context, SearchResultActivity.class);
                            intent.putExtra("keyword", keywordUnCoded);
                            startActivity(intent);
                        } else {
                            Snackbar.make(view, "不输点儿啥吗？", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    //获取数据，进入VideoActivity
                    case 1:
                        String value_bvid = home_fragment_editText_keyword.getText().toString().trim();

                        if (value_bvid.length() != 0) {
                            String bvid = IDUtils.getBvid(value_bvid);

                            if (bvid != null) {
                                Intent intent = new Intent(context, VideoActivity.class);
                                intent.putExtra("bvid", bvid);
                                startActivity(intent);
                            } else {
                                Snackbar.make(view, "使用姿势有点不对哦~\n可通过“帮助”来了解正确的姿势", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        break;

                    //获取数据，进入UpMasterActivity
                    case 2:
                        //获取mid
                        String value_mid = home_fragment_editText_keyword.getText().toString().trim();

                        if (value_mid.length() != 0) {

                            long mid = IDUtils.getMid(value_mid);

                            if (mid != 0) {
                                Intent intent = new Intent(context, UserActivity.class);
                                intent.putExtra("mid", mid);
                                startActivity(intent);
                            } else {
                                Snackbar.make(view, "使用姿势有点不对哦~\n可通过“帮助”来了解正确的姿势", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }
    }

    /**
     * PreferenceActivity被onDestroy后调用该方法，用来初始化Hero
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initValues();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            home_fragment_editText_keyword.setHint(R.string.main_editText_hint1);
        } else {
            home_fragment_editText_keyword.setHint(R.string.main_editText_hint2);
        }
        spinnerIndex = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
