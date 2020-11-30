package com.leon.biuvideo.ui.fragments.userFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.UserAudioAdapter;
import com.leon.biuvideo.beans.upMasterBean.Audio;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.AudioParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * UpMasterActivity-audio/music fragment
 */
public class UserAudioListFragment extends Fragment {
    private long mid;
    private int pageNum = 1;
    private Context context;

    private LayoutInflater inflater;

    //记录数据是否已全部获取完
    private boolean dataState = true;
    private int currentCount;
    private int total;

    private View view;
    private RecyclerView up_audio_recyclerView;
    private SmartRefreshLayout audio_smartRefresh;

    public UserAudioListFragment() {
    }

    public UserAudioListFragment(long mid, Context context) {
        this.mid = mid;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_space, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        up_audio_recyclerView = view.findViewById(R.id.user_recyclerView_space);
        audio_smartRefresh = view.findViewById(R.id.user_smartRefresh);

        //关闭下拉刷新
        audio_smartRefresh.setEnableRefresh(false);
    }

    private void initValue() {
        total = AudioParseUtils.getAudioTotal(mid);

        //判断获取的数据条目是否为0
        if (total == 0) {
            //设置无数据提示界面
            view = inflater.inflate(R.layout.fragment_no_data, null);
            return;
        }

        //获取初始数据
        List<Audio> initAudios = AudioParseUtils.parseAudio(mid, pageNum);
        currentCount += initAudios.size();

        //判断第一次是否已加载完所有数据
        if (total == currentCount) {
            dataState = false;
            audio_smartRefresh.setEnabled(false);
        }

        UserAudioAdapter userAudioAdapter = new UserAudioAdapter(initAudios, getContext());
        up_audio_recyclerView.setAdapter(userAudioAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        up_audio_recyclerView.setLayoutManager(layoutManager);

        //添加加载更多监听事件
        audio_smartRefresh.setOnLoadMoreListener(refreshLayout -> {
            //判断是否有网络
            boolean isHaveNetwork = InternetUtils.checkNetwork(context);

            if (!isHaveNetwork) {
                Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

                //结束加载更多动画
                audio_smartRefresh.finishLoadMore();

                return;
            }

            if (dataState) {
                pageNum++;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取新数据
                        List<Audio> addOns = getNextAudios(mid, pageNum);

                        Log.d(Fuck.blue, "成功获取了" + mid + "的第" + pageNum + "页的" + addOns.size() + "条数据");

                        //添加新数据
                        userAudioAdapter.refresh(addOns);
                    }
                }, 2000);
            } else {
                //关闭上滑刷新
                audio_smartRefresh.setEnabled(false);

                Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
            }

            //结束加载更多动画
            audio_smartRefresh.finishLoadMore();
        });
    }

    /**
     * 获取数据
     *
     * @param mid     up主id
     * @param pageNum 页码
     * @return 返回UpAudio集合
     */
    private List<Audio> getNextAudios(long mid, int pageNum) {
        List<Audio> audios = AudioParseUtils.parseAudio(mid, pageNum);

        currentCount += audios.size();

        //如果第一次获取的条目数小于30则设置dataState
        if (audios.size() < 30 || total == currentCount) {
            dataState = false;
            audio_smartRefresh.setEnabled(false);
        }

        return audios;
    }
}
