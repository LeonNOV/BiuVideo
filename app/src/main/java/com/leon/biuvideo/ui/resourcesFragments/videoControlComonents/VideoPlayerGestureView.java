package com.leon.biuvideo.ui.resourcesFragments.videoControlComonents;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.controller.IGestureComponent;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;
import com.leon.biuvideo.R;

/**
 * @Author Leon
 * @Time 2021/4/3
 * @Desc 视频播放器手势控制视图
 */
public class VideoPlayerGestureView extends LinearLayout implements IGestureComponent {
    private ControlWrapper controlWrapper;

    private ImageView videoPlayerGestureIcon;
    private TextView videoPlayerGesturePercent;
    private ProgressBar videoPlayerGestureProgress;

    public VideoPlayerGestureView(Context context) {
        super(context);
        initView();
    }

    public VideoPlayerGestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerGestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        this.setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.video_player_gesture_view, this, true);

        videoPlayerGestureIcon = findViewById(R.id.video_player_gesture_icon);
        videoPlayerGesturePercent = findViewById(R.id.video_player_gesture_percent);
        videoPlayerGestureProgress = findViewById(R.id.video_player_gesture_progress);
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        this.controlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
            case VideoView.STATE_START_ABORT:
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_PLAYBACK_COMPLETED :
                this.setVisibility(GONE);
                break;
            default:
                this.setVisibility(VISIBLE);
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

    @Override
    public void onStartSlide() {
        // 显示控制窗口
        controlWrapper.hide();

        this.setVisibility(VISIBLE);
        this.setAlpha(1f);
    }

    @Override
    public void onStopSlide() {
        this
                .animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        VideoPlayerGestureView.this.setVisibility(GONE);
                    }
                })
                .start();
    }

    @Override
    public void onPositionChange(int slidePosition, int currentPosition, int duration) {
        videoPlayerGestureProgress.setVisibility(GONE);

        if (slidePosition > currentPosition) {
            videoPlayerGestureIcon.setImageResource(R.drawable.video_fast_forward);
        } else {
            videoPlayerGestureIcon.setImageResource(R.drawable.video_fast_retreat);
        }

        videoPlayerGesturePercent.setText(String.format("%s/%s", PlayerUtils.stringForTime(slidePosition), PlayerUtils.stringForTime(duration)));
    }

    @Override
    public void onBrightnessChange(int percent) {
        videoPlayerGestureProgress.setVisibility(VISIBLE);
        videoPlayerGestureIcon.setImageResource(R.drawable.video_brightness);
        videoPlayerGesturePercent.setText(percent + "%");
        videoPlayerGestureProgress.setProgress(percent);
    }

    @Override
    public void onVolumeChange(int percent) {
        videoPlayerGestureProgress.setVisibility(VISIBLE);

        if (percent <= 0) {
            videoPlayerGestureIcon.setImageResource(R.drawable.video_volume_off);
        } else {
            videoPlayerGestureIcon.setImageResource(R.drawable.video_volume);
        }

        videoPlayerGesturePercent.setText(percent + "%");
        videoPlayerGestureProgress.setProgress(percent);
    }
}
