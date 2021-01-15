package com.leon.biuvideo.ui.fragments.searchResultFragments;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.BangumiAdapter;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiParser;
import com.leon.biuvideo.values.SortType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 番剧搜索结果fragment
 */
public class BangumiResultFragment extends BaseFragment {
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;
    private TextView search_result_no_data;
    private String keyword;

    private BangumiParser bangumiParser;
    private List<Bangumi> bangumiList;

    private int count;
    private int pageNum = 1;
    private int currentCount;
    private boolean dataState;

    private BangumiAdapter bangumiAdapter;
    private LinearLayoutManager linearLayoutManager;

    public BangumiResultFragment(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        search_result_no_data = view.findViewById(R.id.smart_refresh_layout_fragment_no_data);
        search_result_smartRefresh = view.findViewById(R.id.smart_refresh_layout_fragment_smartRefresh);
        search_result_recyclerView = view.findViewById(R.id.smart_refresh_layout_fragment_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void initValues() {
        bangumiParser = new BangumiParser(context);
        count = bangumiParser.getSearchBangumiCount(keyword);

        if (count == 0) {
            //设置无数据提示界面
            search_result_no_data.setVisibility(View.VISIBLE);
            search_result_recyclerView.setVisibility(View.GONE);
            search_result_smartRefresh.setVisibility(View.GONE);
        } else {
            search_result_no_data.setVisibility(View.GONE);
            search_result_recyclerView.setVisibility(View.VISIBLE);
            search_result_smartRefresh.setVisibility(View.VISIBLE);

            bangumiList = bangumiParser.bangumiParse(keyword, pageNum, SortType.DEFAULT);
            pageNum++;

            currentCount = bangumiList.size();

            if (bangumiList.size() == count) {
                dataState = false;
                search_result_smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || bangumiAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                bangumiAdapter = new BangumiAdapter(bangumiList, context);
            }

            initAttr();
        }
    }

    private void initAttr() {
        search_result_recyclerView.setAdapter(bangumiAdapter);
        search_result_recyclerView.setLayoutManager(linearLayoutManager);

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
                                getBangumi();

                                Log.d(Fuck.blue, "成功获取了第" + pageNum + "页的" + bangumiList.size() + "条数据");

                                //添加新数据
                                bangumiAdapter.append(bangumiList);
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

    private void getBangumi() {
        this.bangumiList = bangumiParser.bangumiParse(keyword, pageNum, SortType.DEFAULT);

        currentCount += this.bangumiList.size();

        if (currentCount == count) {
            dataState = false;
            search_result_smartRefresh.setEnabled(false);
        }
    }

    /**
     * 用于二次搜索刷新fragment数据
     *
     * @param keyword   搜索关键字
     */
    public void updateData(String keyword) {
        this.keyword = keyword;
        this.pageNum = 1;
        this.currentCount = 0;
        this.dataState = true;

        initValues();

        if (bangumiList != null) {
            ArrayList<Bangumi> temp = new ArrayList<>(bangumiList);
            bangumiAdapter.removeAll();
            bangumiAdapter.append(temp);
        }
    }
}
