package com.leon.biuvideo.ui.fragments.userFragments;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.UserAudioAdapter;
import com.leon.biuvideo.beans.upMasterBean.Audio;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.AudioParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * UpMasterActivity-audio/music fragment
 */
public class UserAudiosFragment extends BaseFragment {
    private final long mid;

    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;
    private TextView no_data;

    private AudioParseUtils audioParseUtils;

    private int pageNum = 1;
    private int currentCount;
    private boolean dataState = true;

    private List<Audio> audios;

    private UserAudioAdapter userAudioAdapter;
    private LinearLayoutManager linearLayoutManager;

    public UserAudiosFragment(long mid) {
        this.mid = mid;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);
        smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        no_data = findView(R.id.smart_refresh_layout_fragment_no_data);

        //关闭下拉刷新
        smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void initValues() {
        audioParseUtils = new AudioParseUtils();

        int total = audioParseUtils.getAudioTotal(mid);

        if (total == 0) {
            //设置无数据提示界面
            no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            smartRefresh.setEnabled(false);
        } else {
            no_data.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            smartRefresh.setEnabled(true);

            //获取初始数据
            audios = audioParseUtils.parseAudio(mid, pageNum);
            currentCount += audios.size();
            pageNum++;

            if (currentCount < 30) {
                dataState = false;
                smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || userAudioAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                userAudioAdapter = new UserAudioAdapter(audios, context);
            }

            initAttr();
        }
    }

    private void initAttr() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAudioAdapter);

        Handler handler = new Handler();

        //添加加载更多监听事件
        smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

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
                                getAudios(mid, pageNum);
                                pageNum++;

                                //添加新数据
                                userAudioAdapter.append(audios);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        smartRefresh.setEnabled(false);

                        Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取数据
     *
     * @param mid     up主id
     * @param pageNum 页码
     */
    private void getAudios(long mid, int pageNum) {
        audios = audioParseUtils.parseAudio(mid, pageNum);
        currentCount += audios.size();

        //如果第一次获取的条目数小于30则设置dataState
        if (audios.size() < 30) {
            dataState = false;
            smartRefresh.setEnabled(false);
        }
    }
}