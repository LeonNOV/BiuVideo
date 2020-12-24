package com.leon.biuvideo.adapters.userDataAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.userBeans.UserFolderData;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.values.ImagePixelSize;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserFavoriteFolderDetailAdapter extends BaseAdapter<UserFolderData.Media> {
    private final List<UserFolderData.Media> userFolderDatas;
    private final Context context;
    
    public UserFavoriteFolderDetailAdapter(List<UserFolderData.Media> userFolderDatas, Context context) {
        super(userFolderDatas, context);
        this.userFolderDatas = userFolderDatas;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.favorite_video_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        UserFolderData.Media media = userFolderDatas.get(position);

        //设置封面
        holder.setImage(R.id.favorite_video_imageView_cover, media.cover, ImagePixelSize.COVER)

                //设置播放时长
                .setText(R.id.favorite_video_textView_duration, ValueFormat.lengthGenerate(media.duration))

                //设置收藏日期
                .setText(R.id.favorite_video_textView_ftime, "收藏于" + new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(media.addTime * 1000)))

                //设置标题部分
                .setText(R.id.favorite_video_textView_title, media.title)

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                        if (!isHaveNetwork) {
                            Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 判断视频是否已失效
                        if (media.title.equals("已失效视频")) {
                            Toast.makeText(context, "该视频已失效", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //跳转到VideoActivity
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("bvid", media.bvid);
                        context.startActivity(intent);
                    }
                });
    }

    public void reset(List<UserFolderData.Media> addOns) {
        this.userFolderDatas.clear();
        this.userFolderDatas.addAll(addOns);

        notifyDataSetChanged();
    }
}
