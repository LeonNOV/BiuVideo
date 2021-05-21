package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

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
import com.leon.biuvideo.adapters.otherAdapters.BangumiSeriesAdapter;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiSeason;
import com.leon.biuvideo.ui.resourcesFragment.video.OnBottomSheetWithItemListener;
import com.leon.biuvideo.ui.views.BottomSheetTopBar;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/26
 * @Desc 番剧系列选择弹窗
 */
public class VideoSeriesBottomSheet extends BottomSheetDialog {
    private final Context context;
    private final int currentPosition;
    private final List<BangumiSeason> bangumiSeasonList;

    private OnBottomSheetWithItemListener onBottomSheetWithItemListener;

    public VideoSeriesBottomSheet(@NonNull Context context, int currentPosition, List<BangumiSeason> bangumiSeasonList) {
        super(context);
        this.context = context;
        this.currentPosition = currentPosition;
        this.bangumiSeasonList = bangumiSeasonList;
    }

    public void setOnBottomSheetWithItemListener(OnBottomSheetWithItemListener onBottomSheetWithItemListener) {
        this.onBottomSheetWithItemListener = onBottomSheetWithItemListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_series_bottom_sheet_dialog);

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

        LoadingRecyclerView videoSeriesListData = findViewById(R.id.video_series_list_data);
        videoSeriesListData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        BangumiSeriesAdapter bangumiSeriesAdapter = new BangumiSeriesAdapter(bangumiSeasonList, context, currentPosition);
        bangumiSeriesAdapter.setHasStableIds(true);
        bangumiSeriesAdapter.setOnBottomSheetWithItemListener(onBottomSheetWithItemListener);
        videoSeriesListData.setRecyclerViewAdapter(bangumiSeriesAdapter);
        videoSeriesListData.setRecyclerViewLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        videoSeriesListData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }
}
