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
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.dataUtils.MusicListDatabaseUtils;

import java.util.List;

/**
 * PlayListFragment中的music片段
 */
public class MusicListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView textView_noDataStr;

    private Context context;
    private View view;

    private List<MusicPlayList> musicPlayLists;
    private MusicListAdapter musicListAdapter;
    private MusicListDatabaseUtils musicListDatabaseUtils;

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

        musicListDatabaseUtils = new MusicListDatabaseUtils(context);

        recyclerView = view.findViewById(R.id.recyclerView);
        textView_noDataStr = view.findViewById(R.id.textView_noDataStr);

        musicPlayLists = musicListDatabaseUtils.queryPlayList();
        musicListAdapter = new MusicListAdapter(musicPlayLists, context);
        musicListAdapter.setOnMusicItemClickListener(new MusicListAdapter.OnMusicItemClickListener() {
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(musicListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        //处理music播放列表数据
        musicPlayLists = musicListDatabaseUtils.queryPlayList();

        Fuck.blue("MusicListFragment:onResume");

        if (musicPlayLists.size() > 0) {
            textView_noDataStr.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            musicListAdapter.refresh(musicPlayLists);
        } else {
            textView_noDataStr.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }
}
