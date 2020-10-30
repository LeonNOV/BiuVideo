package com.leon.biuvideo.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
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
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.MusicDatabaseUtils;
import com.leon.biuvideo.utils.SQLiteHelper;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.resourcesParseUtils.MusicParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.MusicUrlParseUtils;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpSongActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //返回、播放列表
    private ImageView music_imageView_back, music_imageView_musicList;

    //歌曲名称、作者
    private MarqueeView music_marqueeView;

    //视频（MV）icon
    private ImageView music_imageView_isHaveVideo;

    //music封面
    private CircleImageView music_circleImageView_cover;

    //喜欢按钮
    private ImageView music_imageView_addFavorite;

    //播放数、投币数、评论数、收藏数、分享数
    private TextView
            music_imageView_play,
            music_imageView_coin,
            music_imageView_comment;
    private ImageView music_imageView_link;

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

    //所有的sid
    private long[] sids;

    //当前sid在sids中的索引位置
    private int position;

    //music信息
    private MusicInfo musicInfo;

    //musicUrl链接
    private String musicUrl;

    //music状态：0：停止（初始化状态，不包括切换歌曲）、1：暂停、2：继续
    private int musicState = 0;

    //旋转动画
    private ObjectAnimator rotation;

    //存在于于播放列中的状态
    boolean isHavePlayList;

    //控制music
    private MusicService.MusicControl musicControl;

    private Intent musicIntent;

    //服务连接对象
    private MusicConnection musicConnection;

    //消息处理器
    public static Handler handler;

    //music数据库的helper对象
    private MusicDatabaseUtils musicDatabaseUtils;

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

    /**
     * 初始化
     */
    private void init() {
        //获取music信息
        Intent intent = getIntent();

        //获取sid的position
        position = intent.getIntExtra("position", -1);

        //获取所有的sid
        sids = intent.getLongArrayExtra("sids");

        if (position != -1) {
            long sid = sids[position];

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
        //退出UpSongActivity后，Music在后台播放
//        startService(musicIntent);

        //绑定服务
        bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE);

        //创建sqLiteHelper对象
        musicDatabaseUtils = new MusicDatabaseUtils(getApplicationContext());
    }

    /**
     * 初始化控件
     */
    private void initView() {
        music_imageView_back = findViewById(R.id.music_imageView_back);
        music_imageView_back.setOnClickListener(this);

        music_imageView_musicList = findViewById(R.id.music_imageView_musicList);
        music_imageView_musicList.setOnClickListener(this);

        music_marqueeView = findViewById(R.id.music_marqueeView);

        music_imageView_isHaveVideo = findViewById(R.id.music_imageView_isHaveVideo);
        music_imageView_isHaveVideo.setOnClickListener(this);

        music_circleImageView_cover = findViewById(R.id.music_circleImageView_cover);

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

        music_imageView_link = findViewById(R.id.music_imageView_link);
        music_imageView_link.setOnClickListener(this);

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

    /**
     * 初始化数据
     */
    private void initValue() {
        //设置封面
        Glide.with(getApplicationContext()).load(musicInfo.cover).into(music_circleImageView_cover);

        //设置歌曲名、作者
        String title = musicInfo.title + "-" + musicInfo.uname;
        music_marqueeView.startWithText(title);

        //判断是否有MV
        if (!musicInfo.bvid.equals("")) {
            music_imageView_isHaveVideo.setVisibility(View.VISIBLE);
        }

        //判断是否在播放列表中,更改addFavoriteIcon
        isHavePlayList = musicDatabaseUtils.queryMusic(musicInfo.sid);
        if (isHavePlayList) {
            music_imageView_addFavorite.setImageResource(R.drawable.favorite);
        } else {
            music_imageView_addFavorite.setImageResource(R.drawable.no_favorite);
        }

        //设置播放量
        music_imageView_play.setText(ValueFormat.generateCN(musicInfo.play));

        //设置投币数
        music_imageView_coin.setText(ValueFormat.generateCN(musicInfo.coinNum));

        //设置评论数
        music_imageView_comment.setText(ValueFormat.generateCN(musicInfo.comment));

        //初始化seekBar
        music_seekBar.setProgress(0);

        //初始化当前播放进度
        music_textView_nowProgress.setText("00:00");

        //设置music总长度
        music_textView_length.setText(ValueFormat.lengthGenerate(musicInfo.duration));

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

    /**
     * 点击事件
     *
     * @param v 控件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_imageView_back:
                finish();
                break;
            case R.id.music_imageView_isHaveVideo:
                //跳转到video界面
                Intent intent = new Intent(this, VideoActivity.class);
                intent.putExtra("bvid", musicInfo.bvid);
                startActivity(intent);

                break;
            case R.id.music_imageView_musicList:
                //获取播放列表数据
                List<MusicPlayList> musicPlayLists = musicDatabaseUtils.queryPlayList();
                Log.d(LogTip.blue, "onClick: " + musicPlayLists.size());

                MusicListDialog musicListDialog = new MusicListDialog(UpSongActivity.this, musicPlayLists, new MusicListDialog.PriorityListener() {

                    //刷新当前music_imageView_addFavorite的状态
                    @Override
                    public void refreshFavoriteIcon() {
                        //判断当前歌曲是否从playList中删除
                        boolean state = musicDatabaseUtils.queryMusic(musicInfo.sid);

                        if (state) {
                            music_imageView_addFavorite.setImageResource(R.drawable.favorite);
                        } else {
                            music_imageView_addFavorite.setImageResource(R.drawable.no_favorite);
                            isHavePlayList = false;
                        }
                    }

                    //切换当前歌曲
                    @Override
                    public void refreshMusic(long sid) {
                        switchMusic(sid);
                    }
                });
                musicListDialog.show();

                musicListDialog.setOnDialogClickListener(new MusicListDialog.OnDialogClickListener() {
                    @Override
                    public void onCloseDialog() {
                        musicListDialog.dismiss();
                    }
                });

                break;
            case R.id.music_imageView_download:
                String songPath = FileUtils.folderState(FileUtils.ResourcesFolder.SONGS);
                //保存歌曲线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean saveState = MediaUtils.saveMusic(musicUrl, songPath, musicInfo.title + "-" + musicInfo.uname + ".mp3");

                        //添加到下载任务界面中
                    }
                }).start();
                break;
            case R.id.music_imageView_addFavorite:

                if (isHavePlayList) {
                    //从playList中移除
                    musicDatabaseUtils.removeMusicItem(musicInfo.sid);

                    music_imageView_addFavorite.setImageResource(R.drawable.no_favorite);

                    isHavePlayList = false;
                } else {
                    MusicPlayList musicPlayList = new MusicPlayList();
                    musicPlayList.sid = musicInfo.sid;
                    musicPlayList.musicName = musicInfo.title;
                    musicPlayList.author = musicInfo.uname;

                    //添加至播放列表
                    musicDatabaseUtils.addPlayList(musicPlayList);

                    music_imageView_addFavorite.setImageResource(R.drawable.favorite);

                    isHavePlayList = true;
                }

                break;
            case R.id.music_imageView_control:

                switch (musicState) {
                    case 0:
                        //动画开始
                        rotation.start();
                        musicState = 1;
                        music_imageView_control.setImageResource(R.drawable.music_icon_play);

                        //设置播放地址并播放音乐
                        musicControl.play(musicUrl);

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
            case R.id.music_imageView_up:
                //判断当前播放的歌曲是否处于第一个
                if (position != 0) {
                    //获取上一个music的sid
//                    position--;
                    long sid = sids[--position];

                    Log.d(LogTip.blue, "上一首：" + position);

                    //切换歌曲
                    switchMusic(sid);
                } else {
                    Toast.makeText(this, "当前播放的就是第一个了~~~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.music_imageView_next:
                //判断当前播放的歌曲是否处于最后一个
                if (position != sids.length - 1) {
                    //获取下一个music的sid
//                    position++;
                    long sid = sids[++position];

                    Log.d(LogTip.blue, "下一首：" + position);

                    //切换歌曲
                    switchMusic(sid);
                } else {
                    Toast.makeText(this, "当前播放的就是最后一个了~~~", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.music_imageView_link:

                //跳转到源网站收听
                Intent intentOriginUrl = new Intent();
                intentOriginUrl.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse("https://www.bilibili.com/audio/au" + musicInfo.sid);
                intentOriginUrl.setData(uri);
                startActivity(intentOriginUrl);

                break;
            default:
                break;
        }
    }

    /**
     * 切换歌曲
     *
     * @param sid   下一首歌曲的sid
     */
    private void switchMusic(long sid) {
        //重置当前动画
        rotation.cancel();

        //设置当前歌曲状态为正在播放
        musicState = 1;
        music_imageView_control.setImageResource(R.drawable.music_icon_play);

        musicInfo = MusicParseUtils.parseMusic(sid);

        //切换当前歌曲
        musicUrl = MusicUrlParseUtils.parseMusicUrl(sid);

        //缓一下
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        musicControl.play(musicUrl);

        //刷新当前activity上的数据
        initValue();

        //开始动画
        rotation.start();
    }

    /**
     * seekBar变化监听
     *
     * @param seekBar  seekBar对象
     * @param progress 进度
     * @param fromUser
     */
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

    /**
     * 离开seekBar时的监听
     *
     * @param seekBar seekBar对象
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();

        musicControl.seekPlayProgress(progress);
    }

    /**
     * 开启服务
     */
    @Override
    public void onStart() {
        super.onStart();
        music_marqueeView.startFlipping();
    }

    /**
     * 停止服务
     */
    @Override
    public void onStop() {
        super.onStop();
        music_marqueeView.stopFlipping();
    }

    /**
     * Activity的销毁
     */
    @Override
    protected void onDestroy() {
        unbind();
        super.onDestroy();
    }

    /**
     * 解绑服务
     */
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