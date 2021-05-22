
package com.leon.biuvideo;

import android.app.Application;
import android.content.Context;

import com.arialyy.aria.core.Aria;
import com.baidu.mobstat.StatService;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.leon.biuvideo.values.apis.ApiKeys;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import org.jetbrains.annotations.NotNull;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc Application
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NotNull
            @Override
            public RefreshFooter createRefreshFooter(@NotNull Context context, @NotNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });

        // Fragmentation配置
        // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)

                // 实际场景建议.debug(BuildConfig.DEBUG)
                .debug(BuildConfig.DEBUG)
                .install();

        // 视频在线播放配置
        VideoViewManager
                .setConfig(VideoViewConfig
                        .newBuilder()
                        .setPlayerFactory(IjkPlayerFactory.create())
                        .build());

        // 初始化Aria
        Aria.init(this);

        // 百度统计
        StatService.setAppKey(ApiKeys.BAIDU_KEY);
        StatService.setAuthorizedState(getApplicationContext(), false);
    }
}
