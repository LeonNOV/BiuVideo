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
        int pointBiliBiliPink = R.drawable.shape_bilibili_pink;
        int pointBiliBiliPinkLite = R.drawable.ripple_bilibili_pink_lite;

        Typeface typeface_bold = Typeface.defaultFromStyle(Typeface.BOLD);
        Typeface typeface_normal = Typeface.defaultFromStyle(Typeface.NORMAL);

        TextView textView;
        for (Map.Entry<Integer, TextView> entry : textViewMap.entrySet()) {
            if (entry.getKey() == position) {
                textView = entry.getValue();
                textView.setBackgroundResource(pointBiliBiliPink);
                textView.setTextSize(18);
                textView.setTypeface(typeface_bold);
            } else {
                textView = entry.getValue();
                textView.setBackgroundResource(pointBiliBiliPinkLite);
                textView.setTextSize(15);
                textView.setTypeface(typeface_normal);
            }
        }
    }
}
