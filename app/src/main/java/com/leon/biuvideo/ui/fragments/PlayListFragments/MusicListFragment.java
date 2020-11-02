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
import com.leon.biuvideo.adapters.PlayListAdapter.MusicListAdapter;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;
import com.leon.biuvideo.ui.activitys.UpSongActivity;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.MusicListDatabaseUtils;

import java.util.List;

/**
 * PlayListFragment中的music片段
 */
public class MusicListFragment extends Fragment {
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

        MusicListDatabaseUtils musicListDatabaseUtils = new MusicListDatabaseUtils(context);

        //获取所有喜欢的music
        List<MusicPlayList> musicPlayLists = musicListDatabaseUtils.queryPlayList();

        //判断是否有数据
        if (musicPlayLists.size() == 0) {
            textView_noDataStr.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            textView_noDataStr.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        MusicListAdapter musicListAdapter = new MusicListAdapter(musicPlayLists, context);
        musicListAdapter.setOnMuiscItemClickListener(new MusicListAdapter.OnMuiscItemClickListener() {
            @Override
            public void onMusicItemClickListener(int position, long[] sids) {
                //跳转到UpSongActivity
                Intent intent = new Intent(context, UpSongActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("sids", sids);

                startActivity(intent);
            }

            @Override
            public void onMusicVideoClickListener(int position) {
                //跳转到VideoActivity
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("bvid", musicPlayLists.get(position).bvid);

                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(musicListAdapter);
    }
}
