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
import com.leon.biuvideo.beans.view.ViewPage;
import com.leon.biuvideo.ui.VideoActivity;
import com.leon.biuvideo.utils.HeroImages;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.parseUtils.ViewParseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VideoFragment extends Fragment implements View.OnClickListener {
    private ImageView hero_imageView;
    private EditText main_editText_value;
    private Button main_button_confirm;

    private View view;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        context = view.getContext();

        initView();
        initValues();

        return view;
    }

    private void initView() {
        hero_imageView = view.findViewById(R.id.hero_imageView);
        hero_imageView.setOnClickListener(this);

        main_editText_value = view.findViewById(R.id.main_editText_value);

        main_button_confirm = view.findViewById(R.id.main_button_confirm);
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
            case R.id.hero_imageView:

                break;
            case R.id.main_button_confirm:

                //判断是否有网


                String value = main_editText_value.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("bvid", value);

                String response = HttpUtils.GETByParam(Paths.view, map);
                ViewPage viewPage = ViewParseUtils.parseView(response);

                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("viewPage", viewPage);
                startActivity(intent);

                break;
            default:break;
        }
    }
}
