package com.leon.biuvideo.ui.resourcesFragment.audio;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.Audio;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.service.DownloadWatcher;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.contribution.VideoFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.WarnDialog;
import com.leon.biuvideo.utils.PermissionUtil;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.AudioParser;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @Author Leon
 * @Time 2021/4/13
 * @Desc 音乐播放界面
 */
public class AudioFragment extends BaseSupportFragment implements View.OnClickListener {
    public final String sid;

    private Audio audio;
    private AudioController audioController;
    private SeekBar audioSeekBar;
    private ImageView audioControl;
    private TextView audioCurrentProgress;
    private TextView audioLength;
    private ImageView audioCover;
    private ImageView audioBg;

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

        findView(R.id.audio_back).setOnClickListener(this);
        findView(R.id.audio_link).setOnClickListener(this);
        findView(R.id.audio_download).setOnClickListener(this);
        findView(R.id.audio_play_video).setOnClickListener(this);

        audioCover = findView(R.id.audio_cover);
        audioBg = findView(R.id.audio_bg);

        audioControl = findView(R.id.audio_control);
        audioControl.setOnClickListener(this);

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

        initAudioController();

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                audio = (Audio) msg.obj;

                if (audio != null) {
                    audioController.setUrl(audio.streamUrl);

                    ((TextView) findView(R.id.audio_title)).setText(audio.title);
                    ((TextView) findView(R.id.audio_author)).setText(audio.author);

                    Glide.with(context).load(audio.cover).into(audioCover);

                    Glide
                            .with(context).load(audio.cover)
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3))).into(audioBg);

                    audioController.setPlayerStat();
                } else {
                    SimpleSnackBar.make(getActivity().getWindow().getDecorView(), getString(R.string.snackBarDataErrorWarn), SimpleSnackBar.LENGTH_LONG).show();
                    backPressed();
                }
            }
        });

        getAudioInfo();
    }

    /**
     * 初始化音频控制器
     */
    public void initAudioController() {
        audioController = new AudioController(context);
        audioController.setAudioControllerCallback(new AudioController.AudioControllerCallback() {
            @Override
            public void finished() {
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
                        audioControl.setSelected(true);

                        break;
                    case AudioController.PAUSED:
                        audioControl.setSelected(false);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio_back:
                backPressed();
                break;
            case R.id.audio_favorite:
                SimpleSnackBar.make(view, getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_SHORT).show();
                break;
            case R.id.audio_play_video:
                if (audio.bvid != null) {
                    start(new VideoFragment(audio.bvid));
                } else {
                    SimpleSnackBar.make(v, "该音频没有对应的视频哎~", SimpleSnackBar.LENGTH_SHORT).show();
                }
                break;
            case R.id.audio_link:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://m.bilibili.com/audio/au" + sid));
                startActivity(intent);
                break;
            case R.id.audio_download:
                if (verifyIOPermission()) {
                    downloadAudio();
                } else {
                    requestIOPermission();
                }
                break;
            case R.id.audio_control:
                audioController.setPlayerStat();
                break;
            default:
                break;
        }
    }

    /**
     * 进度转换
     */
    private String toLengthStr(int value) {
        int minute = value / 1000 / 60;
        int second = value / 1000 % 60;
        return (minute < 10 ? "0" + minute : minute + "") + ":" + (second < 10 ? "0" + second : second + "");
    }

    /**
     * 获取音频信息
     */
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

    /**
     * 下载音频
     */
    private void downloadAudio () {
        File file = ResourceDownloadTask.checkSaveDirectory(context, ResourceDownloadTask.RES_TYPE_VIDEO,
                audio.title, audio.author);
        boolean exists = ResourceDownloadTask.isExists(file, context, sid, null);

        if (!exists) {
            DownloadHistory downloadHistory = new DownloadHistory();
            downloadHistory.setResType(ResourceDownloadTask.RES_TYPE_AUDIO);
            downloadHistory.setIsFailed(false);
            downloadHistory.setIsCompleted(false);
            downloadHistory.setIsMultipleAnthology(false);
            downloadHistory.setLevelOneId(sid);
            downloadHistory.setLevelTwoId(null);
            downloadHistory.setMainTitle(audio.title);
            downloadHistory.setSubTitle(audio.author);
            downloadHistory.setCoverUrl(audio.cover);
            downloadHistory.setResStreamUrl(audio.streamUrl);

            ResourceDownloadTask resourceDownloadTask = new ResourceDownloadTask(context, this, downloadHistory);
            resourceDownloadTask.startDownload();

            Toast.makeText(context, R.string.downloadJoinQueue, Toast.LENGTH_SHORT).show();
            DownloadWatcher.addTask(resourceDownloadTask);
        } else {
            Toast.makeText(context, context.getString(R.string.downloadExisted), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 请求读写权限
     */
    private void requestIOPermission () {
        WarnDialog warnDialog = new WarnDialog(context, "读写权限", "由于保存资源文件时需要用到'读写权限',否则将无法正常下载视频、音频等资源");
        warnDialog.setOnWarnActionListener(new WarnDialog.OnWarnActionListener() {
            @Override
            public void onConfirm() {
                warnDialog.dismiss();
                PermissionUtil permissionUtil = new PermissionUtil(context, AudioFragment.this);
                permissionUtil.verifyPermission(PermissionUtil.Permission.RW);
            }

            @Override
            public void onCancel() {
                warnDialog.dismiss();
            }
        });
        warnDialog.show();
    }

    /**
     * 验证读写权限是否已授予
     */
    private boolean verifyIOPermission() {
        return PermissionUtil.verifyPermission(context, PermissionUtil.Permission.RW);
    }

    /**
     * 读写权限回调
     *
     * @param requestCode  请求码
     * @param permissions  权限名称
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1025) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                downloadAudio();
            } else {
                SimpleSnackBar.make(view, "请授予'读写权限',否则将不能下载~", SimpleSnackBar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        audioController.release();

        super.onDestroy();
    }
}
