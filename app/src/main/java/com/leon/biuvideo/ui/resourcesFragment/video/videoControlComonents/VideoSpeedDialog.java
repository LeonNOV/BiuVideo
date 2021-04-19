package com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.VideoSpeedAdapter;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;

import java.util.ArrayList;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc 视频速度选择弹窗
 */
public class VideoSpeedDialog extends AlertDialog {
    private final Context context;
    private final float currentSpeed;
    private VideoSpeedAdapter.OnVideoSpeedListener onVideoSpeedListener;

    public void setOnVideoSpeedListener(VideoSpeedAdapter.OnVideoSpeedListener onVideoSpeedListener) {
        this.onVideoSpeedListener = onVideoSpeedListener;
    }

    public VideoSpeedDialog(@NonNull Context context, float currentSpeed) {
        super(context);
        this.context = context;
        this.currentSpeed = currentSpeed;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_loading_recycler_view_dialog);

        Window window = getWindow();
        window.setGravity(Gravity.END);
        window.setWindowAnimations(R.style.TranslateRightAnimation);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.width = context.getResources().getDimensionPixelOffset(R.dimen.qualityWidth);
        window.setAttributes(attributes);

        initView();
    }

    private void initView() {
        float max = 2.0f;
        float min = 0.25f;
        float step = 0.25f;

        ArrayList<Float> speedList = new ArrayList<>();
        for (float i = max; i > min; i -= step) {
            speedList.add(i);
        }

        LoadingRecyclerView loadingRecyclerView = findViewById(R.id.list);
        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        VideoSpeedAdapter videoSpeedAdapter = new VideoSpeedAdapter(currentSpeed, speedList, context);
        videoSpeedAdapter.setOnVideoSpeedListener(onVideoSpeedListener);
        videoSpeedAdapter.setHasStableIds(true);

        loadingRecyclerView.setRecyclerViewAdapter(videoSpeedAdapter);
        loadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }
}
