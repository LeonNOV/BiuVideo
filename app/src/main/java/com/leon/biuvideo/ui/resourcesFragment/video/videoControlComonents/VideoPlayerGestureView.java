package com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
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
public class VideoPlayerGestureView extends FrameLayout implements IGestureComponent {
    private ControlWrapper controlWrapper;
    private OnDraggingListener onDraggingListener;

    private ImageView videoPlayerGestureIcon;
    private TextView videoPlayerGesturePercent;
    private ProgressBar videoPlayerGestureProgress;
    private LinearLayout videoPlayerGestureContent;

    /**
     * 是否为第一次出现VideoView.STATE_PLAYING
     */
    private boolean isFirstPlayingState = true;

    private OnDanmakuListener onDanmakuListener;

    public void setOnDanmakuListener(OnDanmakuListener onDanmakuListener) {
        this.onDanmakuListener = onDanmakuListener;
    }

    public VideoPlayerGestureView(Context context) {
        super(context);
    }

    public VideoPlayerGestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerGestureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnDraggingListener {
        /**
         * 手势快进/快退
         *
         * @param duration  总进度
         * @param slidePosition 快进进度
         */
        void onDragging(int duration, int slidePosition);
    }

    public void setOnDraggingListener(OnDraggingListener onDraggingListener) {
        this.onDraggingListener = onDraggingListener;
    }

    {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.video_player_gesture_view, this, true);

        videoPlayerGestureContent = findViewById(R.id.video_player_gesture_content);
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

        /*if (playState == VideoView.STATE_IDLE
                || playState == VideoView.STATE_START_ABORT
                || playState == VideoView.STATE_PREPARING
                || playState == VideoView.STATE_PREPARED
                || playState == VideoView.STATE_ERROR
                || playState == VideoView.STATE_PLAYBACK_COMPLETED) {
            setVisibility(GONE);
        } else {
            // 解决第一次打开视频播放界面，会显示手势控制视图的问题
            // 注意：第一次使用手势控制不会显示，后面再使用才能显示
            if (playState == VideoView.STATE_PLAYING && isFirstPlayingState) {
                setVisibility(GONE);
                isFirstPlayingState = false;
            } else {
                setVisibility(VISIBLE);
            }
        }*/
        if (playState == VideoView.STATE_IDLE
                || playState == VideoView.STATE_START_ABORT
                || playState == VideoView.STATE_PREPARING
                || playState == VideoView.STATE_PREPARED
                || playState == VideoView.STATE_ERROR
                || playState == VideoView.STATE_PLAYBACK_COMPLETED) {
            setVisibility(GONE);
        } else {
            if (playState == VideoView.STATE_PLAYING && isFirstPlayingState) {
                setVisibility(GONE);
                isFirstPlayingState = false;
            } else {
                setVisibility(VISIBLE);
            }
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

    /**
     * 开始滑动
     */
    @Override
    public void onStartSlide() {
        // 显示控制窗口
        controlWrapper.hide();

        videoPlayerGestureContent.setVisibility(VISIBLE);
        videoPlayerGestureContent.setAlpha(1f);
    }

    /**
     * 停止滑动
     */
    @Override
    public void onStopSlide() {
        videoPlayerGestureContent
                .animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        videoPlayerGestureContent.setVisibility(GONE);
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

        if (onDraggingListener != null) {
            onDraggingListener.onDragging(duration, slidePosition);
        }

        if (onDanmakuListener != null) {
            onDanmakuListener.onSeekTo(controlWrapper.getCurrentPosition());
        }
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
