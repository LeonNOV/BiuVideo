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
import com.leon.biuvideo.adapters.UserFragmentAdapters.UserVideoAdapter;
import com.leon.biuvideo.beans.upMasterBean.Video;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.VideoParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * UpMasterActivity-video fragment
 */
public class UserVideoListFragment extends Fragment {
    private long mid;
    private Context context;
    private int pageNum = 1;

    private LayoutInflater inflater;

    //记录数据是否已全部获取完
    private boolean dataState = true;

    private int currentCount;
    private int total;

    private View view;
    private RecyclerView up_video_recyclerView;
    private SmartRefreshLayout video_smartRefresh;

    public UserVideoListFragment() {
    }

    public UserVideoListFragment(long mid, Context context) {
        this.mid = mid;
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

    //初始化控件
    private void initView() {
        up_video_recyclerView = view.findViewById(R.id.user_recyclerView_space);
        video_smartRefresh = view.findViewById(R.id.user_smartRefresh);

        //关闭下拉刷新
        video_smartRefresh.setEnableRefresh(false);
    }

    //初始化数据
    private void initValue() {
        total = VideoParseUtils.getVideoTotal(mid);

        //判断获取的数据条目是否为0
        if (total == 0) {
            //设置无数据提示界面
            view = inflater.inflate(R.layout.fragment_no_data, null);
            return;
        }

        //获取初始数据
        List<Video> initVideos = VideoParseUtils.parseVideo(mid, pageNum);
        currentCount += initVideos.size();

        //判断第一次是否已加载完所有数据
        if (total == currentCount) {
            dataState = false;
            video_smartRefresh.setEnabled(false);
        }

        UserVideoAdapter userVideoAdapter = new UserVideoAdapter(initVideos, getContext());
        up_video_recyclerView.setAdapter(userVideoAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        up_video_recyclerView.setLayoutManager(layoutManager);

        //添加加载更多监听事件
        video_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

                    //结束加载更多动画
                    video_smartRefresh.finishLoadMore();

                    return;
                }

                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<Video> addOns = getNextVideos(mid, pageNum);

                            Log.d(Fuck.blue, "成功获取了" + mid + "的第" + pageNum + "页的" + addOns.size() + "条数据");

                            //添加新数据
                            userVideoAdapter.append(addOns);
                        }
                    }, 2000);
                } else {
                    //关闭上滑刷新
                    video_smartRefresh.setEnabled(false);

                    Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
                }

                //结束加载更多动画
                video_smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取数据
     *
     * @param mid     up主id
     * @param pageNum 页码
     * @return 返回UpVideo集合
     */
    private List<Video> getNextVideos(long mid, int pageNum) {
        List<Video> videos = VideoParseUtils.parseVideo(mid, pageNum);

        //记录获取的总数
        currentCount += videos.size();

        //判断是否已获取完所有的数据
        if (videos.size() < 30 || total == currentCount) {
            dataState = false;
            video_smartRefresh.setEnabled(false);
        }

        return videos;
    }
}
