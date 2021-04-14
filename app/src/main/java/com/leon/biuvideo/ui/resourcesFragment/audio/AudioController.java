package com.leon.biuvideo.ui.resourcesFragment.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.leon.biuvideo.utils.HttpUtils;

import java.io.IOException;

/**
 * @Author Leon
 * @Time 2021/4/14
 * @Desc 音频控制器
 */
public class AudioController {
    private final Context context;

    private String audioUrl;
    private MediaPlayer mediaPlayer;

    public AudioController(Context context) {
        this.context = context;
    }

    public void setUrl (String audioUrl) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.setDataSource(context, Uri.parse(audioUrl), HttpUtils.getHeaders());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start () {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void resume () {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause () {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}
