package com.leon.biuvideo.ui.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.testAdapters.RvTestAdapter;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.beans.Weather;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.home.OrderFragment;
import com.leon.biuvideo.ui.home.RecommendFragment;
import com.leon.biuvideo.ui.home.SettingsFragment;
import com.leon.biuvideo.ui.otherFragments.PopularFragment;
import com.leon.biuvideo.ui.views.CardTitle;
import com.leon.biuvideo.utils.WeatherUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 主页面
 */
public class HomeFragment extends SupportFragment implements View.OnClickListener {
    private View view;
    private Context context;
    RecyclerView home_recyclerView;
    private ImageView weatherIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        context = getContext();
        
        initView();
        initValue();
        
        return view;
    }

    private void initView() {
        view.findViewById(R.id.home_my_orders).setOnClickListener(this);
        view.findViewById(R.id.home_my_favorites).setOnClickListener(this);
        view.findViewById(R.id.home_my_follows).setOnClickListener(this);
        view.findViewById(R.id.home_my_history).setOnClickListener(this);
        view.findViewById(R.id.home_my_downloaded).setOnClickListener(this);
        view.findViewById(R.id.home_setting).setOnClickListener(this);
        view.findViewById(R.id.home_popular).setOnClickListener(this);

        weatherIcon = view.findViewById(R.id.home_weatherIcon);

        CardTitle home_fragment_cardTitle_recommend = view.findViewById(R.id.home_fragment_cardTitle_recommend);
        home_fragment_cardTitle_recommend.setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(RecommendFragment.newInstance());
            }
        });

        home_recyclerView = view.findViewById(R.id.home_recyclerView);
    }

    private void initValue() {
        List<RvTestBean> rvTestBeanList = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            RvTestBean rvTestBean = new RvTestBean();

            rvTestBean.title = "Title" + (i + 1);
            rvTestBean.view = (random.nextInt(5000) + 5000);

            rvTestBeanList.add(rvTestBean);
        }

        RvTestAdapter rvTestAdapter = new RvTestAdapter(rvTestBeanList, context);
        home_recyclerView.setAdapter(rvTestAdapter);

        WeatherUtil weatherUtil = new WeatherUtil();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Weather currentWeather = weatherUtil.getCurrentWeather("411303");
                weatherIcon.setImageResource(currentWeather.weatherIconId);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_my_orders:
                ((NavFragment) getParentFragment()).startBrotherFragment(OrderFragment.newInstance());
                break;
            case R.id.home_my_favorites:
                ((NavFragment) getParentFragment()).startBrotherFragment(FavoritesFragment.newInstance());
                break;
            case R.id.home_my_follows:
                Toast.makeText(context, "点击了-我关注的人", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_my_history:
                Toast.makeText(context, "点击了-历史记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_my_downloaded:
                Toast.makeText(context, "点击了-下载记录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_setting:
                ((NavFragment) getParentFragment()).startBrotherFragment(SettingsFragment.newInstance());
                break;
            case R.id.home_popular:
                ((NavFragment) getParentFragment()).startBrotherFragment(PopularFragment.newInstance());
                break;
            default:
                break;
        }
    }
}
