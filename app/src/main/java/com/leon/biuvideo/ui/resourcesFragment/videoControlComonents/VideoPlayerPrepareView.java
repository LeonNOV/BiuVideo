package com.leon.biuvideo.ui.resourcesFragment.videoControlComonents;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.controller.IControlComponent;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.leon.biuvideo.R;

/**
 * @Author Leon
 * @Time 2021/4/3
 * @Desc 视频播放器准备视图
 */
public class VideoPlayerPrepareView extends FrameLayout implements IControlComponent {
    private ControlWrapper controlWrapper;
    private ImageView videoPlayerPrepareControl;
    private LinearLayout videoPlayerPrepareNetWarning;

    public VideoPlayerPrepareView(@NonNull Context context) {
        super(context);
        initView();
    }

    public VideoPlayerPrepareView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoPlayerPrepareView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        this.controlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.video_player_prepare_view, this, true);
        videoPlayerPrepareControl = findViewById(R.id.video_player_prepare_control);
        videoPlayerPrepareNetWarning = findViewById(R.id.video_player_prepare_net_warning);
        findViewById(R.id.video_player_prepare_net_warning_continue).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayerPrepareNetWarning.setVisibility(GONE);

                // 设置在移动网络下播放
                VideoViewManager.instance().setPlayOnMobileNetwork(true);

                // 播放视频
                controlWrapper.start();
            }
        });


    }

    /**
     * 点击此界面开始播放视频
     */
    public void setClickStart () {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                controlWrapper.start();
            }
        });
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            // 准备中
            case VideoView.STATE_PREPARING:
                bringToFront();
                setVisibility(VISIBLE);
                videoPlayerPrepareControl.setVisibility(GONE);
                videoPlayerPrepareNetWarning.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
            case VideoView.STATE_PAUSED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_BUFFERING:
            case VideoView.STATE_BUFFERED:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);
                break;
            case VideoView.STATE_IDLE:
                setVisibility(VISIBLE);
                bringToFront();
                videoPlayerPrepareNetWarning.setVisibility(GONE);
                videoPlayerPrepareControl.setVisibility(VISIBLE);
                break;

            // 播放中止，在移动网络下
            case VideoView.STATE_START_ABORT:
                setVisibility(VISIBLE);
                videoPlayerPrepareNetWarning.setVisibility(VISIBLE);
                videoPlayerPrepareNetWarning.bringToFront();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {

    }
}
