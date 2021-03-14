package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.downloadAdapter.DownloadedFailListAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.SimpleDownloadThread;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * 缓存失败fragment
 */
public class DownloadFailListFragment extends BaseFragment {
    private RecyclerView fragment_downloaded_media_detail_recyclerView;
    private SimpleThreadPool simpleThreadPool;

    private DownloadedFailListAdapter downloadedFailListAdapter;

    @Override
    public int setLayout() {
        return R.layout.fragment_downloaded_media_detail;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        fragment_downloaded_media_detail_recyclerView = findView(R.id.fragment_downloaded_media_detail_recyclerView);
        bindingUtils.setOnClickListener(R.id.fragment_downloaded_media_detail_imageView_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    @Override
    public void initValues() {
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(context);
        List<DownloadedDetailMedia> downloadedDetailMedias = downloadRecordsDatabaseUtils.queryDownloadFailMedia();

        downloadedFailListAdapter = new DownloadedFailListAdapter(context, downloadedDetailMedias);
        downloadedFailListAdapter.setOnClickFailedItemListener(new DownloadedFailListAdapter.OnClickFailedItemListener() {
            @Override
            public void onClick(int position) {
                DownloadedDetailMedia downloadedDetailMedia = downloadedDetailMedias.get(position);

                if (downloadedDetailMedia.isVideo) {
                    SimpleThreadPool.submit(new FutureTask<>(new SimpleDownloadThread(context, downloadedDetailMedia.mainId, downloadedDetailMedia.subId, downloadedDetailMedia.qualityId, downloadedDetailMedia.videoUrl, downloadedDetailMedia.audioUrl, downloadedDetailMedia.fileName)), null);
                } else {
                    SimpleThreadPool.submit(new FutureTask<>(new SimpleDownloadThread(context, downloadedDetailMedia.mainId, downloadedDetailMedia.audioUrl, downloadedDetailMedia.fileName)), null);
                }
            }
        });
        fragment_downloaded_media_detail_recyclerView.setAdapter(downloadedFailListAdapter);
        fragment_downloaded_media_detail_recyclerView.setLayoutManager(new LinearLayoutManager(context));

        initBroadcast();
    }

    public void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DownloadVideo");
        intentFilter.addAction("DownloadAudio");

        LocalReceiver localReceiver = new LocalReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String fileName = intent.getStringExtra("fileName");
            downloadedFailListAdapter.refresh(fileName);
        }
    }
}
