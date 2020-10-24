package com.leon.biuvideo.adapters.UpMaster;

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
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.utils.WebpSizes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpVideoAdapter extends RecyclerView.Adapter {
    private List<UpVideo> upVideos;
    private Context context;

    public UpVideoAdapter(List<UpVideo> upVideos, Context context) {
        this.upVideos = upVideos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.up_media_list_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder innerHolder = (ViewHolder) holder;

        UpVideo upVideo = upVideos.get(position);

        //设置封面
        Glide.with(context).load(upVideo.cover + WebpSizes.cover).into(innerHolder.up_media_imageView_cover);

        //判断是否是和其他人进行合作
        //1为合作
        //0为单人
        int isUnionVideo = upVideo.isUnionVideo;
        if (isUnionVideo == 1) {
            innerHolder.up_media_textView_isUnionmedia.setVisibility(View.VISIBLE);
        } else {
            innerHolder.up_media_textView_isUnionmedia.setVisibility(View.INVISIBLE);
        }

        //设置播放时长
        innerHolder.up_media_textView_mediaLength.setText(upVideo.length);

        //设置播放次数
        innerHolder.up_media_textView_play.setText(upVideo.play + "次播放");

        //设置上传日期
        innerHolder.up_media_textView_ctime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(upVideo.create * 1000)));

        //设置标题部分
        innerHolder.up_media_textView_title.setText(upVideo.title);
    }

    @Override
    public int getItemCount() {
        return upVideos.size();
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

            up_media_imageView_cover = itemView.findViewById(R.id.up_media_imageView_cover);
            up_media_textView_isUnionmedia = itemView.findViewById(R.id.up_media_textView_isUnionmedia);
            up_media_textView_mediaLength = itemView.findViewById(R.id.up_media_textView_mediaLength);
            up_media_textView_title = itemView.findViewById(R.id.up_media_textView_title);
            up_media_textView_play = itemView.findViewById(R.id.up_media_textView_play);
            up_media_textView_ctime = itemView.findViewById(R.id.up_media_textView_ctime);
        }
    }

    //加载数据使用
    public void refresh(List<UpVideo> addOns) {
        int position = upVideos.size();

        upVideos.addAll(position, addOns);
        notifyDataSetChanged();
    }
}
