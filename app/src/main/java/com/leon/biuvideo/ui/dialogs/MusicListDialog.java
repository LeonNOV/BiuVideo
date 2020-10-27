package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.MusicPlayListAdapter;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;

import java.util.List;

public class MusicListDialog extends AlertDialog {
    private List<MusicPlayList> musicPlayLists;
    private Context context;

    private RecyclerView music_recyclerView_playList;
    private Button music_button_close;

    private OnDialogClickListener onDialogClickListener;

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
        MusicPlayListAdapter musicPlayListAdapter;

        music_recyclerView_playList = findViewById(R.id.music_recyclerView_playList);
        music_button_close = findViewById(R.id.music_button_close);
        music_button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onCloseDialog();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        musicPlayListAdapter = new MusicPlayListAdapter(musicPlayLists, context);
        musicPlayListAdapter.setOnItemClickListener(new MusicPlayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                //将当前播放的歌曲切换为在播放列表中选中的歌曲

                Toast.makeText(context, "选择了歌单中的第" + position + "首歌", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDeleteListener(int position) {
                //删除播放列表中的对应条目

                Toast.makeText(context, "点击了第" + position + "条的删除", Toast.LENGTH_SHORT).show();
            }
        });

        music_recyclerView_playList.setLayoutManager(layoutManager);
        music_recyclerView_playList.setAdapter(musicPlayListAdapter);

        //获取window
        Window window = this.getWindow();

        //添加缩放动画
        window.setGravity(Gravity.END | Gravity.TOP); //对话框显示的位置
        window.setWindowAnimations(R.style.music_list_dialog);
    }

    @Override
    public void show() {
        super.show();
    }

    public interface OnDialogClickListener {
        void onCloseDialog();
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }
}
