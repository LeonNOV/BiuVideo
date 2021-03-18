package com.leon.biuvideo.adapters.userAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.upMasterBean.Video;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 用户界面-视频和搜索界面-视频的RecyclerView适配器
 */
public class UserVideoAdapter extends BaseAdapter<Video> {
    private final List<Video> videos;
    private final Context context;

    public UserVideoAdapter(List<Video> videos, Context context) {
        super(videos, context);
        this.videos = videos;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_media_list_view_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Video video = videos.get(position);

        //设置封面
        holder.setImage(R.id.up_media_imageView_cover, video.cover, ImagePixelSize.COVER)

                //判断视频是否是和其他人进行合作
                //1为合作
                //0为单人
                .setVisibility(R.id.up_media_textView_isUnionmedia, video.isUnionVideo == 1 ? View.VISIBLE : View.INVISIBLE)

                //设置播放时长
                .setText(R.id.up_media_textView_mediaLength, video.length)

                //设置播放次数
                .setText(R.id.up_media_textView_play, ValueUtils.generateCN(video.play))

                //设置上传日期
                .setText(R.id.up_media_textView_ctime, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(video.create * 1000)))

                //设置标题部分
                .setText(R.id.up_media_textView_title, video.title)

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                        if (!isHaveNetwork) {
                            SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        //跳转到VideoActivity
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("bvid", video.bvid);
                        context.startActivity(intent);
                    }
                });
    }
}