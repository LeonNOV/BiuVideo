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
import com.leon.biuvideo.adapters.otherAdapters.VideoQualityAdapter;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc 视频画质选择弹窗
 */
public class VideoQualityDialog extends AlertDialog {
    private final Context context;
    private final int currentQuality;
    private final LinkedHashMap<Integer, String> qualityMap;

    private VideoQualityAdapter.OnVideoQualityListener onVideoQualityListener;

    public void setOnVideoQualityListener(VideoQualityAdapter.OnVideoQualityListener onVideoQualityListener) {
        this.onVideoQualityListener = onVideoQualityListener;
    }

    public VideoQualityDialog(@NonNull Context context, int currentQuality, LinkedHashMap<Integer, String> qualityMap) {
        super(context);
        this.context = context;
        this.currentQuality = currentQuality;
        this.qualityMap = qualityMap;
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
        attributes.width = context.getResources().getDimensionPixelOffset(R.dimen.qualityDialogWidth);
        window.setAttributes(attributes);

        initView();
    }

    private void initView() {
        boolean loginStatus = PreferenceUtils.getLoginStatus();
        boolean vipStatus = PreferenceUtils.getVipStatus();

        List<String[]> strings = new ArrayList<>(qualityMap.size());
        for (Map.Entry<Integer, String> integerStringEntry : qualityMap.entrySet()) {
            String reason = null;
            Integer qualityCode = integerStringEntry.getKey();
            if (qualityCode > 32) {
                if (qualityCode == 80 || qualityCode == 64) {
                    reason = loginStatus ? null : "登录";
                } else {
                    reason = vipStatus ? null : "大会员";
                }
            }
            strings.add(new String[]{String.valueOf(qualityCode), integerStringEntry.getValue(), reason});
        }

        LoadingRecyclerView loadingRecyclerView = findViewById(R.id.list);
        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        VideoQualityAdapter videoQualityAdapter = new VideoQualityAdapter(currentQuality, strings, context);
        videoQualityAdapter.setOnVideoQualityListener(onVideoQualityListener);
        videoQualityAdapter.setHasStableIds(true);

        loadingRecyclerView.setRecyclerViewAdapter(videoQualityAdapter);
        loadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        loadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }
}
