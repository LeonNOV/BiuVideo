package com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.controller.IControlComponent;
import com.dueeeke.videoplayer.player.VideoView;
import com.leon.biuvideo.ui.resourcesFragment.video.BiliDanmakuParser;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;
import com.leon.biuvideo.wraps.DanmakuWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * @Author Leon
 * @Time 2021/4/17
 * @Desc 弹幕视图
 */
public class VideoDanmakuView extends DanmakuView implements IControlComponent {
    private String cid;
    private DanmakuContext danmakuContext;
    private Handler handler;
    private BaseDanmakuParser baseDanmakuParser;

    private ControlWrapper controlWrapper;

    /**
     * 弹幕速度，和视频速度始终相同
     */
    private float speed = 1.0f;

    public VideoDanmakuView(Context context, String cid) {
        super(context);
        this.cid = cid;
        initView();
    }

    public VideoDanmakuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoDanmakuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        // 注册监听
        EventBus.getDefault().register(this);

        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>(1);

        // 滚动弹幕最大显示5行
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5);

        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>(2);
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair)
                .setDanmakuMargin(40);

        setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                start();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {
                if (speed > 1.0) {
                    timer.add((long) (timer.lastInterval() * (speed - 1)));
                } else {
                    timer.update(controlWrapper.getCurrentPosition());
                }
            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });

        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.obj != null) {
                    InputStream inputStream = (InputStream) msg.obj;

                    baseDanmakuParser = createDanmakuParser(inputStream);
                    prepare(baseDanmakuParser, danmakuContext);
                }

                return true;
            }
        });

        enableDanmakuDrawingCache(false);
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
        switch (playState) {
            case VideoView.STATE_IDLE:
                release();
                break;
            case VideoView.STATE_PREPARING:
                if (isPrepared()) {
                    restart();
                }

                getDanmakuData();
                Fuck.blue("danmaku cid----" + cid);
                break;
            case VideoView.STATE_PLAYING:
                if (isPrepared() && isPaused()) {
                    resume();
                }
                break;
            case VideoView.STATE_PAUSED:
                if (isPrepared()) {
                    pause();
                }
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                clear();
                clearDanmakusOnScreen();
                break;
            default:
                break;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetDanmakuMessage(DanmakuWrap danmakuWrap) {
        if (danmakuWrap.danmakuState) {
            show();
        } else {
            hide();
        }
    }

    public void setPosition (long position) {
        seekTo(position);
    }

    public void addDanmaku (String text, boolean isSelf) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null) {
            return;
        }

        danmaku.text = text;

        // 可能会被各种过滤器过滤并隐藏显示
        danmaku.priority = 0;
        danmaku.isLive = false;
        danmaku.padding = 5;
        danmaku.setTime(getCurrentTime() + 1200);
        danmaku.textSize = 25f * (baseDanmakuParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = Color.GRAY;

        danmaku.borderColor = isSelf ? Color.GREEN : Color.TRANSPARENT;
        addDanmaku(danmaku);
    }

    /**
     * 弹幕解析器
     *
     * @param inputStream   已解码过的数据流
     * @return  BaseDanmakuParser
     */
    private BaseDanmakuParser  createDanmakuParser (InputStream inputStream) {
        if (inputStream == null) {
            return new BaseDanmakuParser() {
                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(inputStream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }

        BaseDanmakuParser parser = new BiliDanmakuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }

    /**
     * 获取弹幕数据
     */
    private void getDanmakuData () {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>(1);
                params.put("oid", cid);

                byte[] byteArray = new HttpUtils(BiliBiliAPIs.DANMAKU, params).getByteArray();

                // 需要对响应结果进行解压
                byte[] bytes = ValueUtils.unZipXML(byteArray);

                Message message = handler.obtainMessage();
                message.obj = new ByteArrayInputStream(bytes);
                handler.sendMessage(message);
            }
        });
    }

    public void reset (String cid) {
        release();
        this.cid = cid;
    }
}
