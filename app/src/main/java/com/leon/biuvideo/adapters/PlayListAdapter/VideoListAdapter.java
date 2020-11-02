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

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.upMasterBean.VideoPlayList;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.WebpSizes;

import java.util.List;

/**
 * video播放列表适配器
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private List<VideoPlayList> videoPlayLists;
    private final Context context;

    public VideoListAdapter(List<VideoPlayList> videoPlayLists, Context context) {
        this.videoPlayLists = videoPlayLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.play_list_video_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoPlayList videoPlayList = videoPlayLists.get(position);

        holder.play_list_video_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoItemClickListener != null) {
                    onVideoItemClickListener.onVideoItemClickListener(position);
                }
            }
        });

        Glide.with(context).load(videoPlayList.coverUrl + WebpSizes.cover).into(holder.play_list_video_imageView_cover);
        holder.play_list_video_textView_length.setText(ValueFormat.lengthGenerate(videoPlayList.length));
        holder.play_list_video_textView_desc.setText(videoPlayList.desc);
        holder.play_list_video_imageView_userName.setText(videoPlayList.uname);
        holder.play_list_video_textView_play.setText(ValueFormat.generateCN(videoPlayList.play));
        holder.play_list_video_textView_danmaku.setText(ValueFormat.generateCN(videoPlayList.danmaku));
    }

    @Override
    public int getItemCount() {
        return videoPlayLists.size();
    }

    private OnVideoItemClickListener onVideoItemClickListener;

    public interface OnVideoItemClickListener {
        void onVideoItemClickListener(int position);
    }

    public void setOnVideoItemClickListener(OnVideoItemClickListener onVideoItemClickListener) {
        this.onVideoItemClickListener = onVideoItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout play_list_video_relativeLayout;

        ImageView play_list_video_imageView_cover;
        TextView
                play_list_video_textView_length,
                play_list_video_textView_desc,
                play_list_video_imageView_userName,
                play_list_video_textView_play,
                play_list_video_textView_danmaku;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            play_list_video_relativeLayout = itemView.findViewById(R.id.play_list_video_relativeLayout);

            play_list_video_imageView_cover = itemView.findViewById(R.id.play_list_video_imageView_cover);

            play_list_video_textView_length = itemView.findViewById(R.id.play_list_video_textView_length);
            play_list_video_textView_desc = itemView.findViewById(R.id.play_list_video_textView_desc);
            play_list_video_imageView_userName = itemView.findViewById(R.id.play_list_video_imageView_userName);
            play_list_video_textView_play = itemView.findViewById(R.id.play_list_video_textView_play);
            play_list_video_textView_danmaku = itemView.findViewById(R.id.play_list_video_textView_danmaku);
        }
    }
}
