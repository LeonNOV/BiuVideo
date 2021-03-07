package com.leon.biuvideo.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;

import java.util.Map;

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

    public static void changeTabTitle(TabLayout.Tab tab, boolean isSelected) {
        if (isSelected) {
            View view = tab.getCustomView();
            if (view == null) {
                tab.setCustomView(R.layout.tab_layout_title);
            }

            TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(Color.BLACK);
        } else {
            View view = tab.getCustomView();
            if (view == null) {
                tab.setCustomView(R.layout.tab_layout_title);
            }

            TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
            textView.setTypeface(Typeface.DEFAULT);
            textView.setTextColor(Color.GRAY);
        }
    }
}
