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
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.UpVideoParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * UpMasterActivity-video fragment
 */
public class UserVideoListFragment extends Fragment {
    private long mid;
    private int pageNum;
    private Context context;

    private LayoutInflater inflater;

    //记录数据是否已全部获取完
    private boolean dataState;

    private int valueCount;
    private int total = -1;

    private View view;
    private RecyclerView up_video_recyclerView;
    private SmartRefreshLayout video_smartRefresh;

    public UserVideoListFragment() {
    }

    public UserVideoListFragment(long mid, int pageNum, Context context) {
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

    //初始化控件
    private void initView() {
        up_video_recyclerView = view.findViewById(R.id.user_recyclerView_space);
        video_smartRefresh = view.findViewById(R.id.user_smartRefresh);

        //关闭下拉刷新
        video_smartRefresh.setEnableRefresh(false);
    }

    //初始化数据
    private void initValue() {
        valueCount = UpVideoParseUtils.count;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        up_video_recyclerView.setLayoutManager(layoutManager);

        //获取初始数据
        List<UpVideo> upVideos = getUpVideos(mid, pageNum);

        //判断获取的数据条目是否为0
        if (upVideos.size() == 0) {

            //设置无数据提示界面
            view = inflater.inflate(R.layout.fragment_no_data, null);
        }

        UserVideoAdapter userVideoAdapter = new UserVideoAdapter(upVideos, getContext());
        up_video_recyclerView.setAdapter(userVideoAdapter);

        //添加加载更多监听事件
        video_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<UpVideo> addOns = getUpVideos(mid, pageNum);

                            Log.d(Fuck.blue, "成功获取了" + mid + "的第" + pageNum + "页的" + addOns.size() + "条数据");

                            //添加新数据
                            userVideoAdapter.append(addOns);
                        }
                    }, 1000);
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
    private List<UpVideo> getUpVideos(long mid, int pageNum) {
        List<UpVideo> temp = UpVideoParseUtils.parseVideo(mid, pageNum);

        //记录获取的总数
        valueCount += temp.size();

        //判断是否已获取完所有的数据
        if (temp.size() < 30 || valueCount == total) {
            dataState = false;
        } else {
            dataState = true;
        }

        return temp;
    }
}
