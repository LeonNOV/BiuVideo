package com.leon.biuvideo.ui.home.downloadManagerFragments;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.DownloadedDetailAdapter;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/14
 * @Desc 已下载多选集详细页面
 */
public class DownloadedDetailFragment extends BaseSupportFragment {
    private final String title;
    private List<DownloadHistory> downloadHistoryList;
    private final boolean isVideo;

    public DownloadedDetailFragment(String title, List<DownloadHistory> downloadHistoryList, boolean isVideo) {
        this.title = title;
        this.isVideo = isVideo;

        if (downloadHistoryList != null) {
            this.downloadHistoryList = downloadHistoryList;
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.downloaded_detail_fragment;
    }

    @Override
    protected void initView() {
        if (!isVideo) {
            downloadHistoryList = new DownloadHistoryUtils(context).getDownloadHistoryDaoUtils().
                    queryByQueryBuilder(DownloadHistoryDao.Properties.IsCompleted.eq(true), DownloadHistoryDao.Properties.ResType.eq(ResourceDownloadTask.RES_TYPE_AUDIO));
        }

        ((SimpleTopBar) findView(R.id.downloaded_detail_topBar)).setTopBarTitle(title).setBackListener(this::backPressed);

        LoadingRecyclerView downloadedDetailData = findView(R.id.downloaded_detail_data);
        downloadedDetailData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        DownloadedDetailAdapter downloadedDetailAdapter = new DownloadedDetailAdapter(context);
        downloadedDetailAdapter.setHasStableIds(true);
        downloadedDetailData.setRecyclerViewAdapter(downloadedDetailAdapter);

        if (downloadHistoryList != null && downloadHistoryList.size() > 0) {
            downloadedDetailAdapter.append(downloadHistoryList);
            downloadedDetailData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
        } else {
            downloadedDetailData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
        }
    }
}