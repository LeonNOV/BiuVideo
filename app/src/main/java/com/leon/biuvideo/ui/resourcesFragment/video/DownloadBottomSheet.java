package com.leon.biuvideo.ui.resourcesFragment.video;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthology;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.service.DownloadWatcher;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoWithFlvParser;
import com.leon.biuvideo.values.Quality;
import com.leon.biuvideo.values.Qualitys;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @Author Leon
 * @Time 2021/5/5
 * @Desc 下载弹窗
 */
public class DownloadBottomSheet<T> extends BottomSheetDialog {
    private final Context context;

    private List<VideoInfo.VideoAnthology> videoAnthologyList;
    private List<BangumiAnthology> bangumiAnthologyList;

    private int qualityCode;
    private String quality;

    private OnClickDownloadItemListener onClickDownloadItemListener;

    public DownloadBottomSheet(@NonNull Context context) {
        super(context);

        this.context = context;
        this.qualityCode = PreferenceUtils.getDownloadQuality();
        this.quality = Quality.convertQuality(this.qualityCode);
    }

    public void setVideoAnthologyList(List<VideoInfo.VideoAnthology> videoAnthologyList) {
        this.videoAnthologyList = videoAnthologyList;
    }

    public void setBangumiAnthologyList(List<BangumiAnthology> bangumiAnthologyList) {
        this.bangumiAnthologyList = bangumiAnthologyList;
    }

    public interface OnClickDownloadItemListener {
        /**
         * 点击选集后的回调
         */
        void onClickItem();
    }

    public void setOnClickDownloadItemListener(OnClickDownloadItemListener onClickDownloadItemListener) {
        this.onClickDownloadItemListener = onClickDownloadItemListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_bottom_sheet_dialog);

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

        TagView tagView = findViewById(R.id.download_bottom_sheet_dialog_quality);
        tagView.setRightValue(quality);
        tagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListPopupWindow listPopupWindow = new ListPopupWindow(context);
                listPopupWindow.setAdapter(new ArrayAdapter<>(context, R.layout.search_result_menu_item, Qualitys.getQualityStr()));
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setBackgroundDrawable(context.getDrawable(R.drawable.round_corners6dp_bg));
                listPopupWindow.setAnchorView(tagView);
                listPopupWindow.setDropDownGravity(Gravity.START);
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int qualityCode = Qualitys.getQualityCodes().get(position);

                        quality = Qualitys.getQualityStr().get(position);
                        tagView.setRightValue(quality);
                        DownloadBottomSheet.this.qualityCode = qualityCode;

                        listPopupWindow.dismiss();
                    }
                });
                listPopupWindow.show();
            }
        });

        LoadingRecyclerView downloadBottomSheetDialogAnthologyData = findViewById(R.id.download_bottom_sheet_dialog_anthology_data);

        DownloadBottomSheetAdapter<T> downloadBottomSheetAdapter;
        if (videoAnthologyList != null) {
            downloadBottomSheetAdapter = new DownloadBottomSheetAdapter<>(context, false);
            downloadBottomSheetAdapter.setVideoAnthologyList(videoAnthologyList);
        } else if (bangumiAnthologyList != null) {
            downloadBottomSheetAdapter = new DownloadBottomSheetAdapter<>(context, true);
            downloadBottomSheetAdapter.setBangumiAnthologyList(bangumiAnthologyList);
        } else {
            throw new NullPointerException("选集数据不能为null");
        }

        downloadBottomSheetAdapter.setHasStableIds(true);
        downloadBottomSheetDialogAnthologyData.setRecyclerViewAdapter(downloadBottomSheetAdapter);
        downloadBottomSheetDialogAnthologyData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        downloadBottomSheetDialogAnthologyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }

    /**
     * @Author Leon
     * @Time 2021/5/6
     * @Desc 下载选集适配器
     */
    public class DownloadBottomSheetAdapter<T> extends BaseAdapter<T> {
        private List<VideoInfo.VideoAnthology> videoAnthologyList;
        private List<BangumiAnthology> bangumiAnthologyList;

        private final boolean isBangumi;
        private VideoWithFlvParser videoWithFlvParser;
        private final Handler handler;
        private DownloadHistory downloadHistory;

        public DownloadBottomSheetAdapter(Context context, boolean isBangumi) {
            super(context);
            this.isBangumi = isBangumi;
            handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    Bundle data = msg.getData();
                    VideoWithFlv videoWithFlv = (VideoWithFlv) data.getSerializable("videoWithFlv");

                    if (videoWithFlv == null) {
                        SimpleSnackBar.make(view, "获取下载数据失败~", SimpleSnackBar.LENGTH_LONG).show();
                        return true;
                    }

                    downloadHistory.setResStreamUrl(videoWithFlv.videoStreamInfoList.get(0).url);

                    ResourceDownloadTask resourceDownloadTask = new ResourceDownloadTask(context, this, downloadHistory);
                    resourceDownloadTask.startDownload();

                    DownloadWatcher.addTask(resourceDownloadTask);

                    return true;
                }
            });
        }

        public void setVideoAnthologyList(List<VideoInfo.VideoAnthology> videoAnthologyList) {
            this.videoAnthologyList = videoAnthologyList;
            append((List<T>) videoAnthologyList);
        }

        public void setBangumiAnthologyList(List<BangumiAnthology> bangumiAnthologyList) {
            this.bangumiAnthologyList = bangumiAnthologyList;
            append((List<T>) bangumiAnthologyList);
        }

        @Override
        public int getLayout(int viewType) {
            return R.layout.download_bottom_sheet_anthology_item;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            if (isBangumi) {
                setBangumiAnthologyData(holder, bangumiAnthologyList.get(position));
            } else {
                setVideoAnthologyData(holder, videoAnthologyList.get(position));
            }
        }

        private void setBangumiAnthologyData(BaseViewHolder holder, BangumiAnthology bangumiAnthology) {
            ImageView downloadBottomSheetAnthologyDownloadStat = holder.findById(R.id.download_bottom_sheet_anthology_download_stat);
            downloadBottomSheetAnthologyDownloadStat.setVisibility((bangumiAnthology.isDownloading || bangumiAnthology.isDownloaded) ? View.VISIBLE : View.INVISIBLE);
            downloadBottomSheetAnthologyDownloadStat.setSelected(bangumiAnthology.isDownloading && bangumiAnthology.isDownloaded);
            holder
                    .setText(R.id.download_bottom_sheet_anthology_item_title, bangumiAnthology.subTitle)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File file = ResourceDownloadTask.checkSaveDirectory(context, ResourceDownloadTask.RES_TYPE_VIDEO,
                                    bangumiAnthology.mainTitle, bangumiAnthology.subTitle);

                            boolean exists = ResourceDownloadTask.isExists(file, context, bangumiAnthology.seasonId, bangumiAnthology.cid);

                            if (exists) {
                                Toast.makeText(context, context.getString(R.string.downloadExisted), Toast.LENGTH_LONG).show();
                            } else {
                                if (InternetUtils.checkNetwork(v)) {
                                    // 如果未处于 下载完成/正在下载 状态，则进行下载
                                    if (bangumiAnthology.isDownloading || bangumiAnthology.isDownloaded) {
                                        return;
                                    }

                                    bangumiAnthology.isDownloading = true;

                                    downloadBottomSheetAnthologyDownloadStat.setVisibility(View.VISIBLE);
                                    downloadBottomSheetAnthologyDownloadStat.setSelected(false);

                                    if (onClickDownloadItemListener != null) {
                                        onClickDownloadItemListener.onClickItem();
                                    }

                                    getBangumiDownloadInfo(bangumiAnthology);
                                }
                            }
                        }
                    });
        }

        private void setVideoAnthologyData(BaseViewHolder holder, VideoInfo.VideoAnthology videoAnthology) {
            ImageView downloadBottomSheetAnthologyDownloadStat = holder.findById(R.id.download_bottom_sheet_anthology_download_stat);
            downloadBottomSheetAnthologyDownloadStat.setVisibility((videoAnthology.isDownloading || videoAnthology.isDownloaded) ? View.VISIBLE : View.INVISIBLE);
            downloadBottomSheetAnthologyDownloadStat.setSelected(videoAnthology.isDownloading && videoAnthology.isDownloaded);

            holder
                    .setText(R.id.download_bottom_sheet_anthology_item_title, videoAnthology.subTitle)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            File file = ResourceDownloadTask.checkSaveDirectory(context, ResourceDownloadTask.RES_TYPE_VIDEO,
                                    videoAnthology.mainTitle, videoAnthology.subTitle);

                            boolean exists = ResourceDownloadTask.isExists(file, context, videoAnthology.mainId, videoAnthology.cid);

                            if (exists) {
                                Toast.makeText(context, context.getString(R.string.downloadExisted), Toast.LENGTH_LONG).show();
                            } else {
                                if (InternetUtils.checkNetwork(v)) {
                                    // 如果未处于 下载完成/正在下载 状态，则进行下载
                                    if (videoAnthology.isDownloading || videoAnthology.isDownloaded) {
                                        return;
                                    }

                                    videoAnthology.isDownloading = true;

                                    downloadBottomSheetAnthologyDownloadStat.setVisibility(View.VISIBLE);
                                    downloadBottomSheetAnthologyDownloadStat.setSelected(false);

                                    if (onClickDownloadItemListener != null) {
                                        onClickDownloadItemListener.onClickItem();
                                    }

                                    getVideoDownloadInfo(videoAnthology);
                                }
                            }
                        }
                    });
        }

        /**
         * 获取番剧下载信息
         */
        private void getBangumiDownloadInfo(BangumiAnthology bangumiAnthology) {
            downloadHistory = new DownloadHistory();
            downloadHistory.setResType(ResourceDownloadTask.RES_TYPE_VIDEO);
            downloadHistory.setIsFailed(false);
            downloadHistory.setIsCompleted(false);
            downloadHistory.setIsMultipleAnthology(true);
            downloadHistory.setLevelOneId(bangumiAnthology.seasonId);
            downloadHistory.setLevelTwoId(bangumiAnthology.cid);
            downloadHistory.setMainTitle(bangumiAnthology.mainTitle);
            downloadHistory.setSubTitle(bangumiAnthology.subTitle);
            downloadHistory.setCoverUrl(bangumiAnthology.cover);

            getVideoStreamUrl(bangumiAnthology.seasonId, bangumiAnthology.cid);
        }

        /**
         * 获取视频下载信息
         */
        private void getVideoDownloadInfo(VideoInfo.VideoAnthology videoAnthology) {
            downloadHistory = new DownloadHistory();
            downloadHistory.setResType(ResourceDownloadTask.RES_TYPE_VIDEO);
            downloadHistory.setIsFailed(false);
            downloadHistory.setIsCompleted(false);
            downloadHistory.setIsMultipleAnthology(true);
            downloadHistory.setLevelOneId(videoAnthology.mainId);
            downloadHistory.setLevelTwoId(videoAnthology.cid);
            downloadHistory.setMainTitle(videoAnthology.mainTitle);
            downloadHistory.setSubTitle(videoAnthology.subTitle);
            downloadHistory.setCoverUrl(videoAnthology.cover);

            getVideoStreamUrl(videoAnthology.mainId, videoAnthology.cid);
        }

        /**
         * 获取视频流链接
         *
         * @param levelOneId                               levelOneId
         * @param cid                                      选集ID
         */
        private void getVideoStreamUrl(String levelOneId, String cid) {
            if (videoWithFlvParser == null) {
                videoWithFlvParser = new VideoWithFlvParser(levelOneId);
            }

            SimpleSingleThreadPool.executor(new Runnable() {
                @Override
                public void run() {
                    VideoWithFlv videoWithFlv;

                    videoWithFlv = videoWithFlvParser.parseData(cid, String.valueOf(qualityCode), isBangumi, false);

                    // 判断选择清晰度是否与获取的的清晰度相同,如果不相同,则获取与选择清晰度最接近的清晰度
                    if (qualityCode != videoWithFlv.currentQualityId) {
                        // 获取所有能‘获取’的清晰度
                        Set<Integer> keySet = videoWithFlv.qualityMap.keySet();
                        keySet.add(qualityCode);
                        ArrayList<Integer> integers = new ArrayList<>(keySet);
                        Collections.sort(integers);

                        // 以选择的画质为中点，分别获取上一个和下一个清晰度
                        int midpoint = integers.indexOf(qualityCode);

                        int pre = midpoint - 1;
                        int next = midpoint + 1;

                        // 默认获取下一个清晰度，如果没有，则获取上一个
                        // 如果都不符合，则下载第一次获取的视频流
                        if (next < integers.size()) {
                            videoWithFlv = videoWithFlvParser.parseData(cid, String.valueOf(integers.get(next)), isBangumi, false);
                        }
//                        else if (pre >= 0) {
//                            videoWithFlv = videoWithFlvParser.parseData(cid, String.valueOf(integers.get(pre)), isBangumi, false);
//                        }
                    }

                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("videoWithFlv", videoWithFlv);

                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });
        }
    }
}
