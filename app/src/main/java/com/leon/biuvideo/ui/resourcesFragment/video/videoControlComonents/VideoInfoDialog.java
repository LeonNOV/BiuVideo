package com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.QualityInfoAdapter;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc
 */
public class VideoInfoDialog extends AlertDialog {
    private final Context context;
    private int currentQuality;
    private LinkedHashMap<Integer, String> qualityMap;

    public VideoInfoDialog(@NonNull Context context, int currentQuality, LinkedHashMap<Integer, String> qualityMap) {
        super(context);
        this.context = context;
        this.currentQuality = currentQuality;
        this.qualityMap = qualityMap;
    }

    public VideoInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public VideoInfoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_info_dialog);

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
        List<String[]> strings = new ArrayList<>(qualityMap.size());
        for (Map.Entry<Integer, String> integerStringEntry : qualityMap.entrySet()) {
            strings.add(new String[]{String.valueOf(integerStringEntry.getKey()), integerStringEntry.getValue()});
        }

        LoadingRecyclerView qualityPopupWindowQualityInfo = findViewById(R.id.quality_popup_window_qualityInfo);
        qualityPopupWindowQualityInfo.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        QualityInfoAdapter qualityInfoAdapter = new QualityInfoAdapter(currentQuality, strings, context);
        qualityInfoAdapter.setHasStableIds(true);

        qualityPopupWindowQualityInfo.setRecyclerViewAdapter(qualityInfoAdapter);
        qualityPopupWindowQualityInfo.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        qualityPopupWindowQualityInfo.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }
}
