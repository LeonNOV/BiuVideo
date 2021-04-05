package com.leon.biuvideo.ui.resourcesFragment.videoControlComonents;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.controller.IControlComponent;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;
import com.leon.biuvideo.R;

/**
 * @Author Leon
 * @Time 2021/4/3
 * @Desc 视频播放器标题视图
 */
public class VideoPlayerTitleView extends LinearLayout implements IControlComponent {
    private ControlWrapper controlWrapper;

    private TextView videoPlayerTitleViewTitle;
    private TextView videoPlayerTitleViewTime;
    private BatteryReceiver batteryReceiver;

    /**
     * 是否注册BatteryReceiver
     */
    private boolean isRegister;

    public VideoPlayerTitleView(Context context) {
        super(context);
        initView();
    }

    public VideoPlayerTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoPlayerTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.video_player_title_view, this, true);
        findViewById(R.id.video_player_title_view_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = PlayerUtils.scanForActivity(getContext());
                if (activity != null && controlWrapper.isFullScreen()) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    controlWrapper.stopFullScreen();
                }
            }
        });
        videoPlayerTitleViewTitle = findViewById(R.id.video_player_title_view_title);

        batteryReceiver = new BatteryReceiver(findViewById(R.id.video_player_title_view_battery));

        videoPlayerTitleViewTime = findViewById(R.id.video_player_title_view_time);
    }

    public void setTitle (String title) {
        videoPlayerTitleViewTitle.setText(title);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isRegister) {
            getContext().unregisterReceiver(batteryReceiver);
            isRegister = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isRegister) {
            getContext().registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            isRegister = true;
        }
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
        // 在全屏时有效
        if (!controlWrapper.isFullScreen()) {
            return;
        }

        if (isVisible) {
            if (getVisibility() == GONE) {
                videoPlayerTitleViewTime.setText(PlayerUtils.getCurrentSystemTime());
                setVisibility(VISIBLE);

                if (anim != null) {
                    startAnimation(anim);
                }
            }
        } else {
            if (getVisibility() == VISIBLE) {
                setVisibility(GONE);
                if (anim != null) {
                    startAnimation(anim);
                }
            }
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
            case VideoView.STATE_START_ABORT:
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        if (playerState == VideoView.PLAYER_FULL_SCREEN) {
            if (controlWrapper.isShowing() && !controlWrapper.isLocked()) {
                setVisibility(VISIBLE);
                videoPlayerTitleViewTime.setText(PlayerUtils.getCurrentSystemTime());
            }
            videoPlayerTitleViewTitle.setSelected(true);
        } else {
            setVisibility(GONE);
            videoPlayerTitleViewTitle.setSelected(false);
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && controlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = controlWrapper.getCutoutHeight();

            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                this.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                this.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                this.setPadding(0, 0, cutoutHeight, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            videoPlayerTitleViewTime.setText(PlayerUtils.getCurrentSystemTime());
        }
    }

    private static class BatteryReceiver extends BroadcastReceiver {
        private final ImageView batteryImageView;

        public BatteryReceiver(ImageView batteryImageView) {
            this.batteryImageView = batteryImageView;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();

            if (extras == null) {
                return;
            }

            // 当前电量
            int current = extras.getInt("level");

            // 总电量
            int total = extras.getInt("scale");

            // 获取电量比例
            int percent = current * 100 / total;

            // 根据电量百分比设置Drawable资源
            batteryImageView.getDrawable().setLevel(percent);
        }
    }
}
