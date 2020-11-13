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
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.dataUtils.VideoListDatabaseUtils;

import java.util.List;

/**
 * PlayListFragment中的video片段
 */
public class VideoListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView textView_noDataStr;

    private Context context;
    private View view;

    private VideoListAdapter videoListAdapter;
    private List<VideoPlayList> videoPlayLists;

    private VideoListDatabaseUtils videoListDatabaseUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        context = getContext();
        view = getView();

        videoListDatabaseUtils = new VideoListDatabaseUtils(context);

        recyclerView = view.findViewById(R.id.recyclerView);
        textView_noDataStr = view.findViewById(R.id.textView_noDataStr);

        videoPlayLists = videoListDatabaseUtils.queryFavoriteVideos();
        videoListAdapter = new VideoListAdapter(videoPlayLists, getContext());
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

    @Override
    public void onResume() {
        super.onResume();

        //处理video播放列表数据
        videoPlayLists = videoListDatabaseUtils.queryFavoriteVideos();

        Fuck.blue("VideoListFragment:onResume");

        if (videoPlayLists.size() > 0) {
            //隐藏无数据提示，显示item数据
            textView_noDataStr.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            videoListAdapter.refresh(videoPlayLists);
        } else {
            textView_noDataStr.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }
}
