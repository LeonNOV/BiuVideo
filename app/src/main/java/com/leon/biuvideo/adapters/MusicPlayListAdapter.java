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
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;

import java.util.List;

/**
 * 音乐播放界面中播放列表的适配器
 */
public class MusicPlayListAdapter extends RecyclerView.Adapter<MusicPlayListAdapter.ViewHolder> {
    public List<MusicPlayList> musicPlayLists;
    private final Context context;

    public MusicPlayListAdapter(List<MusicPlayList> musicPlayLists, Context context) {
        this.musicPlayLists = musicPlayLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_play_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicPlayList musicPlayList = musicPlayLists.get(position);

        String title = musicPlayList.musicName + " - " + musicPlayList.author;
        holder.music_textView_playList_title.setText(title);
    }

    @Override
    public int getItemCount() {
        return musicPlayLists.size();
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
        TextView music_textView_playList_title;
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
