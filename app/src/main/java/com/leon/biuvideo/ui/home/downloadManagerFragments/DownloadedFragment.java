package com.leon.biuvideo.ui.home.downloadManagerFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.View;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.DownloadedAdapter;
import com.leon.biuvideo.beans.DownloadedRecord;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;
import com.leon.biuvideo.greendao.dao.DownloadLevelOne;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.greendao.daoutils.DownloadLevelOneUtils;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.DownloadManagerFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 下载管理页面-已下载的资源
 */
public class DownloadedFragment extends BaseSupportFragment {
    private DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils;
    private DaoBaseUtils<DownloadLevelOne> downloadLevelOneDaoUtils;
    private LoadingRecyclerView downloadedData;

    @Override
    protected int setLayout() {
        return R.layout.download_manager_downloaded_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.downloading_container).setBackgroundResource(R.color.white);

        downloadedData = findView(R.id.downloaded_data);
        downloadedData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        findView(R.id.downloaded_check_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DownloadManagerFragment) getParentFragment()).start(new DownloadedDetailFragment("已下载音频", null, false));
            }
        });

        initHandler();
        getAllDownloadedRecords();
    }

    private void initHandler() {
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<DownloadedRecord> downloadedRecordList = (List<DownloadedRecord>) msg.obj;

                if (downloadedRecordList != null && downloadedRecordList.size() > 0) {
                    DownloadedAdapter downloadedAdapter = new DownloadedAdapter(context);
                    downloadedAdapter.setHasStableIds(true);
                    downloadedAdapter.setOnDownloadedItemListener(new DownloadedAdapter.OnDownloadedItemListener() {
                        @Override
                        public void onPlay(DownloadHistory downloadHistory) {
                            // 播放视频
                            // 由于该版本在下载视频时并没有同时下载弹幕文件,所以暂使用设备已有的播放器进行播放视频
                            Intent intentVideoPlayer = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse(downloadHistory.getSavePath());
                            intentVideoPlayer.setDataAndType(uri , "video/x-flv");
                            context.startActivity(intentVideoPlayer);
                        }

                        @Override
                        public void onDetail(String title, List<DownloadHistory> downloadHistoryList) {
                            ((DownloadManagerFragment) getParentFragment()).start(new DownloadedDetailFragment(title, downloadHistoryList, true));
                        }
                    });

                    downloadedData.setRecyclerViewAdapter(downloadedAdapter);
                    downloadedAdapter.append(downloadedRecordList);

                    downloadedData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                } else {
                    downloadedData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                }
            }
        });
    }

    private void getAllDownloadedRecords() {
        if (downloadHistoryDaoUtils == null || downloadLevelOneDaoUtils == null) {
            downloadHistoryDaoUtils = new DownloadHistoryUtils(context).getDownloadHistoryDaoUtils();
            downloadLevelOneDaoUtils = new DownloadLevelOneUtils(context).getDownloadLevelOneDaoBaseUtils();
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<DownloadedRecord> downloadedRecordList = new ArrayList<>();

                List<DownloadLevelOne> downloadLevelOneList = downloadLevelOneDaoUtils.queryAll();
                for (DownloadLevelOne downloadLevelOne : downloadLevelOneList) {
                    List<DownloadHistory> downloadHistoryList = downloadHistoryDaoUtils.queryByQueryBuilder(DownloadHistoryDao.Properties.IsCompleted.eq(true),
                            DownloadHistoryDao.Properties.IsFailed.eq(false), DownloadHistoryDao.Properties.LevelOneId.eq(downloadLevelOne.getLevelOneId()));

                    int size = downloadHistoryList.size();
                    if (size > 0) {
                        DownloadedRecord downloadedRecord = new DownloadedRecord();
                        downloadedRecord.count = size;
                        downloadedRecord.title = downloadLevelOne.getTitle();
                        downloadedRecord.cover = downloadLevelOne.getCoverUrl();

                        if (size == 1) {
                            downloadedRecord.downloadHistory = downloadHistoryList.get(0);
                            downloadedRecord.fileSize = ValueUtils.sizeFormat(downloadedRecord.downloadHistory.getFileSize(), true);
                        } else {
                            downloadedRecord.downloadHistoryList = downloadHistoryList;

                            downloadedRecord.fileSize = ValueUtils.
                                    sizeFormat(downloadHistoryList.stream().mapToLong(DownloadHistory::getFileSize).sum(), true);
                        }

                        downloadedRecordList.add(downloadedRecord);
                    }
                }

                Message message = receiveDataHandler.obtainMessage();
                message.obj = downloadedRecordList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
