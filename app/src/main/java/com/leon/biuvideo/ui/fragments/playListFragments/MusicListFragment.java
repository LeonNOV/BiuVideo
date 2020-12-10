package com.leon.biuvideo.ui.fragments.playListFragments;

import android.content.Context;
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
import com.leon.biuvideo.adapters.PlayListAdapters.MusicListAdapter;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;
import com.leon.biuvideo.utils.dataBaseUtils.MusicListDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.dataBaseUtils.Tables;

import java.util.List;

/**
 * PlayListFragment中的music片段
 */
public class MusicListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView textView_noDataStr;

    private List<MusicPlayList> musicPlayLists;
    private MusicListAdapter musicListAdapter;
    private MusicListDatabaseUtils musicListDatabaseUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_recycler_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        Context context = getContext();
        View view = getView();

        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.MusicPlayList);
        musicListDatabaseUtils = (MusicListDatabaseUtils) sqLiteHelperFactory.getInstance();

        recyclerView = view.findViewById(R.id.recyclerView);
        textView_noDataStr = view.findViewById(R.id.textView_noDataStr);

        musicPlayLists = musicListDatabaseUtils.queryPlayList();
        musicListAdapter = new MusicListAdapter(musicPlayLists, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(musicListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        //处理music播放列表数据
        musicPlayLists = musicListDatabaseUtils.queryPlayList();

        if (musicPlayLists.size() > 0) {
            textView_noDataStr.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            musicListAdapter.refresh(musicPlayLists);
        } else {
            textView_noDataStr.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        musicListDatabaseUtils.close();
    }
}
