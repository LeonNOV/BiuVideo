package com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.leon.biuvideo.R;
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
public class QualityPopupWindow extends PopupWindow {
    private final Context context;
    private int currentQualityId;
    private final LinkedHashMap<Integer, String> qualityMap;

    public QualityPopupWindow(Context context, int currentQualityId, LinkedHashMap<Integer, String> qualityMap) {
        this.context = context;
        this.currentQualityId = currentQualityId;
        this.qualityMap = qualityMap;

        initView();
    }

    private void initView() {
        setAnimationStyle(R.style.TranslateRightAnimation);

        List<String[]> strings = new ArrayList<>(qualityMap.size());
        for (Map.Entry<Integer, String> integerStringEntry : qualityMap.entrySet()) {
            strings.add(new String[]{String.valueOf(integerStringEntry.getKey()), integerStringEntry.getValue()});
        }

        View inflate = LayoutInflater.from(context).inflate(R.layout.quality_popup_window, null);
        LoadingRecyclerView qualityPopupWindowQualityInfo = inflate.findViewById(R.id.quality_popup_window_qualityInfo);
//        QualityInfoAdapter qualityInfoAdapter = new QualityInfoAdapter(currentQualityId, strings, context);
//        qualityInfoAdapter.setHasStableIds(true);
//        qualityPopupWindowQualityInfo.setRecyclerViewAdapter(qualityInfoAdapter);

        this.setContentView(inflate);
        this.setHeight(ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.MarginLayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
    }
}
