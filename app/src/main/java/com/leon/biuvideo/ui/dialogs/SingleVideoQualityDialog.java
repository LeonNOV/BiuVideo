package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.SingleVideoQualityAdapter;
import com.leon.biuvideo.beans.videoBean.play.Media;
import com.leon.biuvideo.layoutManager.SingleQualityLinearLayoutManager;

import java.util.List;
import java.util.Map;

/**
 * 显示缓存视频时选择的清晰度弹窗
 */
public class SingleVideoQualityDialog extends AlertDialog {
    private RecyclerView single_video_recyclerView_quality;

    private final Context context;
    private final List<Map.Entry<Integer, Media>> videoEntries;

    public SingleVideoQualityDialog(@NonNull Context context, List<Map.Entry<Integer, Media>> videoEntries) {
        super(context);
        this.context = context;
        this.videoEntries = videoEntries;
    }

    private OnQualityClickListener onQualityClickListener;

    public interface OnQualityClickListener {
        void onClickListener(Map.Entry<Integer, Media> mediaEntry);
    }

    public void setOnQualityClickListener(OnQualityClickListener onQualityClickListener) {
        this.onQualityClickListener = onQualityClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single_video_quality);

        initView();
        initValue();
    }

    private void initView() {
        //设置背景为透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        single_video_recyclerView_quality = findViewById(R.id.single_video_recyclerView_quality);

        Button single_video_quality_button_cancel = findViewById(R.id.single_video_quality_button_cancel);
        single_video_quality_button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initValue() {
        SingleQualityLinearLayoutManager linearLayoutManager = new SingleQualityLinearLayoutManager(context);
        SingleVideoQualityAdapter videoQualityAdapter = new SingleVideoQualityAdapter(videoEntries, context);
        videoQualityAdapter.setOnSingleVideoQualityClickListener(new SingleVideoQualityAdapter.OnSingleVideoQualityClickListener() {
            @Override
            public void onClickListener(Map.Entry<Integer, Media> mediaEntry) {
                if (onQualityClickListener != null) {
                    onQualityClickListener.onClickListener(mediaEntry);
                }
            }
        });

        single_video_recyclerView_quality.setAdapter(videoQualityAdapter);
        single_video_recyclerView_quality.setLayoutManager(linearLayoutManager);
    }
}
