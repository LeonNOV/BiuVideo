package com.leon.biuvideo.ui.fragments.PlayListFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.PlayListAdapter.VideoListAdapter;
import com.leon.biuvideo.beans.upMasterBean.VideoPlayList;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.VideoListDatabaseUtils;

import java.util.List;

/**
 * PlayListFragment中的video片段
 */
public class VideoListFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        view.setBackgroundResource(R.color.bilibili_pink_lite);

        initView();

        return view;
    }

    private void initView() {
        Context context = getContext();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        TextView textView_noDataStr = view.findViewById(R.id.textView_noDataStr);

        VideoListDatabaseUtils videoListDatabaseUtils = new VideoListDatabaseUtils(context);

        //获取所有的喜欢的video
        List<VideoPlayList> videoPlayLists = videoListDatabaseUtils.queryFavoriteVideos();

        //判断是否有数据
        if (videoPlayLists.size() == 0) {
            textView_noDataStr.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            textView_noDataStr.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        VideoListAdapter videoListAdapter = new VideoListAdapter(videoPlayLists, getContext());
        videoListAdapter.setOnVideoItemClickListener(new VideoListAdapter.OnVideoItemClickListener() {
            @Override
            public void onVideoItemClickListener(int position) {
                //跳转到VideoActivity
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("bvid", videoPlayLists.get(position).bvid);

                startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(videoListAdapter);
    }
}
