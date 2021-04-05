package com.leon.biuvideo.ui.resourcesFragments;

import android.content.Context;

import com.dueeeke.videoplayer.player.PlayerFactory;

/**
 * @Author Leon
 * @Time 2021/4/5
 * @Desc BiuVideoPlayer工厂类
 */
public class BiuVideoPlayerFactory extends PlayerFactory<BiuVideoPlayer> {
    public static BiuVideoPlayerFactory create () {
        return new BiuVideoPlayerFactory();
    }

    @Override
    public BiuVideoPlayer createPlayer(Context context) {
        return new BiuVideoPlayer(context);
    }
}
