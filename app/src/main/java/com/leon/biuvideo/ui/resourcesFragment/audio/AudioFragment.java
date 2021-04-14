package com.leon.biuvideo.ui.resourcesFragment.audio;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.Audio;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.AudioParser;

/**
 * @Author Leon
 * @Time 2021/4/13
 * @Desc 音乐播放界面
 */
public class AudioFragment extends BaseSupportFragment implements View.OnClickListener {
    public final String sid;

    public AudioFragment(String sid) {
        this.sid = sid;
    }

    @Override
    protected int setLayout() {
        return R.layout.audio_fragment;
    }

    @Override
    protected void initView() {
        ((SimpleTopBar) findView(R.id.audio_topBar)).setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        ImageView audioImageViewAddFavorite = findView(R.id.audio_imageView_addFavorite);

        findView(R.id.audio_musicVideo).setOnClickListener(this);
        findView(R.id.audio_link).setOnClickListener(this);
        findView(R.id.audio_download).setOnClickListener(this);

        SeekBar audioSeekBar = findView(R.id.audio_seekBar);
        TextView audioCurrentProgress = findView(R.id.audio_currentProgress);
        TextView audioLength = findView(R.id.audio_length);

        ImageView audioPrev = findView(R.id.audio_prev);
        ImageView audioControl = findView(R.id.audio_control);
        ImageView audioNext = findView(R.id.audio_next);

        AudioController audioController = new AudioController(context);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                Audio audio = (Audio) msg.obj;

                if (audio != null) {
                    audioController.setUrl(audio.streamUrl);
                    audioController.start();
                } else {
                    SimpleSnackBar.make(view, "数据获取失败~", SimpleSnackBar.LENGTH_LONG).show();
                }
            }
        });

        getAudioInfo();
    }

    @Override
    public void onClick(View v) {

    }

    public void getAudioInfo () {
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
}
