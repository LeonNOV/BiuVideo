package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;
import com.leon.biuvideo.ui.activitys.UpSongActivity;
import com.leon.biuvideo.ui.dialogs.MusicListDialog;
import com.leon.biuvideo.utils.dataUtils.MusicListDatabaseUtils;

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

    public void setPriorityListener(MusicListDialog.PriorityListener priorityListener) {
        this.priorityListener = priorityListener;
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
                        //判断当前正在播放的歌曲
                        priorityListener.refreshMusic(musicPlayList.sid);

                        //重置索引位置
                        UpSongActivity.position = position;
                    }
                });
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClickListener(int position);

        void onItemDeleteListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout music_relativeLayout;
        TextView music_textView_playList_title, music_textView_playList_author;
        ImageView music_imageView_playList_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            music_relativeLayout = itemView.findViewById(R.id.music_relativeLayout);
            music_relativeLayout.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(getAdapterPosition());
                }
            });

            music_textView_playList_title = itemView.findViewById(R.id.music_textView_playList_title);
            music_textView_playList_author = itemView.findViewById(R.id.music_textView_playList_author);

            music_imageView_playList_delete = itemView.findViewById(R.id.music_imageView_playList_delete);
            music_imageView_playList_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemDeleteListener(getAdapterPosition());
                }
            });
        }
    }
}
