package com.leon.biuvideo.ui.resourcesFragments;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.dueeeke.videoplayer.player.AbstractPlayer;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;

import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @Author Leon
 * @Time 2021/4/5
 * @Desc 可以播放音视频的自定义播放器
 */
public class BiuVideoPlayer extends AbstractPlayer {
    protected IjkMediaPlayer mVideoPlayer;
    protected IjkMediaPlayer mAudioPlayer;

    private int mBufferedPercent;

    public BiuVideoPlayer(Context context) {

    }

    @Override
    public void initPlayer() {
        initVideoPlayer();
        initAudioPlayer();
    }

    private void initVideoPlayer() {
        mVideoPlayer = new IjkMediaPlayer();
        //native日志
        IjkMediaPlayer.native_setLogLevel(VideoViewManager.getConfig().mIsEnableLog ? IjkMediaPlayer.IJK_LOG_INFO : IjkMediaPlayer.IJK_LOG_SILENT);
        setOptions();
        mVideoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mVideoPlayer.setOnErrorListener(onErrorListener);
        mVideoPlayer.setOnCompletionListener(onCompletionListener);
        mVideoPlayer.setOnInfoListener(onInfoListener);
        mVideoPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        mVideoPlayer.setOnPreparedListener(onPreparedListener);
        mVideoPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        mVideoPlayer.setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
            @Override
            public boolean onNativeInvoke(int i, Bundle bundle) {
                return true;
            }
        });
    }

    private void initAudioPlayer() {
        mAudioPlayer = new IjkMediaPlayer();
        //native日志
        IjkMediaPlayer.native_setLogLevel(VideoViewManager.getConfig().mIsEnableLog ? IjkMediaPlayer.IJK_LOG_INFO : IjkMediaPlayer.IJK_LOG_SILENT);
        setOptions();
        mAudioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mAudioPlayer.setOnErrorListener(onErrorListener);
        mAudioPlayer.setOnCompletionListener(onCompletionListener);
        mAudioPlayer.setOnInfoListener(onInfoListener);
        mAudioPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        mAudioPlayer.setOnPreparedListener(onPreparedListener);
        mAudioPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        mAudioPlayer.setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
            @Override
            public boolean onNativeInvoke(int i, Bundle bundle) {
                return true;
            }
        });
    }

    @Override
    public void setDataSource(String videoPath, String audioPath, Map<String, String> headers) {
        try {
            mVideoPlayer.setDataSource(videoPath, headers);
            mAudioPlayer.setDataSource(audioPath, headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {

    }

    @Override
    public void start() {
        try {
            mVideoPlayer.start();
            mAudioPlayer.start();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void pause() {
        try {
            mVideoPlayer.pause();
            mAudioPlayer.pause();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void stop() {
        try {
            mVideoPlayer.stop();
            mAudioPlayer.stop();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void prepareAsync() {
        try {
            mVideoPlayer.prepareAsync();
            mAudioPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void reset() {
        mVideoPlayer.reset();
        mVideoPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);

        mAudioPlayer.reset();
        mAudioPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);

        setOptions();
    }

    @Override
    public boolean isPlaying() {
        return mVideoPlayer.isPlaying() && mAudioPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        try {
            mVideoPlayer.seekTo((int) time);
            mAudioPlayer.seekTo((int) time);
        } catch (IllegalStateException e) {
            mPlayerEventListener.onError();
        }
    }

    @Override
    public void release() {
        mVideoPlayer.setOnErrorListener(null);
        mVideoPlayer.setOnCompletionListener(null);
        mVideoPlayer.setOnInfoListener(null);
        mVideoPlayer.setOnBufferingUpdateListener(null);
        mVideoPlayer.setOnPreparedListener(null);
        mVideoPlayer.setOnVideoSizeChangedListener(null);

        mAudioPlayer.setOnErrorListener(null);
        mAudioPlayer.setOnCompletionListener(null);
        mAudioPlayer.setOnInfoListener(null);
        mAudioPlayer.setOnBufferingUpdateListener(null);
        mAudioPlayer.setOnPreparedListener(null);
        mAudioPlayer.setOnVideoSizeChangedListener(null);

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                mVideoPlayer.release();
                mAudioPlayer.release();
            }
        });
    }

    @Override
    public long getCurrentPosition() {
        return mAudioPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mAudioPlayer.getDuration();
    }

    @Override
    public int getBufferedPercentage() {
        return mBufferedPercent;
    }

    @Override
    public void setSurface(Surface surface) {
        mVideoPlayer.setSurface(surface);
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        mVideoPlayer.setDisplay(holder);
    }

    @Override
    public void setVolume(float v1, float v2) {
        mVideoPlayer.setVolume(v1, v2);
        mAudioPlayer.setVolume(v1, v2);
    }

    @Override
    public void setLooping(boolean isLooping) {
        mVideoPlayer.setLooping(isLooping);
        mAudioPlayer.setLooping(isLooping);
    }

    @Override
    public void setOptions() {

    }

    @Override
    public void setSpeed(float speed) {
        mVideoPlayer.setSpeed(speed);
        mAudioPlayer.setSpeed(speed);
    }

    @Override
    public float getSpeed() {
        return mAudioPlayer.getSpeed(0);
    }

    @Override
    public long getTcpSpeed() {
        return mVideoPlayer.getTcpSpeed();
    }

    private final IMediaPlayer.OnErrorListener onErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
            mPlayerEventListener.onError();
            return true;
        }
    };

    private final IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            mPlayerEventListener.onCompletion();
        }
    };

    private final IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
            mPlayerEventListener.onInfo(what, extra);
            return true;
        }
    };

    private final IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
            mBufferedPercent = percent;
        }
    };

    private final IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            mPlayerEventListener.onPrepared();
        }
    };

    private final IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
            int videoWidth = iMediaPlayer.getVideoWidth();
            int videoHeight = iMediaPlayer.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                mPlayerEventListener.onVideoSizeChanged(videoWidth, videoHeight);
            }
        }
    };
}
