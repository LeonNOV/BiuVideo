package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.utils.PreferenceUtils;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc 视频画质选择适配器
 */
public class VideoQualityAdapter extends BaseAdapter<String[]> {
    private final List<String[]> qualityList;
    private final int currentQuality;

    private final boolean loginStatus = PreferenceUtils.getLoginStatus();
    private final boolean vipStatus = PreferenceUtils.getVipStatus();

    private OnVideoQualityListener onVideoQualityListener;

    public interface OnVideoQualityListener {
        /**
         * 清晰度选择
         *
         * @param qualityId 清晰度ID
         */
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
        return R.layout.video_quality_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        String[] strings = qualityList.get(position);
        final int qualityCode = Integer.parseInt(strings[0]);

        TextView videoQualityItemContent = holder.findById(R.id.video_quality_item_content);
        videoQualityItemContent.setText(strings[1]);
        if (currentQuality == qualityCode) {
            videoQualityItemContent.setTextColor(Color.parseColor("#FB7299"));
        } else {
            videoQualityItemContent.setTextColor(Color.WHITE);
        }

        if (strings[2] != null) {
            holder.setText(R.id.video_quality_item_mark, strings[2]);
        } else {
            holder.setVisibility(R.id.video_quality_item_mark, View.GONE);
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qualityCode == currentQuality) {
                    return;
                }

                if (loginStatus && vipStatus) {
                    Toast.makeText(context, context.getString(R.string.qualityLoginWarn), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (qualityCode > 32) {
                    if (!loginStatus) {
                        Toast.makeText(context, context.getString(R.string.qualityLoginWarn), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (qualityCode == 74 || qualityCode >= 112) {
                        Toast.makeText(context, context.getString(R.string.qualityVipWarn), Toast.LENGTH_SHORT).show();
                    } else {
                        if (onVideoQualityListener != null) {
                            onVideoQualityListener.onQuality(qualityCode);
                        }
                    }
                } else {
                    if (onVideoQualityListener != null) {
                        onVideoQualityListener.onQuality(qualityCode);
                    }
                }
            }
        });
    }
}
