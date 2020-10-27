package com.leon.biuvideo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import com.leon.biuvideo.ui.activitys.MainActivity;
import com.leon.biuvideo.ui.activitys.UpSongActivity;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;

        super.onDestroy();
    }

    /**
     * 任意一次unbindService()方法，都会触发这个方法
     * 用于释放一些绑定时使用的资源
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MusicControl extends Binder {

        public void play(String url) {
            mediaPlayer.start();

            seekProgress();

            playMusic(url);
        }

        public void pause() {
            mediaPlayer.pause();
        }

        public void continuePlay() {
            mediaPlayer.start();
        }

        public void seekPlayProgress(int progress) {
            mediaPlayer.seekTo(progress);
        }
    }

    //播放音乐
    private void playMusic(String url) {
        mediaPlayer.reset();

        //创建uri
        Uri uri = Uri.parse(url);

        //设置headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://www.bilibili.com/");

        try {
            //设置数据源
            mediaPlayer.setDataSource(getApplicationContext(), uri, headers);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seekProgress() {
        //计时器对象
        Timer timer = new Timer();

        //计时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //该线程用来获取当前播放进度
                int currentPosition = mediaPlayer.getCurrentPosition();

                //获取总长度
                int duration = mediaPlayer.getDuration();

                //利用message给主线程发消息更新seekBar进度
                Message message = Message.obtain();

                Bundle bundle = new Bundle();
                bundle.putInt("duration", duration);
                bundle.putInt("currentPosition", currentPosition);

                //设置发送的消息内容
                message.setData(bundle);

                //发送消息给主线程
                UpSongActivity.handler.sendMessage(message);
            }
        };

        timer.schedule(timerTask, 300, 500);

        //当播放结束时停止播放
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //取消计时任务
                timer.cancel();
                timerTask.cancel();
            }
        });
    }
}
