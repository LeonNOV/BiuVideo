package com.leon.biuvideo.utils;

import android.graphics.Typeface;
import android.widget.TextView;

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
}
