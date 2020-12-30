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
public class SingleVideoQualityDialog extends AlertDialog implements View.OnClickListener {
    private RecyclerView single_video_recyclerView_quality;
    private Button single_video_quality_button_cancel;

    private final Context context;
    private List<Map.Entry<Integer, Media>> videoEntries;
    public static OnQualityItemListener onQualityItemListener;

    public SingleVideoQualityDialog(@NonNull Context context, List<Map.Entry<Integer, Media>> videoEntries) {
        super(context);
        this.context = context;
        this.videoEntries = videoEntries;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_video_quality_dialog);

        initView();
        initValue();
    }

    private void initView() {
        //设置背景为透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        single_video_recyclerView_quality = findViewById(R.id.single_video_recyclerView_quality);

        single_video_quality_button_cancel = findViewById(R.id.single_video_quality_button_cancel);
        single_video_quality_button_cancel.setOnClickListener(this);
    }

    private void initValue() {
        SingleQualityLinearLayoutManager linearLayoutManager = new SingleQualityLinearLayoutManager(context);
        SingleVideoQualityAdapter videoQualityAdapter = new SingleVideoQualityAdapter(videoEntries, context);

        single_video_recyclerView_quality.setAdapter(videoQualityAdapter);
        single_video_recyclerView_quality.setLayoutManager(linearLayoutManager);
    }

    public interface OnQualityItemListener {
        void onItemClickListener(Map.Entry<Integer, Media> videoEntry);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
