package com.leon.biuvideo.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.MainActivity;

import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/3/21
 * @Desc View工具类
 */
public class ViewUtils {
    /**
     * 切换Fragment时，顶部文字的变化
     *
     * @param textViewMap   TextView
     * @param position  position
     */
    public static void changeText(Map<Integer, TextView> textViewMap, int position) {
        int selected = R.drawable.lndicator_bar_selected;
        int unselected = R.drawable.lndicator_bar_unselected;

        Typeface typeface_bold = Typeface.defaultFromStyle(Typeface.BOLD);
        Typeface typeface_normal = Typeface.defaultFromStyle(Typeface.NORMAL);

        TextView textView;
        for (Map.Entry<Integer, TextView> entry : textViewMap.entrySet()) {
            if (entry.getKey() == position) {
                textView = entry.getValue();
                textView.setBackgroundResource(selected);
                textView.setTextSize(18);
                textView.setTypeface(typeface_bold);
            } else {
                textView = entry.getValue();
                textView.setBackgroundResource(unselected);
                textView.setTextSize(15);
                textView.setTypeface(typeface_normal);
            }
        }
    }

    /**
     * 改变Tab的样式
     *
     * @param tab   Tab
     * @param isSelected    是否已选中
     */
    public static void changeTabTitle(TabLayout.Tab tab, boolean isSelected) {
        if (isSelected) {
            View view = tab.getCustomView();
            if (view == null) {
                tab.setCustomView(R.layout.tab_layout_title);
            }

            TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } else {
            View view = tab.getCustomView();
            if (view == null) {
                tab.setCustomView(R.layout.tab_layout_title);
            }

            TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setTextColor(textView.getContext().getColor(R.color.gray));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

    /**
     * 初始化ViewPager2和TabLayout
     *
     * @param tabLayout TabLayout
     * @param viewPager2    ViewPager2
     * @param titles    Tab标题
     * @param firstShowItemPosition 第一个显示的item索引
     * @return MainActivity.OnTouchListener
     */
    public static MainActivity.OnTouchListener initTabLayoutAndViewPager2(Activity activity, TabLayout tabLayout, ViewPager2 viewPager2, String[] titles, int firstShowItemPosition) {
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setCurrentItem(firstShowItemPosition, false);
        viewPager2.setOffscreenPageLimit(titles.length);
        
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == firstShowItemPosition) {
                    ViewUtils.changeTabTitle(tab, true);
                }

                tab.setText(titles[position]);
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewUtils.changeTabTitle(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ViewUtils.changeTabTitle(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 设置ViewPager2灵敏度
        return setViewPagerSensitivity(activity, viewPager2);
    }

    /**
     * 设置ViewPager2灵敏度
     *
     * @param activity  activity
     * @param viewPager2    viewPager2
     * @return  MainActivity.OnTouchListener
     */
    private static MainActivity.OnTouchListener setViewPagerSensitivity(Activity activity, ViewPager2 viewPager2) {
        MainActivity.OnTouchListener onTouchListener = new MainActivity.OnTouchListener() {
            private int startX = 0;
            private int startY = 0;

            @Override
            public void onTouch(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getX();
                        int endY = (int) event.getY();

                        int disX = Math.abs(endX - startX);
                        int disY = Math.abs(endY - startY);
                        if (disX < disY) {
                            // 如果水平滑动的距离小于垂直的滑动距离，就关闭左右滑动操作
                            viewPager2.setUserInputEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        startX = 0;
                        startY = 0;
                        viewPager2.setUserInputEnabled(true);
                        break;
                    default:
                        break;
                }
            }

        };

        // 注册Touch事件
        ((MainActivity) activity).registerTouchEvenListener(onTouchListener);

        return onTouchListener;
    }
}
