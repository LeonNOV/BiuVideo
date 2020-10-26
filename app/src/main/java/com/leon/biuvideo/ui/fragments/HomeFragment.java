package com.leon.biuvideo.ui.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.activitys.MainActivity;
import com.leon.biuvideo.ui.activitys.UpMasterActivity;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.GeneralNotification;
import com.leon.biuvideo.utils.IDUtils;
import com.leon.biuvideo.utils.HeroImages;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.MediaUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ImageView hero_imageView;
    private Spinner home_spinner;
    private EditText main_editText_value;
    private Button main_button_confirm;

    private View view;
    private Context context;

    //spinner索引
    private int spinnerIndex;

    //闹着玩儿专用
    int nullCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView();
        context = getActivity();

        initView();
        initValues();
    }

    private void initView() {
        hero_imageView = view.findViewById(R.id.home_imageView_hero);
        hero_imageView.setOnClickListener(this);


        home_spinner = view.findViewById(R.id.home_spinner);
        home_spinner.setOnItemSelectedListener(this);

        main_editText_value = view.findViewById(R.id.home_editText_value);

        main_button_confirm = view.findViewById(R.id.home_button_confirm);
        main_button_confirm.setOnClickListener(this);
    }

    private void initValues() {
        //设置每天打开的hero
        setHero();
    }

    /**
     * 设置hero
     */
    private void setHero() {
        SharedPreferences initValues = context.getSharedPreferences("initValues", context.MODE_PRIVATE);
        SharedPreferences.Editor edit = initValues.edit();

        //获取当前时间
        long startTime = initValues.getLong("startTime", 0);
        int heroIndex = initValues.getInt("heroIndex", 0);

        //判断是否为第一次启动
        if (startTime == 0 && heroIndex == 0) {
            heroIndex = 0;
        } else {
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

        hero_imageView.setImageResource(HeroImages.heroImages[heroIndex]);
        edit.putLong("startTime", getTime());
        edit.putInt("heroIndex", heroIndex);
        edit.apply();
    }

    /**
     * 获取的年月日
     *
     * @return  返回年月日
     */
    private long getTime() {
        Date nowDate = new Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        String ymd = sdf.format(nowDate);

        try {
            Date ymd_long = sdf.parse(ymd);
            return ymd_long.getTime();
        } catch (ParseException e) {
            Toast.makeText(context, "时间解析出错", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_imageView_hero:

                //判断是否有网
//                InternetUtils.InternetState internetState = InternetUtils.InternetState(context);
//                switch (internetState) {
//                    case INTERNET_NoAvailable:
//                        Toast.makeText(context, "无网络", Toast.LENGTH_SHORT).show();
//                        break;
//                    case INTERNET_WIFI:
//                        Toast.makeText(context, "WIFI", Toast.LENGTH_SHORT).show();
//                        break;
//                    case INTERNET_MOBILE:
//                        Toast.makeText(context, "移动网络", Toast.LENGTH_SHORT).show();
//                    default:
//                        Toast.makeText(context, "未知类型", Toast.LENGTH_SHORT).show();
//                        break;
//                }

                ActivityCompat.requestPermissions(requireActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1024);

                //抖一抖！！！
                break;
            case R.id.home_button_confirm:

                switch (spinnerIndex) {
                    //获取数据，进入VideoActivity
                    case 0:
                        String value_bvid = main_editText_value.getText().toString();

                        if (value_bvid.length() != 0) {

                            String bvid = IDUtils.getBvid(value_bvid);

                            if (bvid != null) {
                                Intent intent = new Intent(context, VideoActivity.class);
                                intent.putExtra("bvid", bvid);
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, "ERROR~~~\n可通过右上角中的帮助来了解正确的获取方式", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            switch (nullCount) {
                                case 0:
                                    Toast.makeText(context, "咋滴?搁这儿闹着玩儿呢?", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                    Toast.makeText(context, nullCount + "次了啊!", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "嫩可长点儿心吧~~~", Toast.LENGTH_SHORT).show();

                                    for (int i = 3; i > 0; i--) {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    int pid = android.os.Process.myPid();
                                    android.os.Process.killProcess(pid);
                            }

                            nullCount++;
                        }

                        Toast.makeText(context, "Video", Toast.LENGTH_SHORT).show();
                        break;
                    //获取数据，进入UpMasterActivity
                    case 1:

                        //获取mid
                        String value_mid = main_editText_value.getText().toString();

                        if (value_mid.length() != 0) {

                            long mid = IDUtils.getMid(value_mid);

                            if (mid != 0) {
                                Intent intent = new Intent(context, UpMasterActivity.class);
                                intent.putExtra("mid", mid);
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, "ERROR~~~\n可通过右上角中的帮助来了解正确的获取方式", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Toast.makeText(context, "UP", Toast.LENGTH_SHORT).show();
                        break;
                    default:break;
                }

                break;
            default:break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerIndex = i;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals("android.permission.WRITE_EXTERNAL_STORAGE") && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "权限" + permissions[i] + "申请成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
