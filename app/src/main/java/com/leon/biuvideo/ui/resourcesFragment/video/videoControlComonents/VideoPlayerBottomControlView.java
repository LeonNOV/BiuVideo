package com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.controller.IControlComponent;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.VideoQualityAdapter;
import com.leon.biuvideo.adapters.otherAdapters.VideoSpeedAdapter;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.wraps.DanmakuWrap;
import com.leon.biuvideo.wraps.VideoQualityWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Author Leon
 * @Time 2021/4/3
 * @Desc 视频播放器底部控制视图
 */
public class VideoPlayerBottomControlView extends FrameLayout implements IControlComponent,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener{
    private ControlWrapper controlWrapper;
    private TextView videoPlayerBottomControlCurrentProgress;
    private TextView videoPlayerBottomControlTotalProgress;
    private SeekBar videoPlayerBottomControlSeekBar;
    private ImageView videoPlayerBottomControlFullScreen;
    private LinearLayout videoPlayerBottomControlSetting;
    private ImageView videoPlayerBottomPlayButton;
    public ImageView videoPlayerBottomControlDanmakuControl;
    private EditText videoPlayerBottomControlDanmakuEditText;
    private TextView videoPlayerBottomControlSpeed;
    private TextView videoPlayerBottomControlQuality;
    private ProgressBar videoPlayerBottomControlProgressBar;

    private float speed = 1.0f;
    private boolean isShowBottomProgress = true;
    private boolean isDragging;
    private LinearLayout videoPlayerBottomControlContent;

    private OnDanmakuListener onDanmakuListener;
    private VideoWithFlv videoWithFlv;

    public void setOnDanmakuListener(OnDanmakuListener onDanmakuListener) {
        this.onDanmakuListener = onDanmakuListener;
    }

    public VideoPlayerBottomControlView(Context context, VideoWithFlv videoWithFlv) {
        super(context);
        this.videoWithFlv = videoWithFlv;
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
        // 注册监听
        EventBus.getDefault().register(this);

        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.video_player_bottom_control, this, true);

        videoPlayerBottomControlContent = findViewById(R.id.video_player_bottom_control_content);
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
        videoPlayerBottomControlDanmakuControl.setSelected(true);
        videoPlayerBottomControlDanmakuControl.setOnClickListener(this);

        videoPlayerBottomControlDanmakuEditText = findViewById(R.id.video_player_bottom_control_danmaku_editText);
        videoPlayerBottomControlSpeed = findViewById(R.id.video_player_bottom_control_speed);
        videoPlayerBottomControlSpeed.setOnClickListener(this);

        videoPlayerBottomControlQuality = findViewById(R.id.video_player_bottom_control_quality);
        videoPlayerBottomControlQuality.setText(videoWithFlv.qualityMap.get(videoWithFlv.currentQualityId));
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
            videoPlayerBottomControlContent.setVisibility(VISIBLE);
            if (anim != null) {
                videoPlayerBottomControlContent.startAnimation(anim);
            }

            if (isShowBottomProgress) {
                videoPlayerBottomControlProgressBar.setVisibility(GONE);
            }
        } else {
            videoPlayerBottomControlContent.setVisibility(GONE);
            if (anim != null) {
                videoPlayerBottomControlContent.startAnimation(anim);
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
                        videoPlayerBottomControlContent.setVisibility(VISIBLE);
                    } else {
                        videoPlayerBottomControlProgressBar.setVisibility(VISIBLE);
                        videoPlayerBottomControlContent.setVisibility(GONE);
                    }
                } else {
                    videoPlayerBottomControlContent.setVisibility(GONE);
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
                videoPlayerBottomControlContent.setPadding(0, 0, 0, 0);
                videoPlayerBottomControlProgressBar.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                videoPlayerBottomControlContent.setPadding(cutoutHeight, 0, 0, 0);
                videoPlayerBottomControlProgressBar.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                videoPlayerBottomControlContent.setPadding(0, 0, cutoutHeight, 0);
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

                int videoPos = (int) (position * 1.0 / duration * videoPlayerBottomControlSeekBar.getMax());

                videoPlayerBottomControlSeekBar.setProgress(videoPos);
                videoPlayerBottomControlProgressBar.setProgress(videoPos);
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
            videoPlayerBottomControlTotalProgress.setText(PlayerUtils.stringForTime(duration));
        }

        if (videoPlayerBottomControlCurrentProgress != null) {
            videoPlayerBottomControlCurrentProgress.setText(PlayerUtils.stringForTime(position));
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
                boolean selected = videoPlayerBottomControlDanmakuControl.isSelected();
                videoPlayerBottomControlDanmakuControl.setSelected(!selected);
                EventBus.getDefault().post(DanmakuWrap.getInstance(videoPlayerBottomControlDanmakuControl.isSelected()));
                break;
            case R.id.video_player_bottom_control_speed:
                VideoSpeedDialog videoSpeedDialog = new VideoSpeedDialog(getContext(), speed);
                videoSpeedDialog.setOnVideoSpeedListener(new VideoSpeedAdapter.OnVideoSpeedListener() {
                    @Override
                    public void onSpeed(float speed) {
                        VideoPlayerBottomControlView.this.speed = speed;
                        controlWrapper.setSpeed(speed);
                        videoSpeedDialog.dismiss();
                    }
                });
                videoSpeedDialog.show();
                break;
            case R.id.video_player_bottom_control_quality:
                VideoQualityDialog videoQualityDialog = new VideoQualityDialog(getContext(), videoWithFlv.currentQualityId, videoWithFlv.qualityMap);
                videoQualityDialog.setOnVideoQualityListener(new VideoQualityAdapter.OnVideoQualityListener() {
                    @Override
                    public void onQuality(int qualityId) {
                        videoWithFlv.currentQualityId = qualityId;
                        videoPlayerBottomControlQuality.setText(videoWithFlv.qualityMap.get(qualityId));
                        EventBus.getDefault().post(VideoQualityWrap.getInstance(qualityId));

                        videoQualityDialog.dismiss();
                    }
                });
                videoQualityDialog.show();
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
            videoPlayerBottomControlCurrentProgress.setText(PlayerUtils.stringForTime((int) newPosition));
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
        long videoDuration = controlWrapper.getDuration();
        long videoNewPosition = (videoDuration * seekBar.getProgress()) / videoPlayerBottomControlSeekBar.getMax();

        controlWrapper.seekTo((int) videoNewPosition);

        isDragging = false;
        controlWrapper.startProgress();
        controlWrapper.startFadeOut();

        if (onDanmakuListener != null) {
            onDanmakuListener.onSeekTo(videoNewPosition);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage (DanmakuWrap danmakuWrap) {
        videoPlayerBottomControlDanmakuControl.setSelected(danmakuWrap.danmakuState);
    }

    /**
     * 设置底部视频进度条进度
     *
     * @param duration  总长度
     * @param slidePosition 滑动进度
     */
    public void setBottomProgressPosition (int duration, int slidePosition) {
        int pos = (int) (slidePosition * 1.0 / duration * videoPlayerBottomControlSeekBar.getMax());
        videoPlayerBottomControlProgressBar.setProgress(pos);
    }
}