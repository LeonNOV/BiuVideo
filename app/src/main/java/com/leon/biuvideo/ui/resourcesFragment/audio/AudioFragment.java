package com.leon.biuvideo.ui.resourcesFragment.audio;

import android.animation.ObjectAnimator;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.Audio;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.AudioParser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author Leon
 * @Time 2021/4/13
 * @Desc 音乐播放界面
 */
public class AudioFragment extends BaseSupportFragment implements View.OnClickListener {
    public final String sid;

    private ObjectAnimator rotation;

    private Audio audio;
    private AudioController audioController;
    private SeekBar audioSeekBar;
    private ImageView audioControl;
    private TextView audioCurrentProgress;
    private TextView audioLength;
    private ImageView audioImageViewAddFavorite;

    public AudioFragment(String sid) {
        this.sid = sid;
    }

    @Override
    protected int setLayout() {
        return R.layout.audio_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.audio_linearLayout).setBackgroundResource(R.color.white);

        ((SimpleTopBar) findView(R.id.audio_topBar)).setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        audioImageViewAddFavorite = findView(R.id.audio_imageView_addFavorite);
        audioImageViewAddFavorite.setOnClickListener(this);

        findView(R.id.audio_musicVideo).setOnClickListener(this);
        findView(R.id.audio_link).setOnClickListener(this);
        findView(R.id.audio_download).setOnClickListener(this);

        CircleImageView audioCover = findView(R.id.audio_cover);

        audioSeekBar = findView(R.id.audio_seekBar);
        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }

                long duration = audioController.audioDuration;
                long newPosition = (duration * progress) / audioSeekBar.getMax();
                audioController.seekTo(newPosition);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        audioCurrentProgress = findView(R.id.audio_currentProgress);
        audioLength = findView(R.id.audio_length);

        audioControl = findView(R.id.audio_control);
        audioControl.setOnClickListener(this);

        findView(R.id.audio_prev).setOnClickListener(this);
        findView(R.id.audio_next).setOnClickListener(this);

        createAnimator(audioCover);
        initAudioController();

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                audio = (Audio) msg.obj;

                if (audio != null) {
                    audioController.setUrl(audio.streamUrl);

                    ((TextView) findView(R.id.audio_audioName)).setText(audio.title);
                    Glide.with(context).load(audio.cover).into(audioCover);

                    audioController.setPlayerStat();
                    rotation.start();
                } else {
                    SimpleSnackBar.make(getActivity().getWindow().getDecorView(), "数据获取失败~", SimpleSnackBar.LENGTH_LONG).show();
                    backPressed();
                }
            }
        });

        getAudioInfo();
    }

    private void createAnimator(CircleImageView audioCover) {
        // 设置旋转动画
        rotation = ObjectAnimator.ofFloat(audioCover, "rotation", 0.0f, 360.0f);

        // 一圈的时间
        rotation.setDuration(45000);
        rotation.setInterpolator(new LinearInterpolator());

        // 次数，-1为无限制
        rotation.setRepeatCount(-1);

        // 动画始终重复
        rotation.setRepeatMode(ObjectAnimator.RESTART);
    }

    public void initAudioController() {
        audioController = new AudioController(context);
        audioController.setAudioControllerCallback(new AudioController.AudioControllerCallback() {
            @Override
            public void finished() {
                rotation.pause();
                audioControl.setSelected(false);
            }

            @Override
            public void progress(int currentPosition, int bufferPosition, int duration) {
                if (audioSeekBar != null) {
                    if (duration > 0) {
                        audioSeekBar.setEnabled(true);

                        int videoPos = (int) (currentPosition * 1.0 / duration * audioSeekBar.getMax());

                        audioSeekBar.setProgress(videoPos);
                    } else {
                        audioSeekBar.setEnabled(false);
                    }

                    if (bufferPosition >= 95) {
                        audioSeekBar.setSecondaryProgress(audioSeekBar.getMax());
                    } else {
                        audioSeekBar.setSecondaryProgress(bufferPosition * 10);
                    }
                }

                audioLength.setText(toLengthStr(duration));
                audioCurrentProgress.setText(toLengthStr(currentPosition));
            }

            @Override
            public void playStat(int stat) {
                switch (stat) {
                    case AudioController.PLAYING:
                        rotation.resume();
                        audioControl.setSelected(true);

                        break;
                    case AudioController.PAUSED:
                        rotation.pause();
                        audioControl.setSelected(false);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    private String toLengthStr(int value) {
        int minute = value / 1000 / 60;
        int second = value / 1000 % 60;
        return (minute < 10 ? "0" + minute : minute + "") + ":" + (second < 10 ? "0" + second : second + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio_imageView_addFavorite:

                break;
            case R.id.audio_musicVideo:
                if (audio.bvid != null) {
                    start(new VideoFragment(audio.bvid));
                } else {
                    SimpleSnackBar.make(v, "该音频没有对应的视频哎~", SimpleSnackBar.LENGTH_SHORT).show();
                }
                break;
            case R.id.audio_link:
                break;
            case R.id.audio_download:
                break;
            case R.id.audio_control:
                audioController.setPlayerStat();
                break;
            case R.id.audio_prev:
                break;
            case R.id.audio_next:
                break;
            default:
                break;
        }
    }

    public void getAudioInfo() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                Audio audio = AudioParser.parseData(sid);

                Message message = receiveDataHandler.obtainMessage();
                message.obj = audio;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        audioController.release();

        super.onDestroy();
    }
}
