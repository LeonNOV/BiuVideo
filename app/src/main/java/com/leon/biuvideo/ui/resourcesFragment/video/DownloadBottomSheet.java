package com.leon.biuvideo.ui.resourcesFragment.video;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.ui.views.BottomSheetTopBar;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoWithFlvParser;
import com.leon.biuvideo.values.Quality;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/5/5
 * @Desc 下载弹窗
 */
public class DownloadBottomSheet extends BottomSheetDialog {
    private final Context context;
    private final List<VideoInfo.VideoAnthology> videoAnthologyList;
    private int qualityCode;

    public DownloadBottomSheet(@NonNull Context context, List<VideoInfo.VideoAnthology> videoAnthologyList) {
        super(context);
        this.context = context;
        this.videoAnthologyList = videoAnthologyList;
        this.qualityCode = PreferenceUtils.getDownloadQuality();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_bottom_sheet_dialog);

        initView();
    }

    private void initView() {
        final Map<String, Integer> qualityMap = new LinkedHashMap<>();
        qualityMap.put("4K 超清", 120);
        qualityMap.put("1080P60 高清", 116);
        qualityMap.put("1080P+ 高清", 112);
        qualityMap.put("1080P 高清", 80);
        qualityMap.put("720P60 高清", 74);
        qualityMap.put("720P 高清", 64);
        qualityMap.put("480P 清晰", 32);
        qualityMap.put("360", 16);
        qualityMap.put("240P 极速", 6);

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

        TagView tagView = findViewById(R.id.download_bottom_sheet_dialog_quality);
        tagView.setRightValue(Quality.convertQuality(qualityCode));
        tagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<>(qualityMap.keySet());

                ListPopupWindow listPopupWindow = new ListPopupWindow(context);
                listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayList));
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setAnchorView(tagView);
                listPopupWindow.setDropDownGravity(Gravity.START);
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        qualityCode = qualityMap.get(arrayList.get(position));
                        listPopupWindow.dismiss();
                    }
                });
                listPopupWindow.show();
            }
        });

        LoadingRecyclerView downloadBottomSheetDialogAnthologyData = findViewById(R.id.download_bottom_sheet_dialog_anthology_data);
        DownloadBottomSheetAdapter downloadBottomSheetAdapter = new DownloadBottomSheetAdapter(videoAnthologyList, context);
        downloadBottomSheetAdapter.setHasStableIds(true);
        downloadBottomSheetDialogAnthologyData.setRecyclerViewAdapter(downloadBottomSheetAdapter);
        downloadBottomSheetDialogAnthologyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }

    private static class DownloadBottomSheetAdapter extends BaseAdapter<VideoInfo.VideoAnthology> {
        private final List<VideoInfo.VideoAnthology> videoAnthologyList;
        private final DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils;
        private VideoWithFlvParser videoWithFlvParser;

        public DownloadBottomSheetAdapter(List<VideoInfo.VideoAnthology> beans, Context context) {
            super(beans, context);
            this.videoAnthologyList = beans;
            DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
            downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();
        }

        @Override
        public int getLayout(int viewType) {
            return R.layout.download_bottom_sheet_anthology_item;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            VideoInfo.VideoAnthology videoAnthology = videoAnthologyList.get(position);

            ImageView downloadBottomSheetAnthologyDownloadStat = holder.findById(R.id.download_bottom_sheet_anthology_download_stat);
            holder
                    .setText(R.id.download_bottom_sheet_anthology_item_title, videoAnthology.part)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadBottomSheetAnthologyDownloadStat.setVisibility(View.VISIBLE);
                            downloadBottomSheetAnthologyDownloadStat.setSelected(false);

                            /*DownloadHistory downloadHistory = new DownloadHistory();
                            downloadHistory.setResType(ResourceDownloadTask.RES_TYPE_VIDEO);
                            downloadHistory.setIsFailed(false);
                            downloadHistory.setIsCompleted(false);
                            downloadHistory.setIsMultipleAnthology(true);
                            downloadHistory.setLevelOneId(videoAnthology.mainId);
                            downloadHistory.setLevelTwoId(videoAnthology.cid);
                            downloadHistory.setTitle(videoAnthology.part);

                            if (videoWithFlvParser == null) {
                                videoWithFlvParser = new VideoWithFlvParser(videoAnthology.mainId);
                            }
                            VideoWithFlv videoWithFlv = videoWithFlvParser.parseData(videoAnthology.cid, VideoWithFlvParser.DEFAULT_QUALITY, false);

                            downloadHistory.setResStreamUrl(videoWithFlv.videoStreamInfoList.get(0).url);

                            ResourceDownloadTask resourceDownloadTask = new ResourceDownloadTask(context, downloadHistory);
                            resourceDownloadTask.startDownload();*/
                        }
                    });

            List<DownloadHistory> downloadHistoryList = downloadHistoryDaoUtils.queryByQueryBuilder(DownloadHistoryDao.Properties.LevelTwoId.eq(videoAnthology.cid));
            if (downloadHistoryList != null) {
                if (downloadHistoryList.size() > 0) {
                    downloadBottomSheetAnthologyDownloadStat.setVisibility(View.VISIBLE);
                    downloadBottomSheetAnthologyDownloadStat.setSelected(true);
                }
            }
        }
    }
}
