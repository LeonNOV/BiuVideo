package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.GestureVideoController;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoDanmakuView;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.OnDanmakuPositionStateListener;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerBottomControlView;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerCompleteView;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerErrorView;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerGestureView;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerPrepareView;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerTitleView;

/**
 * @Author Leon
 * @Time 2021/4/2
 * @Desc 视频播放控制器
 */
public class VideoPlayerController extends GestureVideoController {
    private ImageView videoPlayerControllerLock;
    private ProgressBar videoPlayerControllerLoading;
    private VideoDanmakuView videoDanmakuView;

    private VideoPlayerTitleView.OnBackListener onBackListener;

    public void setOnBackListener(VideoPlayerTitleView.OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public VideoPlayerController(@NonNull Context context) {
        super(context);
    }

    public VideoPlayerController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.video_player_controller;
    }

    @Override
    protected void initView() {
        super.initView();

        videoPlayerControllerLock = findViewById(R.id.video_player_controller_lock);
        videoPlayerControllerLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mControlWrapper.toggleLockState();
            }
        });

        videoPlayerControllerLoading = findViewById(R.id.video_player_controller_loading);
    }

    public void addDefaultControlComponent (String title, String cid) {
        VideoPlayerCompleteView videoPlayerCompleteView = new VideoPlayerCompleteView(getContext());
        VideoPlayerErrorView videoPlayerErrorView = new VideoPlayerErrorView(getContext());
        VideoPlayerPrepareView videoPlayerPrepareView = new VideoPlayerPrepareView(getContext());
        videoPlayerPrepareView.setClickStart();

        VideoPlayerTitleView videoPlayerTitleView = new VideoPlayerTitleView(getContext());
        videoPlayerTitleView.setOnBackListener(onBackListener);
        videoPlayerTitleView.setTitle(title);

        addControlComponent(videoPlayerCompleteView, videoPlayerErrorView, videoPlayerPrepareView, videoPlayerTitleView);

        VideoPlayerBottomControlView videoPlayerBottomControlView = new VideoPlayerBottomControlView(getContext());
        videoPlayerBottomControlView.setOnDanmakuPositionStateListener(new OnDanmakuPositionStateListener() {
            @Override
            public void onStart() {
                videoDanmakuView.hideDanmaku();
            }

            @Override
            public void onStop(long position) {
                videoDanmakuView.setPosition(position);
            }
        });

        VideoPlayerGestureView videoPlayerGestureView = new VideoPlayerGestureView(getContext());
        videoPlayerGestureView.setOnDraggingListener(new VideoPlayerGestureView.OnDraggingListener() {
            @Override
            public void onDragging(int duration, int slidePosition) {
                videoPlayerBottomControlView.setBottomProgressPosition(duration, slidePosition);
            }
        });
        videoPlayerGestureView.setOnDanmakuPositionStateListener(new OnDanmakuPositionStateListener() {
            @Override
            public void onStart() {
                videoDanmakuView.hideDanmaku();
            }

            @Override
            public void onStop(long position) {
                videoDanmakuView.setPosition(position);
            }
        });

        videoDanmakuView = new VideoDanmakuView(getContext(), cid);

        addControlComponent(videoPlayerBottomControlView, videoPlayerGestureView, videoDanmakuView);
    }

    /**
     * 控制弹幕视图是否显示
     *
     * @param stat  显示状态
     */
    public void setDanmakuVisibility (boolean stat) {
        if (stat) {
            videoDanmakuView.show();
        } else {
            videoDanmakuView.hide();
        }
    }

    private void addDanmaku (String text) {
        if (videoDanmakuView != null) {
            videoDanmakuView.addDanmaku(null, true);
        }
    }

    @Override
    protected void onLockStateChanged(boolean isLocked) {
        videoPlayerControllerLock.setSelected(isLocked);
    }

    @Override
    protected void onVisibilityChanged(boolean isVisible, Animation anim) {
        if (mControlWrapper.isFullScreen()) {
            if (isVisible) {
                if (videoPlayerControllerLock.getVisibility() == GONE) {
                    videoPlayerControllerLock.setVisibility(VISIBLE);
                    if (anim != null) {
                        videoPlayerControllerLock.startAnimation(anim);
                    }
                }
            } else {
                videoPlayerControllerLock.setVisibility(GONE);
                if (anim != null) {
                    videoPlayerControllerLock.startAnimation(anim);
                }
            }
        }
    }

    @Override
    protected void onPlayerStateChanged(int playerState) {
        super.onPlayerStateChanged(playerState);
        switch (playerState) {
            case VideoView.PLAYER_NORMAL:
                setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                videoPlayerControllerLock.setVisibility(GONE);
                break;
            case VideoView.PLAYER_FULL_SCREEN:
                videoPlayerControllerLock.setVisibility(isShowing() ? VISIBLE : GONE);
                break;
            default:
                break;
        }

        if (mActivity != null && hasCutout()) {
            int orientation = mActivity.getRequestedOrientation();
            int dp24 = PlayerUtils.dp2px(getContext(), 24);
            int cutoutHeight = getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                FrameLayout.LayoutParams lblp = (LayoutParams) videoPlayerControllerLock.getLayoutParams();
                lblp.setMargins(dp24, 0, dp24, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                FrameLayout.LayoutParams layoutParams = (LayoutParams) videoPlayerControllerLock.getLayoutParams();
                layoutParams.setMargins(dp24 + cutoutHeight, 0, dp24 + cutoutHeight, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                FrameLayout.LayoutParams layoutParams = (LayoutParams) videoPlayerControllerLock.getLayoutParams();
                layoutParams.setMargins(dp24, 0, dp24, 0);
            }
        }
    }

    @Override
    protected void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            //调用release方法会回到此状态
            case VideoView.STATE_IDLE:
                videoPlayerControllerLock.setSelected(false);
                videoPlayerControllerLoading.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
            case VideoView.STATE_PAUSED:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_BUFFERED:
                videoPlayerControllerLoading.setVisibility(GONE);
                break;
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_BUFFERING:
                videoPlayerControllerLoading.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                videoPlayerControllerLoading.setVisibility(GONE);
                videoPlayerControllerLock.setVisibility(GONE);
                videoPlayerControllerLock.setSelected(false);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isLocked()) {
            show();
            return true;
        }

        if (mControlWrapper.isFullScreen()) {
            return stopFullScreen();
        }

        return super.onBackPressed();
    }
}
