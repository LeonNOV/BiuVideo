package com.leon.biuvideo.ui.resourcesFragment.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author Leon
 * @Time 2021/4/14
 * @Desc 音频控制器
 */
public class AudioController {
    /**
     * 准备前状态，初始状态
     */
    public static final int PREPARED_BEFORE = -1;

    /**
     * 播放中
     */
    public static final int PLAYING = 1;

    /**
     * 已暂停播放
     */
    public static final int PAUSED = 2;

    /**
     * 已播放完毕
     */
    public static final int FINISHED = 3;

    /**
     * 播放状态
     */
    public static int PLAY_STAT = PREPARED_BEFORE;

    private final Context context;

    private MediaPlayer mediaPlayer;
    private AudioControllerCallback audioControllerCallback;

    public int audioDuration = -1;
    private int bufferPosition = 0;

    public AudioController(Context context) {
        this.context = context;
    }

    public void setAudioControllerCallback(AudioControllerCallback audioControllerCallback) {
        this.audioControllerCallback = audioControllerCallback;
    }

    public void setUrl(String audioUrl) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "audio_thread");
            }
        });
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if (audioControllerCallback != null && PLAY_STAT == PLAYING) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        int duration = mediaPlayer.getDuration();

                        if (audioDuration == -1) {
                            audioDuration = duration;
                        }

                        audioControllerCallback.progress(currentPosition, bufferPosition, duration);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5, 500, TimeUnit.MILLISECONDS);

        try {
            mediaPlayer.setDataSource(context, Uri.parse(audioUrl), HttpUtils.getHeaders());
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    bufferPosition = percent;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audioControllerCallback.finished();
                    setPlayStat(FINISHED);
                }
            });

            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置播放状态
     */
    public void setPlayerStat() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            setPlayStat(PAUSED);
        } else {
            mediaPlayer.start();
            setPlayStat(PLAYING);
        }
    }

    public void seekTo(long position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo((int) position);
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /**
     * 设置播放状态
     *
     * @param playStat 播放状态
     */
    public void setPlayStat(int playStat) {
        PLAY_STAT = playStat;

        if (audioControllerCallback != null) {
            audioControllerCallback.playStat(PLAY_STAT);
        }
    }

    public interface AudioControllerCallback {
        /**
         * 播放完毕
         */
        void finished();

        /**
         * 进度监听
         *
         * @param currentPosition 当前进度
         * @param bufferPosition  缓冲进度
         * @param duration        总进度
         */
        void progress(int currentPosition, int bufferPosition, int duration);

        /**
         * 播放状态
         *
         * @param stat stat
         */
        void playStat(int stat);
    }
}
