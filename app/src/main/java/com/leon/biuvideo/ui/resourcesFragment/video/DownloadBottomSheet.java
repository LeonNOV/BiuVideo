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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthology;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoWithFlvParser;
import com.leon.biuvideo.values.Quality;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
                listPopupWindow.setAdapter(new ArrayAdapter<>(context, R.layout.search_result_menu_item, arrayList));
                listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
                listPopupWindow.setBackgroundDrawable(context.getDrawable(R.drawable.round_corners6dp_bg));
                listPopupWindow.setAnchorView(tagView);
                listPopupWindow.setDropDownGravity(Gravity.START);
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tagView.setRightValue(arrayList.get(position));
                        quality = arrayList.get(position);
                        qualityCode = qualityMap.get(arrayList.get(position));
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

                    ImageView imageView = (ImageView) msg.obj;
                    ResourceDownloadTask resourceDownloadTask = new ResourceDownloadTask(context, downloadHistory, data.getSerializable("anthologyInfo"));
                    resourceDownloadTask.setOnDownloadStatListener(new ResourceDownloadTask.OnDownloadStatListener() {
                        @Override
                        public void onCompleted() {
                            imageView.setSelected(true);

                            VideoInfo.VideoAnthology videoAnthology = resourceDownloadTask.getVideoAnthology();
                            if (videoAnthology != null) {
                                videoAnthology.isDownloading = false;
                                videoAnthology.isDownloaded = true;
                                return;
                            }

                            BangumiAnthology bangumiAnthology = resourceDownloadTask.getBangumiAnthology();
                            if (bangumiAnthology != null) {
                                bangumiAnthology.isDownloading = false;
                                bangumiAnthology.isDownloaded = true;
                            }
                        }

                        @Override
                        public void onCancel() {
                            imageView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailed() {
                            imageView.setVisibility(View.INVISIBLE);
                        }
                    });
                    resourceDownloadTask.startDownload();
                    downloadHistory = null;

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
                setBangumiAnthologyData(holder, bangumiAnthologyList.get(position), position);
            } else {
                setVideoAnthologyData(holder, videoAnthologyList.get(position), position);
            }
        }

        private void setBangumiAnthologyData(BaseViewHolder holder, BangumiAnthology bangumiAnthology, int position) {
            ImageView downloadBottomSheetAnthologyDownloadStat = holder.findById(R.id.download_bottom_sheet_anthology_download_stat);
            downloadBottomSheetAnthologyDownloadStat.setVisibility(bangumiAnthology.isDownloading || bangumiAnthology.isDownloaded ? View.VISIBLE : View.INVISIBLE);
            downloadBottomSheetAnthologyDownloadStat.setSelected(bangumiAnthology.isDownloading && bangumiAnthology.isDownloaded);
            holder
                    .setText(R.id.download_bottom_sheet_anthology_item_title, bangumiAnthology.longTitle)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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

                            getBangumiDownloadInfo(bangumiAnthology, downloadBottomSheetAnthologyDownloadStat);
                        }
                    });
        }

        private void setVideoAnthologyData(BaseViewHolder holder, VideoInfo.VideoAnthology videoAnthology, int position) {
            ImageView downloadBottomSheetAnthologyDownloadStat = holder.findById(R.id.download_bottom_sheet_anthology_download_stat);
            downloadBottomSheetAnthologyDownloadStat.setVisibility(videoAnthology.isDownloading || videoAnthology.isDownloaded ? View.VISIBLE : View.INVISIBLE);
            downloadBottomSheetAnthologyDownloadStat.setSelected(videoAnthology.isDownloading && videoAnthology.isDownloaded);

            holder
                    .setText(R.id.download_bottom_sheet_anthology_item_title, videoAnthology.part)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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

                            getVideoDownloadInfo(videoAnthology, downloadBottomSheetAnthologyDownloadStat);
                        }
                    });
        }

        /**
         * 获取番剧下载信息
         */
        private void getBangumiDownloadInfo(BangumiAnthology bangumiAnthology, ImageView downloadBottomSheetAnthologyDownloadStat) {
            downloadHistory = new DownloadHistory();
            downloadHistory.setResType(ResourceDownloadTask.RES_TYPE_VIDEO);
            downloadHistory.setIsFailed(false);
            downloadHistory.setIsCompleted(false);
            downloadHistory.setIsMultipleAnthology(true);
            downloadHistory.setLevelOneId(bangumiAnthology.seasonId);
            downloadHistory.setLevelTwoId(bangumiAnthology.cid);
            downloadHistory.setTitle(bangumiAnthology.longTitle);
            downloadHistory.setSubTitle(quality.replace(" ", ""));
            downloadHistory.setCoverUrl(bangumiAnthology.cover);

            getVideoStreamUrl(bangumiAnthology, downloadBottomSheetAnthologyDownloadStat, bangumiAnthology.seasonId, bangumiAnthology.cid);
        }

        /**
         * 获取视频下载信息
         */
        private void getVideoDownloadInfo(VideoInfo.VideoAnthology videoAnthology, ImageView imageView) {
            downloadHistory = new DownloadHistory();
            downloadHistory.setResType(ResourceDownloadTask.RES_TYPE_VIDEO);
            downloadHistory.setIsFailed(false);
            downloadHistory.setIsCompleted(false);
            downloadHistory.setIsMultipleAnthology(true);
            downloadHistory.setLevelOneId(videoAnthology.mainId);
            downloadHistory.setLevelTwoId(videoAnthology.cid);
            downloadHistory.setTitle(videoAnthology.part);
            downloadHistory.setSubTitle(quality.replace(" ", ""));
            downloadHistory.setCoverUrl(videoAnthology.cover);

            getVideoStreamUrl(videoAnthology, imageView, videoAnthology.mainId, videoAnthology.cid);
        }

        /**
         * 获取视频流链接
         *
         * @param anthology                                选集数据
         * @param downloadBottomSheetAnthologyDownloadStat ImageView
         * @param levelOneId                               levelOneId
         * @param cid                                      选集ID
         */
        private void getVideoStreamUrl(Serializable anthology, ImageView downloadBottomSheetAnthologyDownloadStat, String levelOneId, String cid) {
            if (videoWithFlvParser == null) {
                videoWithFlvParser = new VideoWithFlvParser(levelOneId);
            }

            SimpleSingleThreadPool.executor(new Runnable() {
                @Override
                public void run() {
                    VideoWithFlv videoWithFlv = videoWithFlvParser.parseData(cid, null, isBangumi, false);

                    Message message = handler.obtainMessage();
                    message.obj = downloadBottomSheetAnthologyDownloadStat;

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("videoWithFlv", videoWithFlv);
                    bundle.putSerializable("anthologyInfo", anthology);

                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });
        }


    }
}
