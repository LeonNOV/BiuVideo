package com.leon.biuvideo.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.activitys.UpMasterActivity;
import com.leon.biuvideo.utils.HeroImages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ImageView hero_imageView;
    private Spinner home_spinner;
    private EditText main_editText_value;
    private Button main_button_confirm;

    private View view;
    private Context context;

    //spinner索引
    private int spinnerIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();

        initView();
        initValues();

        return view;
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
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
                //抖一抖！！！
                break;
            case R.id.home_button_confirm:

                //判断是否有网

                switch (spinnerIndex) {
                    //获取数据，进入VideoActivity
                    case 0:
//                        String value = main_editText_value.getText().toString();
//
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("bvid", value);
//
//                        String response = HttpUtils.GETByParam(Paths.view, map);
//                        ViewPage viewPage = ViewParseUtils.parseView(response);
//
//                        Intent intent = new Intent(context, VideoActivity.class);
//                        intent.putExtra("viewPage", viewPage);
//                        startActivity(intent);
                        Toast.makeText(context, "Video", Toast.LENGTH_SHORT).show();
                        break;
                    //获取数据，进入UpMasterActivity
                    case 1:

                        //获取mid
                        String mid = main_editText_value.getText().toString();

                        if (mid != null) {
                            Intent intent = new Intent(context, UpMasterActivity.class);
                            intent.putExtra("mid", Long.parseLong(mid));

                            startActivity(intent);
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

        switch (i) {
            case 0:
                main_editText_value.setHint(R.string.main_editText_video_hint);
                break;
            case 1:
                main_editText_value.setHint(R.string.main_editText_up_hint);
                break;
            default:break;
        }

        Toast.makeText(context, "选择了spinner的第" + spinnerIndex + "个", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
