package com.leon.biuvideo.ui.fragments.UserFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.leon.biuvideo.beans.upMasterBean.UpAudio;
import com.leon.biuvideo.ui.activitys.UpSongActivity;
import com.leon.biuvideo.utils.resourcesParseUtils.UpAudioParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * UpMasterActivity中的audio/music片段
 */
public class UserAudioListFragment extends Fragment {
    private long mid;
    private int pageNum;
    private Context context;

    private LayoutInflater inflater;

    //记录数据是否已全部获取完
    private boolean dataState;
    private int valueCount;
    private int total = -1;

    private View view;
    private RecyclerView up_audio_recyclerView;
    private SmartRefreshLayout up_smartRefresh;

    public UserAudioListFragment() {
    }

    public UserAudioListFragment(long mid, int pageNum, Context context) {
        this.mid = mid;
        this.pageNum = pageNum;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_up_space, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        up_audio_recyclerView  = view.findViewById(R.id.user_recyclerView_space);
        up_smartRefresh = view.findViewById(R.id.user_smartRefresh);

        //关闭下拉刷新
        up_smartRefresh.setEnableRefresh(false);
    }

    private void initValue() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        up_audio_recyclerView.setLayoutManager(layoutManager);

        //获取初始数据
        List<UpAudio> upAudios = getUpAudios(mid, pageNum);

        //判断获取的数据条目是否为0
        if (upAudios.size() == 0) {

            //设置无数据提示界面
            view = inflater.inflate(R.layout.fragment_up_no_data, null);
        }

        UserAudioAdapter userAudioAdapter = new UserAudioAdapter(upAudios, context);
        up_audio_recyclerView.setAdapter(userAudioAdapter);

        //设置item点击事件，跳转到音乐播放界面
        userAudioAdapter.setOnItemClickListener((view, position) -> {

            //获取所有sid
            long[] sids = new long[upAudios.size()];

            for (int i = 0; i < upAudios.size(); i++) {
                sids[i] = (upAudios.get(i).sid);
            }

            Intent intent = new Intent(context, UpSongActivity.class);

            //传递sid在upAudios中的position
            intent.putExtra("position", position);

            //传递所有的sid
            intent.putExtra("sids", sids);

            startActivity(intent);
        });

        //添加加载更多监听事件
        up_smartRefresh.setOnLoadMoreListener(refreshLayout -> {

            if (dataState) {
                pageNum++;

                new Handler().postDelayed(() -> {
                    //获取新数据
                    List<UpAudio> addOns = getUpAudios(mid, pageNum);

                    //添加新数据
                    userAudioAdapter.refresh(addOns);
                }, 2000);
            } else {
                Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
            }

            //结束加载更多动画
            up_smartRefresh.finishLoadMore();
        });
    }

    /**
     * 获取数据
     *
     * @param mid   up主id
     * @param pageNum   页码
     * @return  返回UpAudio集合
     */
    private List<UpAudio> getUpAudios(long mid, int pageNum) {
        List<UpAudio> temp = UpAudioParseUtils.parseAudio(mid, pageNum);

        //记录获取的总数
        valueCount += temp.size();

        //初始化数据总数目
        if (total == -1) {
            valueCount = UpAudioParseUtils.count;
        }

        //判断是否已获取完所有的数据
        if (temp.size() < 30 || valueCount == total) {
            dataState = false;
        } else {
            dataState = true;
        }

        return temp;
    }
}
