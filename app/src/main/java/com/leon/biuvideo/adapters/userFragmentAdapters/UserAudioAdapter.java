package com.leon.biuvideo.adapters.userFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.upMasterBean.Audio;
import com.leon.biuvideo.ui.activitys.MusicActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 用户界面，音频/音乐fragment适配器
 */
public class UserAudioAdapter extends BaseAdapter<Audio> {
    private final List<Audio> audio;
    private final Context context;

    public UserAudioAdapter(List<Audio> audio, Context context) {
        super(audio, context);
        this.audio = audio;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_media_list_view_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Audio audio = this.audio.get(position);

        //设置封面
        holder.setImage(R.id.up_media_imageView_cover, audio.cover, ImagePixelSize.COVER)

                //设置播放时长
                .setText(R.id.up_media_textView_mediaLength, ValueUtils.lengthGenerate(audio.duration))

                //不显示合作标识
                .setVisibility(R.id.up_media_textView_isUnionmedia, View.INVISIBLE)

                //设置播放次数
                .setText(R.id.up_media_textView_play, ValueUtils.generateCN(audio.play))

                //设置上传日期
                .setText(R.id.up_media_textView_ctime, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(audio.ctime)))

                //设置标题
                .setText(R.id.up_media_textView_title, audio.title)

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

                        //获取所有sid
                        String[] sids = new String[UserAudioAdapter.this.audio.size()];
                        for (int i = 0; i < UserAudioAdapter.this.audio.size(); i++) {
                            sids[i] = (UserAudioAdapter.this.audio.get(i).sid);
                        }

                        Intent intent = new Intent(context, MusicActivity.class);

                        //传递sid在upAudios中的position
                        intent.putExtra("position", position);

                        //传递所有的sid
                        intent.putExtra("sids", sids);

                        context.startActivity(intent);
                    }
                });
    }

    //加载数据使用
    public void refresh(List<Audio> addOns) {
        int position = audio.size();

        audio.addAll(position, addOns);
        notifyDataSetChanged();
    }
}
