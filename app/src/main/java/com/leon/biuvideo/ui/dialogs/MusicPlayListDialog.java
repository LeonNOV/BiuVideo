package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.MusicPlayListAdapter;
import com.leon.biuvideo.adapters.userAdapters.OnMusicListListener;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;

import java.util.List;

/**
 * music播放列表弹窗，只在UpSongActivity中出现
 */
public class MusicPlayListDialog extends AlertDialog {
    private final List<LocalOrder> localOrderList;
    private final Context context;

    public MusicPlayListDialog(@NonNull Context context, List<LocalOrder> localOrderList) {
        super(context);
        this.context = context;
        this.localOrderList = localOrderList;
    }

    private OnMusicListListener onMusicListListener;

    public void setOnMusicListListener(OnMusicListListener onMusicListListener) {
        this.onMusicListListener = onMusicListListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_music_play_list);

        initView();
    }

    private void initView() {
        RecyclerView music_recyclerView_playList = findViewById(R.id.music_recyclerView_playList);
        MusicPlayListAdapter musicPlayListAdapter = new MusicPlayListAdapter(localOrderList, context);
        musicPlayListAdapter.setOnMusicListListener(new OnMusicListListener() {
            @Override
            public void onRefreshFavoriteIcon(LocalOrder localOrder) {
                if (onMusicListListener != null) {
                    onMusicListListener.onRefreshFavoriteIcon(localOrder);
                }
            }

            @Override
            public void onSwitchMusic(String sid) {
                if (onMusicListListener != null) {
                    onMusicListListener.onSwitchMusic(sid);
                }
            }
        });
        music_recyclerView_playList.setAdapter(musicPlayListAdapter);

        Button music_button_close = findViewById(R.id.music_button_close);
        music_button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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
}
