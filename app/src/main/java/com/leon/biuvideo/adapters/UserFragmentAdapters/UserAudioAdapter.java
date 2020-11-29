package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.upMasterBean.UpAudio;
import com.leon.biuvideo.ui.activitys.UpSongActivity;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 用户界面，音频/音乐fragment适配器
 */
public class UserAudioAdapter extends BaseAdapter<UpAudio> {
    private List<UpAudio> upAudios;
    private final Context context;

    public UserAudioAdapter(List<UpAudio> upAudios, Context context) {
        super(upAudios, context);
        this.upAudios = upAudios;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_media_list_view_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        UpAudio upAudio = upAudios.get(position);

        //设置封面
        holder.setImage(R.id.up_media_imageView_cover, upAudio.cover, ImagePixelSize.COVER)

                //设置播放时长
                .setText(R.id.up_media_textView_mediaLength, ValueFormat.lengthGenerate(upAudio.duration))

                //不显示合作标识
                .setVisibility(R.id.up_media_textView_isUnionmedia, View.INVISIBLE)

                //设置播放次数
                .setText(R.id.up_media_textView_play, ValueFormat.generateCN(upAudio.play))

                //设置上传日期
                .setText(R.id.up_media_textView_ctime, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(upAudio.ctime)))

                //设置标题
                .setText(R.id.up_media_textView_title, upAudio.title)

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

                        //获取所有sid
                        long[] sids = new long[upAudios.size()];
                        for (int i = 0; i < upAudios.size(); i++) {
                            sids[i] = (upAudios.get(i).sid);
                        }

                        Intent intent = new Intent(context, UpSongActivity.class);

                        //传递sid在upAudios中的position
                        intent.putExtra("position", position);

                        //传递所有的sid
                        intent.putExtra("sids", sids);

                        context.startActivity(intent);
                    }
                });
    }

    //加载数据使用
    public void refresh(List<UpAudio> addOns) {
        int position = upAudios.size();

        upAudios.addAll(position, addOns);
        notifyDataSetChanged();
    }
}
