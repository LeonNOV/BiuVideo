package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.downloadAdapter.MediaDetailAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.util.List;

public class DownloadedMusicListFragment extends BaseFragment {
    private RecyclerView fragment_downloaded_media_detail_recyclerView;
    private ImageView fragment_downloaded_media_detail_imageView_back;

    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;
    private List<DownloadedDetailMedia> downloadedDetailMedias;
    private MediaDetailAdapter mediaDetailAdapter;

    @Override
    public int setLayout() {
        return R.layout.fragment_downloaded_media_detail;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        fragment_downloaded_media_detail_recyclerView = findView(R.id.fragment_downloaded_media_detail_recyclerView);
        fragment_downloaded_media_detail_imageView_back = findView(R.id.fragment_downloaded_media_detail_imageView_back);
    }

    @Override
    public void initValues() {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
        downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();

        initBroadcast();
    }

    @Override
    public void onResume() {
        downloadedDetailMedias = downloadRecordsDatabaseUtils.queryAllMusic();

        mediaDetailAdapter = new MediaDetailAdapter(downloadedDetailMedias, context);
        fragment_downloaded_media_detail_recyclerView.setAdapter(mediaDetailAdapter);
        fragment_downloaded_media_detail_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        fragment_downloaded_media_detail_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_downloadedMusicListFragment_to_downloadedVideoListFragment);
            }
        });

        super.onResume();
    }

    public void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DownloadVideo");
        intentFilter.addAction("DownloadAudio");

        LocalReceiver localReceiver = new LocalReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    private class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DownloadVideo")) {
                String fileName = intent.getStringExtra("fileName");
                mediaDetailAdapter.refresh(fileName);
            }
        }
    }
}
