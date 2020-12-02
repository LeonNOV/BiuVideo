package com.leon.biuvideo.ui.fragments.searchResultFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.leon.biuvideo.values.OrderType;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.VideoParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchResultActivity-video Fragment
 */
public class VideoResultFragment extends Fragment {
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;
    private TextView search_result_no_data;

    private String keyword;

    private int count;
    private int currentCount;

    private VideoParser videoParser;
    private List<Video> videos;

    private Context context;
    private LinearLayoutManager linearLayoutManager;
    private UserVideoAdapter userVideoAdapter;

    private boolean dataState = true;
    private int pageNum = 1;

    private View view;

    public VideoResultFragment() {
    }

    public VideoResultFragment(String keyword) {
        this.keyword = keyword;
    }

    public static VideoResultFragment getInstance(String keyword) {
        VideoResultFragment videoResultFragment = new VideoResultFragment(keyword);

        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        videoResultFragment.setArguments(bundle);

        return videoResultFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_result_fragment, container, false);

        initView();
        initVisibility();

        return view;
    }

    /**
     * 初始控件
     */
    private void initView() {
        context = getContext();

        search_result_no_data = view.findViewById(R.id.search_result_no_data);
        search_result_smartRefresh = view.findViewById(R.id.search_result_smartRefresh);
        search_result_recyclerView = view.findViewById(R.id.search_result_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    /**
     * 初始化控件Visibility
     */
    private void initVisibility() {
        //获取总条目数，最大为1000，最小为0
        count = VideoParser.getSearchVideoCount(keyword);

        //判断结果是否与搜索关键词匹配
        if (VideoParser.dataState(keyword) || count == 0) {
            //设置无数据提示界面
            search_result_no_data.setVisibility(View.VISIBLE);
            search_result_recyclerView.setVisibility(View.GONE);
            search_result_smartRefresh.setEnabled(false);
        } else {
            search_result_no_data.setVisibility(View.GONE);
            search_result_recyclerView.setVisibility(View.VISIBLE);
            search_result_smartRefresh.setEnabled(true);

            if (videoParser == null) {
                videoParser = new VideoParser();
            }

            //获取第一页数据
            List<Video> newVideos = videoParser.videoParse(keyword, pageNum, OrderType.DEFAULT);

            //获取第一页结果总数，最大为20，最小为0
            currentCount += newVideos.size();

            //判断第一次加载是否已加载完所有数据
            if (count == newVideos.size()) {
                dataState = false;
                //关闭上滑加载
                search_result_smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || userVideoAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                userVideoAdapter = new UserVideoAdapter(newVideos, context);
            }

            videos = newVideos;

            initAttr();
        }
    }

    /**
     * 初始化控件属性
     */
    private void initAttr() {
        search_result_recyclerView.setLayoutManager(linearLayoutManager);
        search_result_recyclerView.setAdapter(userVideoAdapter);

        //添加加载更多监听事件
        search_result_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

                    //结束加载更多动画
                    search_result_smartRefresh.finishLoadMore();

                    return;
                }

                RefreshState state = refreshLayout.getState();

                //判断是否处于拖拽已释放的状态
                if (state.finishing == RefreshState.ReleaseToLoad.finishing) {
                    if (dataState) {
                        pageNum++;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //获取新数据
                                List<Video> addOns = getVideos(pageNum);

                                Log.d(Fuck.blue, "成功获取了第" + pageNum + "页的" + addOns.size() + "条数据");

                                //添加新数据
                                userVideoAdapter.append(addOns);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        search_result_smartRefresh.setEnabled(false);

                        Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                search_result_smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页的数据
     *
     * @param pageNum   页码
     * @return  返回下一页的数据
     */
    public List<Video> getVideos(int pageNum) {
        List<Video> newVideos = videoParser.videoParse(keyword, pageNum, OrderType.DEFAULT);

        //记录获取的总数
        currentCount += newVideos.size();

        //判断是否已获取完所有的数据
        if (newVideos.size() < 20 || currentCount == count) {
            dataState = false;
        }

        return newVideos;
    }

    /**
     * 用于二次搜索刷新fragment数据
     *
     * @param keyword   搜索关键字
     */
    public void updateData(String keyword) {
        this.keyword = keyword;     //初始化当前搜索关键字
        this.pageNum = 1;       //初始化当前页码值
        this.currentCount = 0;      //重置现数据数量
        this.dataState = true;

        //获取二次搜索的数据
        initVisibility();

        /**
         * 需要将二次搜索的第一个页面的数据放入一个临时的变量中
         * 以防userVideoAdapter.removeAll()将其清空
         */
        List<Video> temp = new ArrayList<>(videos);

        userVideoAdapter.removeAll();
        userVideoAdapter.append(temp);
    }
}