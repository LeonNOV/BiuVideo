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

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.beans.userBeans.HistoryType;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.fragments.historyFragment.HistoryInnerFragment;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史记录Fragment
 */
public class HistoryFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager history_view_pager;
    private TextView history_video, history_live, history_article, history_view_textView_warn;
    private LinearLayout history_linearLayout;

    private Map<Integer, TextView> textViewMap;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_history;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        history_linearLayout = findView(R.id.history_linearLayout);

        history_view_pager = findView(R.id.history_view_pager);
        history_view_pager.setOffscreenPageLimit(3);

        history_video = findView(R.id.history_video);
        history_video.setOnClickListener(this);

        history_live = findView(R.id.history_live);
        history_live.setOnClickListener(this);

        history_article = findView(R.id.history_article);
        history_article.setOnClickListener(this);

        history_view_textView_warn = findView(R.id.history_view_textView_warn);

        initBroadcast();
    }

    @Override
    public void initValues() {
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        String cookie = initValues.getString("cookie", null);

        if (cookie == null) {
            history_view_textView_warn.setVisibility(View.VISIBLE);
            history_view_pager.setVisibility(View.GONE);
            history_linearLayout.setVisibility(View.GONE);
        } else {
            history_view_textView_warn.setVisibility(View.GONE);
            history_view_pager.setVisibility(View.VISIBLE);
            history_linearLayout.setVisibility(View.VISIBLE);

            textViewMap = new HashMap<>();
            textViewMap.put(0, history_video);
            textViewMap.put(1, history_live);
            textViewMap.put(2, history_article);

            List<Fragment> fragments = new ArrayList<>();
            fragments.add(new HistoryInnerFragment(cookie, HistoryType.VIDEO));
            fragments.add(new HistoryInnerFragment(cookie, HistoryType.LIVE));
            fragments.add(new HistoryInnerFragment(cookie, HistoryType.ARTICLE));

            ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
            history_view_pager.setAdapter(viewPageAdapter);
            history_view_pager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_video:
                history_view_pager.setCurrentItem(0);
                break;
            case R.id.history_live:
                history_view_pager.setCurrentItem(1);
                break;
            case R.id.history_article:
                history_view_pager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                ViewUtils.changeText(textViewMap, 0);
                break;
            case 1:
                ViewUtils.changeText(textViewMap, 1);
                break;
            case 2:
                ViewUtils.changeText(textViewMap, 2);
                break;
            default:
                break;
        }
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
