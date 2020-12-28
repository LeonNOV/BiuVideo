package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.downloadAdapter.MediaListAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.util.List;

public class DownloadedVideoListFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout fragment_downloaded_video_list_linearLayout;
    private RecyclerView fragment_downloaded_video_list_recyclerView;
    private TextView fragment_downloaded_video_list_no_data, fragment_downloaded_video_list_textView_toAudio;
    private ImageView fragment_downloaded_video_list_imageView_back;

    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;
    private List<DownloadedRecordsForVideo> downloadedRecordsForVideos;

    @Override
    public int setLayout() {
        return R.layout.fragment_downloaded_video_list;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        fragment_downloaded_video_list_linearLayout = findView(R.id.fragment_downloaded_video_list_linearLayout);
        fragment_downloaded_video_list_recyclerView = findView(R.id.fragment_downloaded_video_list_recyclerView);
        fragment_downloaded_video_list_no_data = findView(R.id.fragment_downloaded_video_list_no_data);
        fragment_downloaded_video_list_textView_toAudio = findView(R.id.fragment_downloaded_video_list_textView_toAudio);
        fragment_downloaded_video_list_imageView_back = findView(R.id.fragment_downloaded_video_list_imageView_back);
    }

    @Override
    public void initValues() {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
        downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();
        downloadedRecordsForVideos = downloadRecordsDatabaseUtils.queryAllVideo();

        if (downloadedRecordsForVideos.size() == 0) {
            fragment_downloaded_video_list_no_data.setVisibility(View.VISIBLE);
            fragment_downloaded_video_list_linearLayout.setVisibility(View.GONE);
        } else {
            fragment_downloaded_video_list_no_data.setVisibility(View.GONE);
            fragment_downloaded_video_list_linearLayout.setVisibility(View.VISIBLE);

            fragment_downloaded_video_list_imageView_back.setOnClickListener(this);
            fragment_downloaded_video_list_textView_toAudio.setOnClickListener(this);

            fragment_downloaded_video_list_recyclerView.setAdapter(new MediaListAdapter(downloadedRecordsForVideos, context));
            fragment_downloaded_video_list_recyclerView.setLayoutManager(new LinearLayoutManager(context));

            initBroadcast();
        }
    }

    public void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DownloadVideo");
        intentFilter.addAction("DownloadAudio");

        LocalReceiver localReceiver = new LocalReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_downloaded_video_list_textView_toAudio:
                Navigation.findNavController(v).navigate(R.id.action_downloadedVideoListFragment_to_downloadedMusicListFragment);
                break;
            case R.id.fragment_downloaded_video_list_imageView_back:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    private class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DownloadVideo")) {
                String fileName = intent.getStringExtra("fileName");
                Fuck.blue(fileName + "---已下载完成");
            }
        }
    }
}
