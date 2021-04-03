package com.leon.biuvideo.ui.resourcesFragments.videoControlComonents;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.controller.IControlComponent;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;
import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.ValueUtils;

/**
 * @Author Leon
 * @Time 2021/4/3
 * @Desc 视频播放器底部控制视图
 */
public class VideoPlayerBottomControlView extends LinearLayout implements IControlComponent,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener{
    private ControlWrapper controlWrapper;
    private TextView videoPlayerBottomControlCurrentProgress;
    private TextView videoPlayerBottomControlTotalProgress;
    private SeekBar videoPlayerBottomControlSeekBar;
    private ImageView videoPlayerBottomControlFullScreen;
    private LinearLayout videoPlayerBottomControlSetting;
    private ImageView videoPlayerBottomPlayButton;
    private ImageView videoPlayerBottomControlDanmakuControl;
    private EditText videoPlayerBottomControlDanmakuEditText;
    private TextView videoPlayerBottomControlSpeed;
    private TextView videoPlayerBottomControlQuality;
    private ProgressBar videoPlayerBottomControlProgressBar;

    private boolean isShowBottomProgress = true;
    private boolean isDragging;

    public VideoPlayerBottomControlView(Context context) {
        super(context);
        initView();
    }

    public VideoPlayerBottomControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoPlayerBottomControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.video_player_bottom_contrl, this, true);

        videoPlayerBottomControlCurrentProgress = findViewById(R.id.video_player_bottom_control_currentProgress);
        videoPlayerBottomControlTotalProgress = findViewById(R.id.video_player_bottom_control_totalProgress);

        videoPlayerBottomControlSeekBar = findViewById(R.id.video_player_bottom_control_seekBar);
        videoPlayerBottomControlSeekBar.setOnSeekBarChangeListener(this);

        videoPlayerBottomControlFullScreen = findViewById(R.id.video_player_bottom_control_full_screen);
        videoPlayerBottomControlFullScreen.setOnClickListener(this);

        videoPlayerBottomControlSetting = findViewById(R.id.video_player_bottom_control_setting);
        videoPlayerBottomPlayButton = findViewById(R.id.video_player_bottom_play_button);
        videoPlayerBottomPlayButton.setOnClickListener(this);

        videoPlayerBottomControlDanmakuControl = findViewById(R.id.video_player_bottom_control_danmaku_control);
        videoPlayerBottomControlDanmakuControl.setOnClickListener(this);

        videoPlayerBottomControlDanmakuEditText = findViewById(R.id.video_player_bottom_control_danmaku_editText);
        videoPlayerBottomControlSpeed = findViewById(R.id.video_player_bottom_control_speed);
        videoPlayerBottomControlSpeed.setOnClickListener(this);

        videoPlayerBottomControlQuality = findViewById(R.id.video_player_bottom_control_quality);
        videoPlayerBottomControlQuality.setOnClickListener(this);

        videoPlayerBottomControlProgressBar = findViewById(R.id.video_player_bottom_control_progressBar);
    }

    /**
     * 是否显示底部进度条，默认显示
     */
    public void showBottomProgress (boolean isShow) {
        isShowBottomProgress = isShow;
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
        if (isVisible) {
            this.setVisibility(VISIBLE);
            if (anim != null) {
                this.startAnimation(anim);
            }

            if (isShowBottomProgress) {
                videoPlayerBottomControlProgressBar.setVisibility(GONE);
            }
        } else {
            this.setVisibility(GONE);
            if (anim != null) {
                this.startAnimation(anim);
            }

            if (isShowBottomProgress) {
                videoPlayerBottomControlProgressBar.setVisibility(VISIBLE);
                AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(300);
                videoPlayerBottomControlProgressBar.startAnimation(animation);
            }
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);

                videoPlayerBottomControlProgressBar.setProgress(0);
                videoPlayerBottomControlProgressBar.setSecondaryProgress(0);

                videoPlayerBottomControlSeekBar.setProgress(0);
                videoPlayerBottomControlSeekBar.setSecondaryProgress(0);
                break;
            case VideoView.STATE_START_ABORT:
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
                setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
                videoPlayerBottomPlayButton.setSelected(true);

                if (isShowBottomProgress) {
                    if (controlWrapper.isShowing()) {
                        videoPlayerBottomControlProgressBar.setVisibility(GONE);
                        this.setVisibility(VISIBLE);
                    } else {
                        videoPlayerBottomControlProgressBar.setVisibility(VISIBLE);
                        this.setVisibility(GONE);
                    }
                } else {
                    this.setVisibility(GONE);
                }

                setVisibility(VISIBLE);

                //开始刷新进度
                controlWrapper.startProgress();
                break;
            case VideoView.STATE_PAUSED:
                videoPlayerBottomPlayButton.setSelected(false);
                break;
            case VideoView.STATE_BUFFERING:
            case VideoView.STATE_BUFFERED:
                videoPlayerBottomPlayButton.setSelected(controlWrapper.isPlaying());
                break;
            default:
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        // 如果处于未全屏状态下则隐藏setting部分,并显示窗口放大按钮
        if (playerState == VideoView.PLAYER_NORMAL) {
            videoPlayerBottomControlSetting.setVisibility(GONE);
            videoPlayerBottomControlFullScreen.setVisibility(VISIBLE);
        } else {
            videoPlayerBottomControlSetting.setVisibility(VISIBLE);
            videoPlayerBottomControlFullScreen.setVisibility(GONE);
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && controlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = controlWrapper.getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                this.setPadding(0, 0, 0, 0);
                videoPlayerBottomControlProgressBar.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                this.setPadding(cutoutHeight, 0, 0, 0);
                videoPlayerBottomControlProgressBar.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                this.setPadding(0, 0, cutoutHeight, 0);
                videoPlayerBottomControlProgressBar.setPadding(0, 0, cutoutHeight, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {
        if (isDragging) {
            return;
        }
        if (videoPlayerBottomControlSeekBar != null) {
            if (duration > 0) {
                videoPlayerBottomControlSeekBar.setEnabled(true);

                int pos = (int) (position * 1.0 / duration * videoPlayerBottomControlSeekBar.getMax());
                videoPlayerBottomControlSeekBar.setProgress(pos);
                videoPlayerBottomControlProgressBar.setProgress(pos);
            } else {
                videoPlayerBottomControlSeekBar.setEnabled(false);
            }

            int percent = controlWrapper.getBufferedPercentage();

            if (percent >= 95) {
                videoPlayerBottomControlSeekBar.setSecondaryProgress(videoPlayerBottomControlSeekBar.getMax());
                videoPlayerBottomControlProgressBar.setSecondaryProgress(videoPlayerBottomControlProgressBar.getMax());
            } else {
                videoPlayerBottomControlSeekBar.setSecondaryProgress(percent * 10);
                videoPlayerBottomControlProgressBar.setSecondaryProgress(percent * 10);
            }
        }

        if (videoPlayerBottomControlTotalProgress != null) {
            videoPlayerBottomControlTotalProgress.setText(ValueUtils.lengthGenerate(duration));
        }

        if (videoPlayerBottomControlCurrentProgress != null) {
            videoPlayerBottomControlCurrentProgress.setText(ValueUtils.lengthGenerate(duration));
        }
    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        onVisibilityChanged(!isLocked, null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_player_bottom_control_full_screen:
                // 横竖屏切换
                Activity activity = PlayerUtils.scanForActivity(getContext());
                controlWrapper.toggleFullScreen(activity);
                break;
            case R.id.video_player_bottom_play_button:
                controlWrapper.togglePlay();
                break;
            case R.id.video_player_bottom_control_danmaku_control:
                Toast.makeText(getContext(), "弹幕开关", Toast.LENGTH_SHORT).show();
                break;
            case R.id.video_player_bottom_control_speed:
                Toast.makeText(getContext(), "倍速播放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.video_player_bottom_control_quality:
                Toast.makeText(getContext(), "清晰度选择", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            return;
        }

        long duration = controlWrapper.getDuration();
        long newPosition = (duration * progress) / videoPlayerBottomControlSeekBar.getMax();

        if (videoPlayerBottomControlCurrentProgress != null) {
            videoPlayerBottomControlCurrentProgress.setText(ValueUtils.lengthGenerate((int) newPosition));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        controlWrapper.stopProgress();
        controlWrapper.stopFadeOut();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long duration = controlWrapper.getDuration();
        long newPosition = (duration * seekBar.getProgress()) / videoPlayerBottomControlSeekBar.getMax();

        controlWrapper.seekTo((int) newPosition);
        isDragging = false;
        controlWrapper.startProgress();
        controlWrapper.startFadeOut();
    }
}