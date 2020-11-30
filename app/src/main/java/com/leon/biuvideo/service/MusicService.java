package com.leon.biuvideo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.activitys.MusicActivity;
import com.leon.biuvideo.utils.Fuck;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐后台播放服务
 * 该服务暂属于Bound状态，即该服务的生命周期和UpSongActivity一样
 * 如果需要该服务的生命周期不和UpSongActivity一样的话则只需要在
 * UpSongActivity中使用onStart()，和在该服务中使用onStartCommand()即可
 * 以上两个方法都已完成，只需取消对应的注释
 */
public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private Timer timer;

    //播放停止时的数值与长度之间的差值
    private final int differenceRange = 100;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
    }

    //退出UpSongActivity后，Music在后台播放
    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle data = msg.getData();

                Long currentSid = data.getLong("sid");

                if (beforeSid != currentSid) {
                    Log.d(LogTip.blue, "handleMessage: " + "其他歌曲");
                } else {
                    Log.d(LogTip.blue, "handleMessage: " + "同一首歌");
                }

                return true;
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d(Fuck.blue, "onBind: " + "服务已绑定");

        return new MusicControl();
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

    private void addTimer() {

        if (timer == null) {
            //计时器对象
            timer = new Timer();

            //计时任务
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    //该线程用来获取当前播放进度
                    int currentPosition = mediaPlayer.getCurrentPosition();

                    //获取总长度
                    int duration = mediaPlayer.getDuration();

                    //利用message给主线程发消息更新seekBar进度
                    Message message = MusicActivity.handler.obtainMessage();

                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);

                    //设置发送的消息内容
                    message.setData(bundle);

                    //发送消息给主线程
                    MusicActivity.handler.sendMessage(message);
                }
            };

            //开启计时任务，第一次开启五毫秒后执行，以后是每500毫秒执行一次
            timer.schedule(timerTask, 5, 500);
        }
    }

    public class MusicControl extends Binder {

        public void play(String url) {
            mediaPlayer.reset();

            playMusic(url);

            addTimer();
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
        if (url.startsWith("https")) {
            url = "http" + url.substring(5);
        }

        //创建uri
        Uri uri = Uri.parse(url);

        //设置headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://www.bilibili.com/");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36 Edg/86.0.622.51");

        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            if (mediaPlayer.isPlaying()) {
                onDestroy();

                mediaPlayer = new MediaPlayer();
            }

            //设置数据源
            mediaPlayer.setDataSource(getApplicationContext(), uri, headers);

            //准备数据监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            //播放完毕监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    int duration = mp.getDuration();
                    int currentPosition = mp.getCurrentPosition();

                    if (duration != 0 && currentPosition != 0) {
                        //确定范围
                        if (mp.getDuration() - mp.getCurrentPosition() <= differenceRange) {
                            MusicActivity.music_imageView_control.setImageResource(R.drawable.music_icon_pause);
                            MusicActivity.rotation.pause();
                            MusicActivity.musicState = 2;
                        }
                    }
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.seekTo(0);
                            Toast.makeText(MusicService.this, "重新播放中", Toast.LENGTH_SHORT).show();
                        }
                    return false;
                }
            });

            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;

        //取消计时任务
        if (timer != null) {
            timer.cancel();
        }

        Log.d(Fuck.blue, "onDestroy: " + "解绑服务");

        super.onDestroy();
    }
}
