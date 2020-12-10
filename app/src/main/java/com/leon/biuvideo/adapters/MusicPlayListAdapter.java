package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;
import com.leon.biuvideo.ui.dialogs.MusicListDialog;
import com.leon.biuvideo.utils.dataBaseUtils.MusicListDatabaseUtils;

import java.util.List;

/**
 * 音乐播放界面中播放列表的适配器
 */
public class MusicPlayListAdapter extends BaseAdapter<MusicPlayList> {
    public List<MusicPlayList> musicPlayLists;
    private final Context context;

    private MusicListDialog.PriorityListener priorityListener;

    public MusicPlayListAdapter(List<MusicPlayList> musicPlayLists, Context context) {
        super(musicPlayLists, context);
        this.musicPlayLists = musicPlayLists;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.music_play_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        MusicPlayList musicPlayList = musicPlayLists.get(position);
        priorityListener = MusicListDialog.priorityListener;

        holder.setText(R.id.music_textView_playList_title, musicPlayList.musicName)
                .setText(R.id.music_textView_playList_author, musicPlayList.author)
                .setOnClickListener(R.id.music_imageView_playList_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除播放列表中的对应条目
                        MusicListDatabaseUtils musicDatabaseUtils = new MusicListDatabaseUtils(context);

                        musicDatabaseUtils.removeMusicItem(musicPlayList.sid);

                        musicPlayLists.remove(position);

                        //通知数据已发生改变
                        notifyItemRemoved(position);

                        //刷新UpSongActivity红心的状态
                        priorityListener.refreshFavoriteIcon();
                    }
                })

                //切换歌曲
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //切换当前歌曲为歌单内选定的歌曲
                        priorityListener.refreshMusic(musicPlayList.sid);
                    }
                });
    }
}
