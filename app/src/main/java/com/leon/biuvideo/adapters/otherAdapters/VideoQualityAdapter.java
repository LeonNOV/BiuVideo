package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.utils.Fuck;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc 视频画质选择适配器
 */
public class VideoQualityAdapter extends BaseAdapter<String[]> {
    private final List<String[]> qualityList;
    private final int currentQuality;

    private OnVideoQualityListener onVideoQualityListener;

    public interface OnVideoQualityListener {
        void onQuality (int qualityId);
    }

    public void setOnVideoQualityListener(OnVideoQualityListener onVideoQualityListener) {
        this.onVideoQualityListener = onVideoQualityListener;
    }

    public VideoQualityAdapter(int currentQuality, List<String[]> beans, Context context) {
        super(beans, context);
        this.currentQuality = currentQuality;
        this.qualityList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        String[] strings = qualityList.get(position);

        TextView textView = holder.findById(android.R.id.text1);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        if (currentQuality == Integer.parseInt(strings[0])) {
            textView.setTextColor(Color.parseColor("#FB7299"));
        } else {
            textView.setTextColor(Color.WHITE);
        }
        textView.setText(strings[1]);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(strings[0]) == currentQuality) {
                    return;
                }

                if (onVideoQualityListener != null) {
                    onVideoQualityListener.onQuality(Integer.parseInt(strings[0]));
                }
            }
        });
    }

    
}
