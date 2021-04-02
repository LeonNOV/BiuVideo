package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.LiveControlView;
import com.dueeeke.videocontroller.component.PrepareView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.controller.BaseVideoController;
import com.dueeeke.videoplayer.controller.GestureVideoController;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;
import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.Fuck;

/**
 * @Author Leon
 * @Time 2021/4/2
 * @Desc 视频播放控制器
 */
public class VideoPlayerController extends StandardVideoController implements View.OnClickListener {

    private TextView videoControllerTitle;
    private ImageView videoControllerLock;
    private ImageView videoControllerControl;
    private ProgressBar videoControllerLoading;
    private SeekBar videoControllerSeekBar;
    private TextView videoControllerTotalProgress;
    private TextView videoControllerCurrentProgress;

    public VideoPlayerController(@NonNull Context context) {
        super(context);
    }

    public VideoPlayerController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayerController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*@Override
    protected int getLayoutId() {
        return R.layout.video_controller;
    }*/

    @Override
    protected void initView() {
        super.initView();

        /*findViewById(R.id.video_controller_back).setOnClickListener(this);
        videoControllerTitle = findViewById(R.id.video_controller_title);

        videoControllerControl = findViewById(R.id.video_controller_control);
        videoControllerLoading = findViewById(R.id.video_controller_loading);

        videoControllerLock = findViewById(R.id.video_controller_lock);
        videoControllerLock.setOnClickListener(this);

        videoControllerCurrentProgress = findViewById(R.id.video_controller_currentProgress);
        videoControllerTotalProgress = findViewById(R.id.video_controller_totalProgress);
        videoControllerSeekBar = findViewById(R.id.video_controller_seekBar);
        videoControllerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/


    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.video_controller_back:
                break;
            case R.id.video_controller_lock:
                mControlWrapper.toggleLockState();
                break;
            case R.id.video_controller_control:
                setPlayState();
                break;
            default:
                break;
        }*/
    }

    @Override
    protected void setProgress(int duration, int position) {
        super.setProgress(duration, position);

        Fuck.blue("duration:" + duration + "----position:" + position);
    }
}
