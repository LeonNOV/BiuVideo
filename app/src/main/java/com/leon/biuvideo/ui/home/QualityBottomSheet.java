package com.leon.biuvideo.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.ui.views.BottomSheetTopBar;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.values.Qualitys;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/5
 * @Desc 画质选择底部弹窗
 */
public class QualityBottomSheet extends BottomSheetDialog {
    private final Context context;
    private final boolean isPlay;
    private QualityBottomSheetAdapter.OnClickQualityItemListener onClickQualityItemListener;

    public QualityBottomSheet(@NonNull Context context, boolean isPlay) {
        super(context);
        this.context = context;
        this.isPlay = isPlay;
    }

    public void setOnClickQualityItemListener(QualityBottomSheetAdapter.OnClickQualityItemListener onClickQualityItemListener) {
        this.onClickQualityItemListener = onClickQualityItemListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_quality_list);

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

        ((BottomSheetTopBar) findViewById(R.id.settings_quality_list_topBar)).setTitle(isPlay ? "设置默认播放清晰度" : "设置默认下载清晰度");

        LoadingRecyclerView settingsQualityListData = findViewById(R.id.settings_quality_list_data);
        QualityBottomSheetAdapter qualityBottomSheetAdapter = new QualityBottomSheetAdapter(Qualitys.getQUALITYS(), context);
        qualityBottomSheetAdapter.setHasStableIds(true);
        qualityBottomSheetAdapter.setOnClickQualityItemListener(onClickQualityItemListener);
        settingsQualityListData.setRecyclerViewAdapter(qualityBottomSheetAdapter);
        settingsQualityListData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }

    public static class QualityBottomSheetAdapter extends BaseAdapter<String[]> {
        private final List<String[]> qualityList;
        private OnClickQualityItemListener onClickQualityItemListener;

        public QualityBottomSheetAdapter(List<String[]> beans, Context context) {
            super(beans, context);
            this.qualityList = beans;
        }

        public interface OnClickQualityItemListener {
            void onClick(String quality, int qualityCode);
        }

        public void setOnClickQualityItemListener(OnClickQualityItemListener onClickQualityItemListener) {
            this.onClickQualityItemListener = onClickQualityItemListener;
        }

        @Override
        public int getLayout(int viewType) {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            TextView textView = holder.findById(android.R.id.text1);
            textView.setText(qualityList.get(position)[1]);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(15);
            textView.setPadding(0, 10, 0, 10);

            textView.setBackgroundResource(R.drawable.ripple_round_corners6dp_bg);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickQualityItemListener != null) {
                        String[] quality = qualityList.get(position);
                        onClickQualityItemListener.onClick(quality[1], Integer.parseInt(quality[0]));
                    }
                }
            });
        }
    }
}
