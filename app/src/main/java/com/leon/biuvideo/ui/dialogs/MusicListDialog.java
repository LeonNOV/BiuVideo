package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.MusicPlayListAdapter;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;

import java.util.List;

/**
 * music播放列表弹窗，只在UpSongActivity中出现
 */
public class MusicListDialog extends AlertDialog {
    private List<MusicPlayList> musicPlayLists;
    private final Context context;

    public static PriorityListener priorityListener;

    public MusicListDialog(@NonNull Context context, List<MusicPlayList> musicPlayLists) {
        super(context);
        this.context = context;
        this.musicPlayLists = musicPlayLists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play_list_dialog);

        initView();
    }

    private void initView() {
        RecyclerView music_recyclerView_playList = findViewById(R.id.music_recyclerView_playList);

        Button music_button_close = findViewById(R.id.music_button_close);
        music_button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        MusicPlayListAdapter musicPlayListAdapter = new MusicPlayListAdapter(musicPlayLists, context);

        music_recyclerView_playList.setLayoutManager(layoutManager);
        music_recyclerView_playList.setAdapter(musicPlayListAdapter);

        //获取window
        Window window = this.getWindow();

        //添加缩放动画
        window.setWindowAnimations(R.style.music_list_dialog);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        //限制大小
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (widthPixels * 0.8f);
        attributes.height = (int) (heightPixels * 0.8f);
    }

    @Override
    public void show() {
        super.show();
    }

    public interface PriorityListener {

        //回调函数，用于刷新UpSongActivity
        void refreshFavoriteIcon();

        //回调函数，用于切换歌曲
        void refreshMusic(long sid);
    }
}
