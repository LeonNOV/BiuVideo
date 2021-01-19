package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.fragments.orderFragments.OrderInnerFragment;
import com.leon.biuvideo.ui.fragments.orderFragments.UserOrderArticleFragment;
import com.leon.biuvideo.ui.fragments.orderFragments.UserOrderVideoFragment;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.OrderFollowType;
import com.leon.biuvideo.values.OrderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户订阅数据fragment
 */
public class OrderFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager order_view_pager;
    private TextView order_video, order_bangumi, order_series, order_article, order_view_textView_warn;
    private Map<Integer, TextView> textViewMap;

    private LinearLayout order_view_linearLayout;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_order;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        order_view_textView_warn = findView(R.id.order_view_textView_warn);

        order_view_linearLayout = findView(R.id.order_view_linearLayout);

        order_view_pager = findView(R.id.order_view_pager);

        order_video =  findView(R.id.order_video);
        order_video.setOnClickListener(this);

        order_bangumi =  findView(R.id.order_bangumi);
        order_bangumi.setOnClickListener(this);

        order_series =  findView(R.id.order_series);
        order_series.setOnClickListener(this);

        order_article =  findView(R.id.order_article);
        order_article.setOnClickListener(this);

        initBroadcast();
    }

    @Override
    public void initValues() {
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        String cookie = initValues.getString("cookie", null);

        if (cookie == null) {
            setNoData();
        } else {
            boolean network = InternetUtils.checkNetwork(context);
            if (network) {
                order_view_textView_warn.setVisibility(View.GONE);
                order_view_pager.setVisibility(View.VISIBLE);
                order_view_linearLayout.setVisibility(View.VISIBLE);

                textViewMap = new HashMap<>();

                textViewMap.put(0, order_video);
                textViewMap.put(1, order_bangumi);
                textViewMap.put(2, order_series);
                textViewMap.put(3, order_article);

                order_view_pager.setOffscreenPageLimit(3);

                long mid = initValues.getLong("mid", -1);

                List<Fragment> fragments = new ArrayList<>();
                fragments.add(new UserOrderVideoFragment(mid));
                fragments.add(new OrderInnerFragment(mid, OrderType.BANGUMI, OrderFollowType.ALL));
                fragments.add(new OrderInnerFragment(mid, OrderType.SERIES, OrderFollowType.ALL));
                fragments.add(new UserOrderArticleFragment());

                ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
                order_view_pager.setAdapter(viewPageAdapter);
                order_view_pager.addOnPageChangeListener(this);
            } else {
                setNoData();
                Snackbar.make(view, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void setNoData () {
        order_view_textView_warn.setVisibility(View.VISIBLE);
        order_view_pager.setVisibility(View.GONE);
        order_view_linearLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_video:
                order_view_pager.setCurrentItem(0);
                break;
            case R.id.order_bangumi:
                order_view_pager.setCurrentItem(1);
                break;
            case R.id.order_series:
                order_view_pager.setCurrentItem(2);
                break;
            case R.id.order_article:
                order_view_pager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        ViewUtils.changeText(textViewMap, position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 初始化接收者
     */
    public void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UserLogin");
        intentFilter.addAction("UserLogout");

        UserLoginLocalReceiver userLoginLocalReceiver = new UserLoginLocalReceiver();
        userLoginLocalReceiver.setOnUserLoginListener(new OnUserLoginListener() {
            @Override
            public void onUserLogin() {
                // 刷新当前界面数据
                initValues();
            }
        });
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(userLoginLocalReceiver, intentFilter);
    }
}
