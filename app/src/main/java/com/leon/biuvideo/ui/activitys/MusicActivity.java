package com.leon.biuvideo.ui.activitys;

import androidx.annotation.NonNull;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userFragmentAdapters.OnMusicListListener;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.musicBeans.MusicInfo;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.service.MusicService;
import com.leon.biuvideo.ui.SimpleLoadDataThread;
import com.leon.biuvideo.ui.dialogs.LoadingDialog;
import com.leon.biuvideo.ui.dialogs.MusicPlayListDialog;
import com.leon.biuvideo.ui.dialogs.WarnDialog;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleDownloadThread;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.LocalOrdersDatabaseUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.values.LocalOrderType;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.MusicParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.MusicUrlParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 音乐/音频Activity
 */
public class MusicActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
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
    private List<String> sids;

    //当前sid在sids中的索引位置
    public int position;

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
    public static Handler musicPlayHandler;

    private final static LocalOrderType localOrderType = LocalOrderType.AUDIO;

    //music数据库的helper对象
    private LocalOrdersDatabaseUtils localOrdersDatabaseUtils;

    //播放列表弹窗
    private MusicPlayListDialog musicPlayListDialog;

    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;
    private MusicParser musicParser;
    private MusicUrlParser musicUrlParser;
    private LinearLayout music_linearLayout;
    private Handler initDataHandler;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        music_linearLayout = findViewById(R.id.music_linearLayout);
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

        loadingDialog = new LoadingDialog(MusicActivity.this);
        loadingDialog.show();

        loadData();
    }

    private void loadData() {
        SimpleLoadDataThread simpleLoadDataThread = new SimpleLoadDataThread() {
            @Override
            public void load() {
                //获取music信息
                Intent intent = getIntent();

                //获取sid的position
                position = intent.getIntExtra("position", -1);

                String[] sidsArray = intent.getStringArrayExtra("sids");

                // 获取所有的sid,转换为List集合
                if (sidsArray.length != 0) {
                    sids = new ArrayList<>();
                    sids.addAll(Arrays.asList(sidsArray));
                }

                Message message = initDataHandler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();

                if (position != -1) {
                    String sid = sids.get(position);

                    if (musicParser == null) {
                        musicParser = new MusicParser();
                    }

                    //获取music信息
                    musicInfo = musicParser.parseMusic(sid);

                    //获取music文件

                    if (musicUrlParser == null) {
                        musicUrlParser = new MusicUrlParser(getApplicationContext());
                    }

                    musicUrl = musicUrlParser.parseMusicUrl(sid);

                    bundle.putBoolean("loadState", true);
                } else {
                    Snackbar.make(music_circleImageView_cover, "数据获取失败", Snackbar.LENGTH_SHORT).show();
                    bundle.putBoolean("loadState", false);

                    finish();
                }

                //创建sqLiteHelper对象
                localOrdersDatabaseUtils = new LocalOrdersDatabaseUtils(getApplicationContext());

                message.setData(bundle);
                initDataHandler.sendMessage(message);
            }
        };

        SimpleThreadPool simpleThreadPool = simpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(simpleLoadDataThread), "loadMusicInfo");

        initDataHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");

                if (loadState) {
                    initValue();
                    connectionMusicService();
                }

                loadingDialog.dismiss();

                return true;
            }
        });
    }

    /**
     * 与MusicService建立连接
     */
    private void connectionMusicService() {
        musicConnection = new MusicConnection();
        musicIntent = new Intent(getApplicationContext(), MusicService.class);

        //开启服务
        //退出UpSongActivity后，Music在后台播放
//                startService(musicIntent);

        //绑定服务
        bindService(musicIntent, musicConnection, Context.BIND_AUTO_CREATE);

        //处理消息
        musicPlayHandler = new Handler(new Handler.Callback() {
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
        isHavePlayList = localOrdersDatabaseUtils.queryLocalOrder(musicInfo.sid, musicInfo.bvid, localOrderType);
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
                    Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    break;
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
                    Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    break;
                }

                //跳转至作者页面
                Intent userIntent = new Intent(this, UserActivity.class);
                userIntent.putExtra("mid", musicInfo.uid);
                startActivity(userIntent);

                break;
            case R.id.music_imageView_musicList:
                //获取播放列表数据
                List<LocalOrder> localOrderList = localOrdersDatabaseUtils.queryLocalOrder(localOrderType);

                musicPlayListDialog = new MusicPlayListDialog(MusicActivity.this, localOrderList);
                musicPlayListDialog.setOnMusicListListener(new OnMusicListListener() {
                    @Override
                    public void onRefreshFavoriteIcon(LocalOrder localOrder) {
                        boolean state = localOrdersDatabaseUtils.deleteLocalOrder(localOrder.mainId, localOrder.subId, localOrderType);
                        if (state) {
                            music_imageView_addFavorite.setImageResource(R.drawable.no_favorite);
                            isHavePlayList = false;
                        } else {
                            music_imageView_addFavorite.setImageResource(R.drawable.favorite);
                        }
                    }

                    @Override
                    public void onSwitchMusic(String sid) {
                        //判断是否有网络
                        if (!InternetUtils.checkNetwork(getApplicationContext())) {
                            Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        switchMusic(sid);

                        musicPlayListDialog.dismiss();
                    }
                });
                musicPlayListDialog.show();

                break;
            case R.id.music_imageView_download:
                //获取权限
                FileUtils.verifyPermissions(this);

                //判断是否有网络
                if (!InternetUtils.checkNetwork(getApplicationContext())) {
                    Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    break;
                }

                if (downloadRecordsDatabaseUtils == null) {
                    downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(getApplicationContext());
                }

                boolean downloadState = downloadRecordsDatabaseUtils.queryVideoDownloadState(musicInfo.sid);
                if (downloadState) {
                    WarnDialog warnDialog = new WarnDialog(MusicActivity.this, "提示", "检测到本地已存在该音频，是否要覆盖本地资源文件？");
                    warnDialog.setOnConfirmListener(new WarnDialog.OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            downloadAudio();
                            warnDialog.dismiss();
                        }

                        @Override
                        public void onCancel() {
                            warnDialog.dismiss();
                        }
                    });
                    warnDialog.show();
                } else {
                    downloadAudio();
                }

                break;
            case R.id.music_imageView_addFavorite:

                if (isHavePlayList) {
                    //从playList中移除
                    localOrdersDatabaseUtils.deleteLocalOrder(musicInfo.sid, musicInfo.bvid, localOrderType);

                    music_imageView_addFavorite.setImageResource(R.drawable.no_favorite);

                    isHavePlayList = false;

                    //从sids中删除
                    sids.remove(position);

                } else {
                    LocalOrder localOrder = new LocalOrder();
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", musicInfo.title);
                    map.put("author", musicInfo.authors.get(0));
                    localOrder.jsonObject = new JSONObject(map);
                    localOrder.mainId = musicInfo.sid;
                    localOrder.subId = musicInfo.bvid;
                    localOrder.orderType = localOrderType;
                    localOrder.addTime = System.currentTimeMillis();

                    //添加至播放列表
                    boolean addState = localOrdersDatabaseUtils.addLocalOrder(localOrder);

                    if (addState) {
                        music_imageView_addFavorite.setImageResource(R.drawable.favorite);
                        isHavePlayList = true;
                    }

                    Snackbar.make(v, addState ? "已添加至播放列表" : "添加失败~", Snackbar.LENGTH_SHORT).show();
                }

                break;
            case R.id.music_imageView_control:
                //判断是否有网络
                if (!InternetUtils.checkNetwork(getApplicationContext())) {
                    Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    break;
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
                if (!InternetUtils.checkNetwork(getApplicationContext())) {
                    Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    break;
                }

                //判断当前播放的歌曲是否处于第一个
                if (position != 0) {
                    //获取上一个music的sid
                    String sid = sids.get(--position);

                    //切换歌曲
                    switchMusic(sid);
                } else {
                    Snackbar.make(v, "当前播放的就是第一个了~", Snackbar.LENGTH_SHORT).show();
                }

                break;
            case R.id.music_imageView_next:
                //判断是否有网络
                if (!InternetUtils.checkNetwork(getApplicationContext())) {
                    Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    break;
                }

                //判断当前播放的歌曲是否处于最后一个
                if (position != sids.size() - 1) {
                    //获取下一个music的sid
                    String sid = sids.get(++position);

                    //切换歌曲
                    switchMusic(sid);
                } else {
                    Snackbar.make(v, "当前播放的就是最后一个了~", Snackbar.LENGTH_SHORT).show();
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
    private void switchMusic(String sid) {
        //暂停音乐
        musicControl.pause();

        //暂停当前动画
        rotation.pause();

        //设置当前歌曲状态为正在播放
        musicState = 1;
        music_imageView_control.setImageResource(R.drawable.music_icon_play);

        musicInfo = musicParser.parseMusic(sid);

        //切换当前歌曲
        musicUrl = musicUrlParser.parseMusicUrl(sid);

        //播放音乐
        musicControl.play(musicUrl);

        //刷新当前activity上的数据
        initValue();

        //开始动画
        rotation.start();
    }

    /**
     * 下载音频
     */
    private void downloadAudio() {
        DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();
        downloadedDetailMedia.fileName = musicInfo.title + "-" + musicInfo.uname;
        downloadedDetailMedia.cover = musicInfo.cover;
        downloadedDetailMedia.title = musicInfo.title + "-" + musicInfo.uname;
        downloadedDetailMedia.audioUrl = musicUrl;

        // 获取视频和音频总大小
        downloadedDetailMedia.size = ResourceUtils.getResourcesSize(musicUrl);
        downloadedDetailMedia.mainId = musicInfo.sid;
        downloadedDetailMedia.resourceMark = downloadedDetailMedia.mainId;
        downloadedDetailMedia.isVideo = false;

        // 添加至DownloadDetailsForMedia
        downloadRecordsDatabaseUtils.addMediaDetail(downloadedDetailMedia);

        Snackbar.make(music_linearLayout, R.string.isDownloading, Snackbar.LENGTH_SHORT).show();

        //保存歌曲线程
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(SimpleThreadPool.DownloadTaskNum, SimpleThreadPool.DownloadTask);
        SimpleDownloadThread simpleDownloadThread = new SimpleDownloadThread(getApplicationContext(), musicInfo.sid, musicUrl, musicInfo.title + "-" + musicInfo.uname);
        simpleThreadPool.submit(new FutureTask<>(simpleDownloadThread));
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

        if (localOrdersDatabaseUtils != null) {
            localOrdersDatabaseUtils.close();
        }

        if (downloadRecordsDatabaseUtils != null) {
            downloadRecordsDatabaseUtils.close();
        }

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
                Snackbar.make(music_linearLayout, "权限申请成功", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(music_linearLayout, "权限申请失败", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}