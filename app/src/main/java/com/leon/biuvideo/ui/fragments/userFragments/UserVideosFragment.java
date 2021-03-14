package com.leon.biuvideo.ui.fragments.userFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userFragmentAdapters.UserVideoAdapter;
import com.leon.biuvideo.beans.upMasterBean.Video;
import com.leon.biuvideo.ui.SimpleLoadDataThread;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseLazyFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.VideoParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * UpMasterActivity-video fragment
 */
public class UserVideosFragment extends BaseLazyFragment {
    private final long mid;

    private LinearLayout smart_refresh_layout_fragment_linearLayout;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;
    private TextView no_data;

    private VideoParser videoParser;

    private int pageNum = 1;
    private int currentCount;
    private boolean dataState = true;

    private List<Video> videos;

    private UserVideoAdapter userVideoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int count;
    private Handler handler;

    public UserVideosFragment(long mid) {
        this.mid = mid;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        smart_refresh_layout_fragment_linearLayout = findView(R.id.smart_refresh_layout_fragment_linearLayout);
        recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);
        smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        no_data = findView(R.id.smart_refresh_layout_fragment_no_data);

        //关闭下拉刷新
        smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void loadData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                videoParser = new VideoParser(context);
                count = videoParser.getVideoTotal(mid);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                smart_refresh_layout_fragment_linearLayout.setVisibility(View.GONE);

                if (loadState) {
                    initValues();
                }

                return true;
            }
        });
    }

    @Override
    public void initValues() {
        if (count == 0) {
            //设置无数据提示界面
            no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            smartRefresh.setEnabled(false);
        } else {
            no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            smartRefresh.setEnabled(true);

            //获取初始数据
            videos = videoParser.parseVideo(mid, pageNum);
            currentCount += videos.size();
            pageNum++;

            if (currentCount < 30) {
                dataState = false;
                smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || userVideoAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                userVideoAdapter = new UserVideoAdapter(videos, context);
            }

            initAttr();
        }
    }

    private void initAttr() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userVideoAdapter);

        Handler handler = new Handler();

        //添加加载更多监听事件
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    SimpleSnackBar.make(view, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();

                    //结束加载更多动画
                    smartRefresh.finishLoadMore();

                    return;
                }

                RefreshState state = refreshLayout.getState();

                //判断是否处于拖拽已释放的状态
                if (state.finishing == RefreshState.ReleaseToLoad.finishing) {
                    if (dataState) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //获取新数据
                                getVideos();

                                //添加新数据
                                userVideoAdapter.append(videos);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        smartRefresh.setEnabled(false);

                        SimpleSnackBar.make(view, R.string.isDone, SimpleSnackBar.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页数据
     */
    private void getVideos() {
        videos = videoParser.parseVideo(mid, pageNum);

        //记录获取的总数
        currentCount += videos.size();

        //判断是否已获取完所有的数据
        if (currentCount == count) {
            dataState = false;
            smartRefresh.setEnabled(false);
        }

        pageNum++;
    }
}
