package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc 视频速度选择适配器
 */
public class VideoSpeedAdapter extends BaseAdapter<Float> {
    private final List<Float> speedList;
    private final float currentSpeed;

    private OnVideoSpeedListener onVideoSpeedListener;

    public interface OnVideoSpeedListener {
        void onSpeed (float speed);
    }

    public void setOnVideoSpeedListener(OnVideoSpeedListener onVideoSpeedListener) {
        this.onVideoSpeedListener = onVideoSpeedListener;
    }

    public VideoSpeedAdapter(float currentSpeed, List<Float> beans, Context context) {
        super(beans, context);
        this.currentSpeed = currentSpeed;
        this.speedList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Float speed = speedList.get(position);

        TextView textView = holder.findById(android.R.id.text1);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        if (currentSpeed == speed) {
            textView.setTextColor(Color.parseColor("#FB7299"));
        } else {
            textView.setTextColor(Color.WHITE);
        }
        textView.setText(speed + "x");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed == currentSpeed) {
                    return;
                }

                if (onVideoSpeedListener != null) {
                    onVideoSpeedListener.onSpeed(speed);
                }
            }
        });
    }

    
}
