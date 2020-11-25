package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.ValueFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 用户界面，视频fragment适配器
 */
public class UserVideoAdapter extends BaseAdapter<UpVideo> {
    private List<UpVideo> upVideos;
    private final Context context;

    public UserVideoAdapter(List<UpVideo> upVideos, Context context) {
        super(upVideos, context);
        this.upVideos = upVideos;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_media_list_view_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        UpVideo upVideo = upVideos.get(position);

        //设置封面
        holder.setImage(R.id.up_media_imageView_cover, upVideo.cover, ImagePixelSize.COVER)

                //判断视频是否是和其他人进行合作
                //1为合作
                //0为单人
                .setVisibility(R.id.up_media_textView_isUnionmedia, upVideo.isUnionVideo == 1 ? View.VISIBLE : View.INVISIBLE)

                //设置播放时长
                .setText(R.id.up_media_textView_mediaLength, upVideo.length)

                //设置播放次数
                .setText(R.id.up_media_textView_play, ValueFormat.generateCN(upVideo.play))

                //设置上传日期
                .setText(R.id.up_media_textView_ctime, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(upVideo.create * 1000)))

                //设置标题部分
                .setText(R.id.up_media_textView_title, upVideo.title)

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到VideoActivity
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("bvid", upVideo.bvid);
                        context.startActivity(intent);
                    }
                });
    }

    /**
     * 刷新加载数据
     *
     * @param addOns    要加入的数据
     */
    public void append(List<UpVideo> addOns) {
        upVideos.addAll(addOns);
        notifyDataSetChanged();
    }

    /**
     * 清空上次搜索后的数据缓存
     */
    public void removeAll() {
        upVideos.clear();
    }
}