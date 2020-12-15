package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.ui.fragments.BaseFragment;
import com.leon.biuvideo.ui.fragments.BindingUtils;
import com.leon.biuvideo.ui.fragments.OrderInnerFragment;
import com.leon.biuvideo.ui.fragments.playListFragments.VideoListFragment;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.OrderFollowType;
import com.leon.biuvideo.values.OrderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager order_view_pager;
    private TextView order_video, order_bangumi, order_series, order_article;
    private Map<Integer, TextView> textViewMap;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_order;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        order_view_pager = findView(R.id.order_view_pager);
        order_video =  findView(R.id.order_video);
        order_bangumi =  findView(R.id.order_bangumi);
        order_series =  findView(R.id.order_series);
        order_article =  findView(R.id.order_article);
    }

    @Override
    public void initValues() {
        textViewMap = new HashMap<>();

        order_video.setOnClickListener(this);
        textViewMap.put(0, order_video);

        order_bangumi.setOnClickListener(this);
        textViewMap.put(1, order_bangumi);

        order_series.setOnClickListener(this);
        textViewMap.put(2, order_series);

        order_article.setOnClickListener(this);
        textViewMap.put(3, order_article);

        order_view_pager.setOffscreenPageLimit(3);

        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        String cookie = initValues.getString("cookie", null);
        long mid = initValues.getLong("mid", -1);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new VideoListFragment());
        fragments.add(new VideoListFragment());
        fragments.add(new OrderInnerFragment(mid, cookie, OrderType.BANGUMI, OrderFollowType.ALL));
        fragments.add(new OrderInnerFragment(mid, cookie, OrderType.SERIES, OrderFollowType.ALL));
//        fragments.add(new OrderInnerFragment(mid, cookie, OrderType.ARTICLE, OrderFollowType.ALL));

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        order_view_pager.setAdapter(viewPageAdapter);
        order_view_pager.addOnPageChangeListener(this);
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
}
