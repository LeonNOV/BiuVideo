package com.leon.biuvideo.adapters.PlayListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;

import java.util.List;

/**
 * music播放列表适配器
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private List<MusicPlayList> musicPlayLists;
    private Context context;

    public MusicListAdapter(List<MusicPlayList> musicPlayLists, Context context) {
        this.musicPlayLists = musicPlayLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.play_list_music_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicPlayList musicPlayList = musicPlayLists.get(position);

        holder.play_list_music_textView_serial.setText(String.valueOf(position + 1));
        holder.play_list_music_textView_musicName.setText(musicPlayList.musicName);
        holder.play_list_video_author.setText(musicPlayList.author);

        holder.play_list_music_imageView_isHaveVideo.setVisibility(musicPlayList.isHaveVideo ? View.VISIBLE : View.INVISIBLE);
        holder.play_list_music_imageView_isHaveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMuiscItemClickListener != null) {
                    onMuiscItemClickListener.onMusicVideoClickListener(position);
                }
            }
        });

        holder.play_list_music_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMuiscItemClickListener != null) {
                    long[] sids = new long[musicPlayLists.size()];
                    for (int i = 0; i < musicPlayLists.size(); i++) {
                        sids[i] = musicPlayLists.get(i).sid;
                    }

                    onMuiscItemClickListener.onMusicItemClickListener(position, sids);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicPlayLists.size();
    }

    private OnMuiscItemClickListener onMuiscItemClickListener;

    public interface OnMuiscItemClickListener {
        void onMusicItemClickListener(int position, long[] sids);
        void onMusicVideoClickListener(int position);
    }

    public void setOnMuiscItemClickListener(OnMuiscItemClickListener onMuiscItemClickListener) {
        this.onMuiscItemClickListener = onMuiscItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout play_list_music_relativeLayout;
        TextView
                play_list_music_textView_serial,
                play_list_music_textView_musicName,
                play_list_video_author;
        ImageView play_list_music_imageView_isHaveVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            play_list_music_relativeLayout = itemView.findViewById(R.id.play_list_music_relativeLayout);
            play_list_music_textView_serial = itemView.findViewById(R.id.play_list_music_textView_serial);
            play_list_music_textView_musicName = itemView.findViewById(R.id.play_list_music_textView_musicName);
            play_list_video_author = itemView.findViewById(R.id.play_list_video_author);
            play_list_music_imageView_isHaveVideo = itemView.findViewById(R.id.play_list_music_imageView_isHaveVideo);
        }
    }
}
