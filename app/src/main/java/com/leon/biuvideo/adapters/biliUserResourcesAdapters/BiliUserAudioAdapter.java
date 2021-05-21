package com.leon.biuvideo.adapters.biliUserResourcesAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserAudio;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

/**
 * @Author Leon
 * @Time 2021/4/10
 * @Desc B站用户音配数据适配器
 */
public class BiliUserAudioAdapter extends BaseAdapter<BiliUserAudio> {
    private final MainActivity mainActivity;

    public BiliUserAudioAdapter(MainActivity mainActivity, Context context) {
        super(context);
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.bili_user_audio_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BiliUserAudio biliUserAudio = getAllData().get(position);

        holder
                .setImage(R.id.bili_user_audio_item_cover, biliUserAudio.cover, ImagePixelSize.COVER)
                .setText(R.id.bili_user_audio_item_duration, biliUserAudio.duration)
                .setText(R.id.bili_user_audio_item_title, biliUserAudio.title)
                .setText(R.id.bili_user_audio_item_pubTime, biliUserAudio.pubTime)
                .setText(R.id.bili_user_audio_item_play, biliUserAudio.play)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            startPublicFragment(mainActivity, FragmentType.AUDIO, biliUserAudio.id);
                        }
                    }
                });
    }
}
