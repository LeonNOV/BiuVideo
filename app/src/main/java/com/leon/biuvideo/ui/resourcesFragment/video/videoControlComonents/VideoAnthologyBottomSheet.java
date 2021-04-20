package com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.VideoAnthologyAdapter;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoDetailInfo;
import com.leon.biuvideo.ui.views.BottomSheetTopBar;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/20
 * @Desc 视频选集弹窗
 */
public class VideoAnthologyBottomSheet extends BottomSheetDialog {
    private final Context context;
    private final int currentPosition;
    private final List<VideoDetailInfo.AnthologyInfo> anthologyInfoList;
    private VideoAnthologyAdapter.OnVideoAnthologyListener onVideoAnthologyListener;

    public VideoAnthologyBottomSheet(@NonNull Context context, int currentPosition, List<VideoDetailInfo.AnthologyInfo> anthologyInfoList) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.currentPosition = currentPosition;
        this.anthologyInfoList = anthologyInfoList;
    }

    public void setOnVideoAnthologyListener(VideoAnthologyAdapter.OnVideoAnthologyListener onVideoAnthologyListener) {
        this.onVideoAnthologyListener = onVideoAnthologyListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_anthology_bottom_sheet_dialog);

        initView();
    }

    private void initView() {
        Window window = getWindow();

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);

        // 设置底部透明
        FrameLayout bottom = findViewById(R.id.design_bottom_sheet);
        if (bottom != null) {
            bottom.setBackgroundResource(android.R.color.transparent);
        }

        ((BottomSheetTopBar) findViewById(R.id.video_anthology_list_topBar)).setOnCloseListener(new BottomSheetTopBar.OnCloseListener() {
            @Override
            public void onClose() {
                dismiss();
            }
        });

        LoadingRecyclerView videoAnthologyListData = findViewById(R.id.video_anthology_list_data);

        videoAnthologyListData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        VideoAnthologyAdapter videoAnthologyAdapter = new VideoAnthologyAdapter(currentPosition, anthologyInfoList, context);
        videoAnthologyAdapter.setHasStableIds(true);
        videoAnthologyAdapter.setOnVideoAnthologyListener(onVideoAnthologyListener);
        videoAnthologyListData.setRecyclerViewAdapter(videoAnthologyAdapter);
        videoAnthologyListData.setRecyclerViewLayoutManager(linearLayoutManager);
        videoAnthologyListData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }
}
