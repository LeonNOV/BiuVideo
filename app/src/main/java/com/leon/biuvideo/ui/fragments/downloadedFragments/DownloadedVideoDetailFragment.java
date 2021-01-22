package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.downloadAdapter.DownloadedDetailAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;

import java.util.List;

/**
 * 显示已缓存的同一bvid视频的fragment
 */
public class DownloadedVideoDetailFragment extends BaseFragment {
    private DownloadedDetailAdapter downloadedDetailAdapter;

    @Override
    public int setLayout() {
        return R.layout.fragment_downloaded_media_detail;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String mainId = arguments.getString("mainId");

            DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(context);
            List<DownloadedDetailMedia> downloadedDetailMedia = downloadRecordsDatabaseUtils.queryAllSubVideo(mainId);

            RecyclerView fragment_downloaded_media_detail_recyclerView = findView(R.id.fragment_downloaded_media_detail_recyclerView);
            bindingUtils.setOnClickListener(R.id.fragment_downloaded_media_detail_imageView_back, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_downloadedVideoDetailFragment_to_downloadedVideoListFragment);
                }
            });

            downloadedDetailAdapter = new DownloadedDetailAdapter(downloadedDetailMedia, context);
            fragment_downloaded_media_detail_recyclerView.setAdapter(downloadedDetailAdapter);
            fragment_downloaded_media_detail_recyclerView.setLayoutManager(new LinearLayoutManager(context));
            bindingUtils.setOnClickListener(R.id.fragment_downloaded_media_detail_imageView_back, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_downloadedVideoDetailFragment_to_downloadedVideoListFragment);
                }
            });
        } else {
            Snackbar.make(view, "找不到此视频选集信息~", Snackbar.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
        }
    }

    @Override
    public void initValues() {
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
            if (intent.getAction().equals("DownloadVideo")) {
                String fileName = intent.getStringExtra("fileName");
                downloadedDetailAdapter.refresh(fileName);
            }
        }
    }
}
