package com.leon.biuvideo.ui.activitys;

import androidx.annotation.NonNull;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.dataBaseUtils.MusicListDatabaseUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.dataBaseUtils.Tables;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.MusicParseUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.MusicUrlParseUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 音乐/音频Activity
 */
public class UpSongActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    //歌曲名称、作者
    private TextView music_textView_musicName, music_textView_author;

    //视频（MV）icon
    private ImageView music_imageView_isHaveVideo;

    //music封面
    private CircleImageView music_circleImageView_cover;

    //喜欢按钮
    private ImageView music_imageView_addFavorite;

    //当前播放进度、总长度
    private TextView music_textView_nowProgress, music_textView_length;

    //music进度条
    private SeekBar music_seekBar;

    //暂停/播放icon
    public static ImageView music_imageView_control;

    //所有的sid
    private List<Long> sids;

    //当前sid在sids中的索引位置
    public static int position;

    //music信息
    private MusicInfo musicInfo;

    //musicUrl链接
    private String musicUrl;

    //music状态：0：停止(初始化状态)、1：正在播放、2：暂停状态
    public static int musicState = 0;

    //封面旋转动画
    public static ObjectAnimator rotation;

    //存在于播放列表中的状态
    private boolean isHavePlayList;

    //控制music
    private MusicService.MusicControl musicControl;

    private Intent musicIntent;

    //服务连接对象
    private MusicConnection musicConnection;

    //消息处理器
    public static Handler handler;

    //music数据库的helper对象
    private MusicListDatabaseUtils musicDatabaseUtils;

    //播放列表弹窗
    private MusicListDialog musicListDialog;

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

        long[] sidsArray = intent.getLongArrayExtra("sids");
        // 获取所有的sid,转换为List集合
        if (sidsArray.length != 0) {
            this.sids = new ArrayList<>();

            for (long l : sidsArray) {
                this.sids.add(l);
            }
        }

        if (position != -1) {
            long sid = sids.get(position);

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

                String length = (minute < 10 ? "0" + minute : minute + "") + ":" + (second < 10 ? "0" + second : second + "");

                //设置时间进度
                music_textView_nowProgress.setText(length);

                return true;
            }
        });

        //创建sqLiteHelper对象
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.MusicPlayList);
        musicDatabaseUtils = (MusicListDatabaseUtils) sqLiteHelperFactory.getInstance();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //返回、播放列表
        ImageView music_imageView_back = findViewById(R.id.music_imageView_back);
        music_imageView_back.setOnClickListener(this);

        ImageView music_imageView_musicList = findViewById(R.id.music_imageView_musicList);
        music_imageView_musicList.setOnClickListener(this);

        music_textView_musicName = findViewById(R.id.music_textView_musicName);

        music_textView_author = findViewById(R.id.music_textView_author);
        music_textView_author.setOnClickListener(this);

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

        //音乐原链接
        ImageView music_imageView_link = findViewById(R.id.music_imageView_link);
        music_imageView_link.setOnClickListener(this);

        //缓存歌曲
        ImageView music_imageView_download = findViewById(R.id.music_imageView_download);
        music_imageView_download.setOnClickListener(this);

        music_textView_nowProgress = findViewById(R.id.music_textView_nowProgress);
        music_textView_length = findViewById(R.id.music_textView_length);

        music_seekBar = findViewById(R.id.music_seekBar);
        music_seekBar.setOnSeekBarChangeListener(this);

        //上一曲、music总控、下一曲
        ImageView music_imageView_up = findViewById(R.id.music_imageView_up);
        music_imageView_up.setOnClickListener(this);

        music_imageView_control = findViewById(R.id.music_imageView_control);
        music_imageView_control.setOnClickListener(this);

        ImageView music_imageView_next = findViewById(R.id.music_imageView_next);
        music_imageView_next.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initValue() {
        //设置封面
        Glide.with(getApplicationContext()).load(musicInfo.cover).into(music_circleImageView_cover);

        //设置歌曲名、作者
        music_textView_musicName.setText(musicInfo.title.trim());
        music_textView_author.setText(musicInfo.uname.trim());

        //判断是否有MV
        music_imageView_isHaveVideo.setEnabled(!musicInfo.bvid.equals(""));

        //判断是否在播放列表中,更改addFavoriteIcon
        isHavePlayList = musicDatabaseUtils.queryMusic(musicInfo.sid);
        if (isHavePlayList) {
            music_imageView_addFavorite.setImageResource(R.drawable.favorite);
        } else {
            music_imageView_addFavorite.setImageResource(R.drawable.no_favorite);
        }

        //初始化当前播放进度
        music_textView_nowProgress.setText("00:00");

        //设置music总长度
        music_textView_length.setText(ValueFormat.lengthGenerate(musicInfo.duration));

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
                //判断是否有网络
                boolean isHaveNetworkVideo = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetworkVideo) {
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    return;
                }

                //跳转到video界面
                Intent videoIntent = new Intent(this, VideoActivity.class);
                videoIntent.putExtra("bvid", musicInfo.bvid);
                startActivity(videoIntent);

                break;
            case R.id.music_textView_author:
                //判断是否有网络
                boolean isHaveNetworkAuthor = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetworkAuthor) {
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    return;
                }

                //跳转至作者页面
                Intent userIntent = new Intent(this, UpMasterActivity.class);
                userIntent.putExtra("mid", musicInfo.uid);

                //判断上一Activity是否为UpMasterActivity
                //如果是，则销毁当前Activity

                startActivity(userIntent);

                break;
            case R.id.music_imageView_musicList:
                //获取播放列表数据
                List<MusicPlayList> musicPlayLists = musicDatabaseUtils.queryPlayList();
                Log.d(Fuck.blue, "onClick: " + musicPlayLists.size());

                musicListDialog = new MusicListDialog(UpSongActivity.this, musicPlayLists);
                MusicListDialog.priorityListener = new MusicListDialog.PriorityListener() {
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
                    @Override
                    public void refreshMusic(long sid) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(getApplicationContext());

                        if (!isHaveNetwork) {
                            Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        switchMusic(sid);

                        musicListDialog.dismiss();
                    }
                };
                musicListDialog.show();

                break;
            case R.id.music_imageView_download:
                //获取权限
                FileUtils.verifyPermissions(this);

                Toast.makeText(this, "已添加至缓存队列", Toast.LENGTH_SHORT).show();

                //保存歌曲线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean saveState = MediaUtils.saveMusic(getApplicationContext(), musicUrl, musicInfo.title + "-" + musicInfo.uname);

                        Looper.prepare();
                        Toast.makeText(UpSongActivity.this, saveState ? "缓存成功" : "缓存失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
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

                    //从sids中删除
                    sids.remove(position);

                } else {
                    MusicPlayList musicPlayList = new MusicPlayList();
                    musicPlayList.sid = musicInfo.sid;
                    musicPlayList.bvid = musicInfo.bvid.equals("") ? "" : musicInfo.bvid;
                    musicPlayList.author = musicInfo.uname;
                    musicPlayList.musicName = musicInfo.title;
                    musicPlayList.isHaveVideo = !musicInfo.bvid.equals("");

                    //添加至播放列表
                    boolean addState = musicDatabaseUtils.addPlayList(musicPlayList);

                    if (addState) {
                        music_imageView_addFavorite.setImageResource(R.drawable.favorite);
                        isHavePlayList = true;

                        Toast.makeText(this, addState ? "已添加至播放列表" : "添加失败~~~", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.music_imageView_control:
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetwork) {
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    return;
                }

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
                //判断是否有网络
                boolean isHaveNetworkUp = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetworkUp) {
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断当前播放的歌曲是否处于第一个
                if (position != 0) {
                    //获取上一个music的sid
                    long sid = sids.get(--position);

                    Log.d(Fuck.blue, "上一首：" + position);

                    //切换歌曲
                    switchMusic(sid);
                } else {
                    Toast.makeText(this, "当前播放的就是第一个了~~~", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.music_imageView_next:
                //判断是否有网络
                boolean isHaveNetworkNext = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetworkNext) {
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断当前播放的歌曲是否处于最后一个
                if (position != sids.size() - 1) {
                    //获取下一个music的sid
                    long sid = sids.get(++position);

                    Log.d(Fuck.blue, "下一首：" + position);

                    //切换歌曲
                    switchMusic(sid);
                } else {
                    Toast.makeText(this, "当前播放的就是最后一个了~~~", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.music_imageView_link:

                //跳转到源网站
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
        //暂停音乐
        musicControl.pause();

        //暂停当前动画
        rotation.pause();

        //设置当前歌曲状态为正在播放
        musicState = 1;
        music_imageView_control.setImageResource(R.drawable.music_icon_play);

        musicInfo = MusicParseUtils.parseMusic(sid);

        //切换当前歌曲
        musicUrl = MusicUrlParseUtils.parseMusicUrl(sid);

        //播放音乐
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
     * @param fromUser  是否由用户自己控制的
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
    }

    /**
     * 停止服务
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * Activity的销毁
     */
    @Override
    protected void onDestroy() {
        unbind();
        musicState = 0;
        Log.d(Fuck.blue, "onDestroy: UpSongActivity is destroy");

        musicDatabaseUtils.close();
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

    /**
     * 权限回调
     *
     * @param requestCode   请求码
     * @param permissions   文件读写权限
     * @param grantResults  授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1024) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "权限申请成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}