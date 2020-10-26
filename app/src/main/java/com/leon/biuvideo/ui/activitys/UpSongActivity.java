package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
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
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.MusicParseUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpSongActivity extends AppCompatActivity implements View.OnClickListener {
    //顶部控件
    private Toolbar music_toolBar;

    //返回、播放列表
    private ImageView music_imageView_back, music_imageView_musicList;

    //music封面
    private CircleImageView music_circleImageView_cover;

    //喜欢按钮
    private ImageView music_imageView_addFavorite;

    //播放数、投币数、评论数、收藏数、分享数
    private TextView
            music_imageView_play,
            music_imageView_coin,
            music_imageView_comment,
            music_imageView_favorite,
            music_imageView_share;

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
    
    //music状态：0：停止、1：暂停、2：继续
    private int musicState = 0;
    
    //旋转动画
    private ObjectAnimator rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
            musicInfo = MusicParseUtils.parseMusic(sid);
        } else {
            Toast.makeText(this, "获取数据失败~~~", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {
        music_toolBar = findViewById(R.id.music_toolBar);
        music_imageView_back = findViewById(R.id.music_imageView_back);
        music_imageView_back.setOnClickListener(this);

        music_imageView_musicList = findViewById(R.id.music_imageView_musicList);
        music_imageView_musicList.setOnClickListener(this);

        music_circleImageView_cover = findViewById(R.id.music_circleImageView_cover);
        music_circleImageView_cover.setOnClickListener(this);

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
        music_imageView_share = findViewById(R.id.music_imageView_share);

        music_textView_nowProgress = findViewById(R.id.music_textView_nowProgress);
        music_textView_length = findViewById(R.id.music_textView_length);

        music_seekBar = findViewById(R.id.music_seekBar);

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
        music_imageView_play.setText(ValueUtils.generateCN(musicInfo.play));

        //设置投币数
        music_imageView_coin.setText(ValueUtils.generateCN(musicInfo.coinNum));

        //设置评论数
        music_imageView_comment.setText(ValueUtils.generateCN(musicInfo.comment));

        //设置收藏数
        music_imageView_favorite.setText(ValueUtils.generateCN(musicInfo.collect));

        //设置分享数
        music_imageView_share.setText(ValueUtils.generateCN(musicInfo.share));

        //初始化当前播放进度
        music_textView_nowProgress.setText("00:00");

        //设置music总长度
        int minute = musicInfo.duration / 60;
        int second = musicInfo.duration % 60;
        String minuteStr = (minute >= 10 ? "" : "0" + minute) + ":" + (second >= 10 ? "" : "0" + second);
        String secondStr = second >= 10 ? "" : "0" + second;
        music_textView_length.setText("06:23");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_imageView_back:
                finish();
                break;
            case R.id.music_imageView_musicList:
                Toast.makeText(this, "点击了播放列表", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_imageView_cover:
                Toast.makeText(this, "点击了music封面", Toast.LENGTH_SHORT).show();
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
                        break;
                    case 1:
                        //动画暂停
                        rotation.pause();
                        musicState = 2;
                        music_imageView_control.setImageResource(R.drawable.music_icon_pause);
                        break;
                    case 2:
                        //动画继续
                        rotation.resume();
                        musicState = 1;
                        music_imageView_control.setImageResource(R.drawable.music_icon_play);
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
}