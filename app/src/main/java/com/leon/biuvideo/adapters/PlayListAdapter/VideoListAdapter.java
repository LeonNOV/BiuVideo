package com.leon.biuvideo.adapters.PlayListAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.upMasterBean.VideoPlayList;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;

import java.util.List;

/**
 * video播放列表适配器
 */
public class VideoListAdapter extends BaseAdapter<VideoPlayList> {
    private List<VideoPlayList> videoPlayLists;
    private final Context context;

    public VideoListAdapter(List<VideoPlayList> videoPlayLists, Context context) {
        super(videoPlayLists, context);
        this.videoPlayLists = videoPlayLists;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.play_list_video_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        VideoPlayList videoPlayList = videoPlayLists.get(position);

        holder.setOnClickListener(R.id.play_list_video_relativeLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();
                    return;
                }

                //跳转到VideoActivity
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("bvid", videoPlayLists.get(position).bvid);

                context.startActivity(intent);
            }
        })
                //设置封面
                .setImage(R.id.play_list_video_imageView_cover, videoPlayList.coverUrl, ImagePixelSize.COVER)

                //设置视频时长
                .setText(R.id.play_list_video_textView_length, ValueFormat.lengthGenerate(videoPlayList.length))

                //设置视频标题
                .setText(R.id.play_list_video_textView_desc, videoPlayList.title)

                //设置作者
                .setText(R.id.play_list_video_imageView_userName, videoPlayList.uname)

                //设置播放量
                .setText(R.id.play_list_video_textView_play, ValueFormat.generateCN(videoPlayList.play))
                .setText(R.id.play_list_video_textView_danmaku, ValueFormat.generateCN(videoPlayList.danmaku));
    }

    //加载数据使用
    public void refresh(List<VideoPlayList> addOns) {
        //清空原有数据
        if (addOns.size() > 0) {
            if (videoPlayLists.size() > 0) {
                videoPlayLists.clear();
            }

            videoPlayLists.addAll(addOns);
            notifyDataSetChanged();
        }
    }
}
