package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.upMasterBean.UpAudio;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.WebpSizes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 用户界面，音频/音乐fragment适配器
 */
public class UserAudioAdapter extends RecyclerView.Adapter<UserAudioAdapter.ViewHolder> {
    private List<UpAudio> upAudios;
    private final Context context;

    public UserAudioAdapter(List<UpAudio> upAudios, Context context) {
        this.upAudios = upAudios;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_media_list_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAudioAdapter.ViewHolder holder, int position) {
        UpAudio upAudio = upAudios.get(position);

        //设置封面
        Glide.with(context).load(upAudio.cover + WebpSizes.cover).into(holder.up_media_imageView_cover);

        //设置播放时长
        holder.up_media_textView_mediaLength.setText(ValueFormat.lengthGenerate(upAudio.duration));

        //不显示合作标识
        holder.up_media_textView_isUnionmedia.setVisibility(View.INVISIBLE);

        //设置播放次数
        holder.up_media_textView_play.setText(ValueFormat.generateCN(upAudio.play));

        //设置上传日期
        holder.up_media_textView_ctime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(upAudio.ctime)));

        //设置标题
        holder.up_media_textView_title.setText(upAudio.title);
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return upAudios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView up_media_imageView_cover;
        TextView
                up_media_textView_isUnionmedia,
                up_media_textView_mediaLength,
                up_media_textView_title,
                up_media_textView_play,
                up_media_textView_ctime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            up_media_textView_isUnionmedia = itemView.findViewById(R.id.up_media_textView_isUnionmedia);
            up_media_imageView_cover = itemView.findViewById(R.id.up_media_imageView_cover);
            up_media_imageView_cover.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, getAdapterPosition());
                }
            });

            up_media_textView_mediaLength = itemView.findViewById(R.id.up_media_textView_mediaLength);
            up_media_textView_title = itemView.findViewById(R.id.up_media_textView_title);
            up_media_textView_play = itemView.findViewById(R.id.up_media_textView_play);
            up_media_textView_ctime = itemView.findViewById(R.id.up_media_textView_ctime);
        }
    }

    //加载数据使用
    public void refresh(List<UpAudio> addOns) {
        int position = upAudios.size();

        upAudios.addAll(position, addOns);
        notifyDataSetChanged();
    }
}
