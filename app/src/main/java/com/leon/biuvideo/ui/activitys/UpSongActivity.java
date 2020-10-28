package com.leon.biuvideo.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.musicBeans.MusicInfo;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;
import com.leon.biuvideo.service.MusicService;
import com.leon.biuvideo.ui.dialogs.MusicListDialog;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.resourcesParseUtils.MusicParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.MusicUrlParseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpSongActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    //顶部控件
    private Toolbar music_toolBar;

    //返回、播放列表
    private ImageView music_imageView_back, music_imageView_musicList;

    //music封面
    private CircleImageView music_circleImageView_cover;

    //lyrics
    private TextView music_lyrics;

    //喜欢按钮
    private ImageView music_imageView_addFavorite;

    //播放数、投币数、评论数、收藏数、分享数
    private TextView
            music_imageView_play,
            music_imageView_coin,
            music_imageView_comment,
            music_imageView_favorite;

    //缓存歌曲
    private ImageView music_imageView_download;

    //当前播放进度、总长度
    private TextView music_textView_nowProgress, music_textView_length;

    //music进度条
    private SeekBar music_seekBar;

    //上一曲、music总控、下一曲
    private ImageView
            music_imageView_up,
            music_imageView_control,
            music_imageView_next;

    //music信息
    private MusicInfo musicInfo;

    private Map<String, Object> musicUrl;

    //music状态：0：停止、1：暂停、2：继续
    private int musicState = 0;

    //旋转动画
    private ObjectAnimator rotation;

    //歌词显示状态
    private int lyricsState = 0;

    //控制music
    private MusicService.MusicControl musicControl;

    private Intent musicIntent;

    //服务连接对象
    private MusicConnection musicConnection;

    //消息处理器
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_song);

        init();
        initView();
        initValue();
    }

    private void init() {
        //获取music信息
        Intent intent = getIntent();
        long sid = intent.getLongExtra("sid", -1);

        if (sid != -1) {
            //获取music信息
            musicInfo = MusicParseUtils.parseMusic(sid);

            //获取music文件
            musicUrl = MusicUrlParseUtils.parseMusicUrl(sid);
        } else {
            Toast.makeText(this, "获取数据失败~~~", Toast.LENGTH_SHORT).show();
            finish();
        }

        musicConnection = new MusicConnection();
        musicIntent = new Intent(this, MusicService.class);

        //开启服务
//        startService(musicIntent);

        //绑定服务
        bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE);

        //处理消息
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();

                //获取总长度
                int duration = bundle.getInt("duration");

                //获取当前进度
                int currentPosition = bundle.getInt("currentPosition");

                //设置进度条最大值
                music_seekBar.setMax(duration);

                //设置进度条当前进度
                music_seekBar.setProgress(currentPosition);

                int minute = currentPosition / 1000 / 60;
                int second = currentPosition / 1000 % 60;

                String minuteStr = minute < 10 ? "0" + minute : minute + "";
                String secondStr = second < 10 ? "0" + second : second + "";

                //设置时间进度
                music_textView_nowProgress.setText(minuteStr + ":" + secondStr);

                return true;
            }
        });
    }

    private void initView() {
        music_toolBar = findViewById(R.id.music_toolBar);
        music_imageView_back = findViewById(R.id.music_imageView_back);
        music_imageView_back.setOnClickListener(this);

        music_imageView_musicList = findViewById(R.id.music_imageView_musicList);
        music_imageView_musicList.setOnClickListener(this);

        music_circleImageView_cover = findViewById(R.id.music_circleImageView_cover);
        music_circleImageView_cover.setOnClickListener(this);

        music_lyrics = findViewById(R.id.music_lyrics);

        //设置旋转动画
        rotation = ObjectAnimator.ofFloat(music_circleImageView_cover, "rotation", 0.0f, 360.0f);
        rotation.setDuration(45000);//一圈的时间
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(-1);//次数，-1为无限制
        rotation.setRepeatMode(ObjectAnimator.RESTART);//动画始终重复

        music_imageView_addFavorite = findViewById(R.id.music_imageView_addFavorite);
        music_imageView_addFavorite.setOnClickListener(this);

        music_imageView_play = findViewById(R.id.music_imageView_play);
        music_imageView_coin = findViewById(R.id.music_imageView_coin);
        music_imageView_comment = findViewById(R.id.music_imageView_comment);
        music_imageView_favorite = findViewById(R.id.music_imageView_favorite);

        music_imageView_download = findViewById(R.id.music_imageView_download);
        music_imageView_download.setOnClickListener(this);

        music_textView_nowProgress = findViewById(R.id.music_textView_nowProgress);
        music_textView_length = findViewById(R.id.music_textView_length);

        music_seekBar = findViewById(R.id.music_seekBar);
        music_seekBar.setOnSeekBarChangeListener(this);

        music_imageView_up = findViewById(R.id.music_imageView_up);
        music_imageView_up.setOnClickListener(this);

        music_imageView_control = findViewById(R.id.music_imageView_control);
        music_imageView_control.setOnClickListener(this);

        music_imageView_next = findViewById(R.id.music_imageView_next);
        music_imageView_next.setOnClickListener(this);
    }

    private void initValue() {
        //设置封面
        Glide.with(getApplicationContext()).load(musicInfo.cover).into(music_circleImageView_cover);

        //判断是否在播放列表中

        //设置播放量
        music_imageView_play.setText(ValueFormat.generateCN(musicInfo.play));

        //设置投币数
        music_imageView_coin.setText(ValueFormat.generateCN(musicInfo.coinNum));

        //设置评论数
        music_imageView_comment.setText(ValueFormat.generateCN(musicInfo.comment));

        //设置收藏数
        music_imageView_favorite.setText(ValueFormat.generateCN(musicInfo.collect));

        //初始化当前播放进度
        music_textView_nowProgress.setText("00:00");

        //设置music总长度
        music_textView_length.setText(ValueFormat.lengthGenerate(musicInfo.duration));
//        music_seekBar.setMax(musicInfo.duration);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_imageView_back:
                finish();
                break;
            case R.id.music_imageView_musicList:
                Toast.makeText(this, "点击了播放列表", Toast.LENGTH_SHORT).show();

                //获取播放列表数据
                List<MusicPlayList> musicPlayLists = new ArrayList<>();

                for (int i = 1; i <= 10; i++) {
                    MusicPlayList musicPlayList = new MusicPlayList();
                    musicPlayList.musicName = "歌曲名称" + i;
                    musicPlayList.musicAuthor = "作者" + i;

                    musicPlayLists.add(musicPlayList);
                }

                MusicListDialog musicListDialog = new MusicListDialog(UpSongActivity.this, musicPlayLists);
                musicListDialog.show();

                musicListDialog.setOnDialogClickListener(new MusicListDialog.OnDialogClickListener() {
                    @Override
                    public void onCloseDialog() {
                        musicListDialog.dismiss();
                    }
                });

                break;
            case R.id.item_imageView_cover:
                Toast.makeText(this, "点击了music封面", Toast.LENGTH_SHORT).show();

                if (lyricsState == 0) {
                    music_circleImageView_cover.setVisibility(View.INVISIBLE);
                    music_lyrics.setVisibility(View.VISIBLE);
                    lyricsState = 1;
                } else {
                    music_circleImageView_cover.setVisibility(View.VISIBLE);
                    music_lyrics.setVisibility(View.INVISIBLE);
                    lyricsState = 0;
                }

                break;
            case R.id.music_imageView_download:
                String songPath = FileUtils.folderState(FileUtils.ResourcesFolder.SONGS);

                //获取url
                List<String> musicUrls = (List<String>) musicUrl.get("urls");

                //保存歌曲
                saveResurces(musicUrls.get(0), songPath, musicInfo.title + "-" + musicInfo.uname + ".mp3");

//                Toast.makeText(this, saveState ? "歌曲缓存成功" : "歌曲缓存失败", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music_imageView_addFavorite:
                Toast.makeText(this, "点击了\"添加至播放列表\"", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music_imageView_up:
                Toast.makeText(this, "点击了\"上一曲\"", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music_imageView_control:

                switch (musicState) {
                    case 0:
                        //动画开始
                        rotation.start();
                        musicState = 1;
                        music_imageView_control.setImageResource(R.drawable.music_icon_play);

                        List<String> urls = (List<String>) musicUrl.get("urls");

                        //设置播放地址并播放音乐
                        musicControl.play(urls.get(0));

                        break;
                    case 1:
                        //动画暂停
                        rotation.pause();
                        musicState = 2;
                        music_imageView_control.setImageResource(R.drawable.music_icon_pause);

                        //暂停音乐
                        musicControl.pause();

                        break;
                    case 2:
                        //动画继续
                        rotation.resume();
                        musicState = 1;
                        music_imageView_control.setImageResource(R.drawable.music_icon_play);

                        //继续播放音乐
                        musicControl.continuePlay();

                        break;
                    default:
                        break;
                }

                break;
            case R.id.music_imageView_next:
                Toast.makeText(this, "点击了\"下一曲\"", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    private void saveResurces(String path, String songPath, String fileName) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean saveState = MediaUtils.saveMusic(path, songPath, fileName);

                Toast.makeText(UpSongActivity.this, saveState ? "歌曲缓存成功" : "歌曲缓存失败", Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress == seekBar.getMax()) {
            music_imageView_control.setImageResource(R.drawable.music_icon_pause);
            rotation.pause();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();

        musicControl.seekPlayProgress(progress);
    }

    @Override
    protected void onDestroy() {
        unbind();
        super.onDestroy();
    }

    //解绑服务
    public void unbind() {
        musicControl.pause();

        //解绑服务
        unbindService(musicConnection);

        //停止服务
        stopService(musicIntent);
    }

    //服务连接类
    class MusicConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}