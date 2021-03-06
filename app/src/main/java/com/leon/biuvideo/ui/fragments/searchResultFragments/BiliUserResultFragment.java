package com.leon.biuvideo.ui.fragments.searchResultFragments;

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
import com.leon.biuvideo.adapters.userFragmentAdapters.BiliUserAdapter;
import com.leon.biuvideo.beans.searchBean.BiliUser;
import com.leon.biuvideo.ui.AbstractSimpleLoadDataThread;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseLazyFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BiliUserParser;
import com.leon.biuvideo.values.SortType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * SearchResultActivity-BiliUser Fragment
 */
public class BiliUserResultFragment extends BaseLazyFragment {
    private LinearLayout smart_refresh_layout_fragment_linearLayout;
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;
    private TextView search_result_no_data;

    private String keyword;

    private int count;
    private int currentCount;

    private BiliUserParser biliUserParser;
    private List<BiliUser> biliUserList;

    private BiliUserAdapter biliUserAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;

    private boolean dataState = true;
    private int pageNum = 1;
    private Handler handler;

    public BiliUserResultFragment() {
    }

    public BiliUserResultFragment(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int setLayout() {
        return R.layout.smart_refresh_layout_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        smart_refresh_layout_fragment_linearLayout = findView(R.id.smart_refresh_layout_fragment_linearLayout);
        search_result_no_data = findView(R.id.smart_refresh_layout_fragment_no_data);
        search_result_smartRefresh = findView(R.id.smart_refresh_layout_fragment_smartRefresh);
        search_result_recyclerView = findView(R.id.smart_refresh_layout_fragment_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void loadData() {
        AbstractSimpleLoadDataThread abstractSimpleLoadDataThread = new AbstractSimpleLoadDataThread() {
            @Override
            public void load() {
                if (biliUserParser == null) {
                    biliUserParser = new BiliUserParser(context);
                }
                count = biliUserParser.getSearchUserCount(keyword);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        };

        SimpleThreadPool simpleThreadPool = abstractSimpleLoadDataThread.getSimpleThreadPool();
        simpleThreadPool.submit(new FutureTask<>(abstractSimpleLoadDataThread), "loadBiliUserResult");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                smart_refresh_layout_fragment_linearLayout.setVisibility(View.GONE);

                if (loadState) {
                    initValues();
                }

                simpleThreadPool.cancelTask("loadBiliUserResult");

                return true;
            }
        });
    }

    @Override
    public void initValues() {
        if (count == 0) {
            //设置无数据提示界面
            search_result_no_data.setVisibility(View.VISIBLE);
            search_result_recyclerView.setVisibility(View.GONE);
            search_result_smartRefresh.setEnabled(false);
        } else {
            search_result_no_data.setVisibility(View.GONE);
            search_result_recyclerView.setVisibility(View.VISIBLE);
            search_result_smartRefresh.setEnabled(true);

            biliUserList = biliUserParser.userParse(keyword, pageNum, SortType.DEFAULT);
            currentCount += biliUserList.size();
            pageNum++;

            if (count == biliUserList.size()) {
                dataState = false;
                //关闭上滑加载
                search_result_smartRefresh.setEnabled(false);
            }

            if (favoriteUserDatabaseUtils == null) {
                favoriteUserDatabaseUtils = new FavoriteUserDatabaseUtils(context);
            }

            linearLayoutManager = new LinearLayoutManager(context);
            biliUserAdapter = new BiliUserAdapter(biliUserList, context, favoriteUserDatabaseUtils);

            initAttr();
        }
    }

    /**
     * 初始化控件属性
     */
    private void initAttr() {
        search_result_recyclerView.setLayoutManager(linearLayoutManager);
        search_result_recyclerView.setAdapter(biliUserAdapter);

        Handler handler = new Handler();

        //添加加载更多监听事件
        search_result_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    SimpleSnackBar.make(view, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();

                    //结束加载更多动画
                    search_result_smartRefresh.finishLoadMore();

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
                                getBiliUsers();

                                //添加新数据
                                biliUserAdapter.append(biliUserList);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        search_result_smartRefresh.setEnabled(false);

                        SimpleSnackBar.make(view, R.string.isDone, SimpleSnackBar.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                search_result_smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页用户数据
     */
    public void getBiliUsers() {
        biliUserList = biliUserParser.userParse(keyword, pageNum, SortType.DEFAULT);

        //记录获取的总数
        currentCount += biliUserList.size();

        //判断是否已获取完所有的数据
        if (currentCount == count) {
            dataState = false;
            search_result_smartRefresh.setEnabled(false);
        }

        pageNum++;
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

        // 将isLoaded状态设置为“未加载状态”
        this.isLoaded = false;
        onResume();
        this.smart_refresh_layout_fragment_linearLayout.setVisibility(View.VISIBLE);
        this.search_result_no_data.setVisibility(View.GONE);

        if (biliUserList != null) {
            biliUserAdapter.removeAll();
        }
    }

    @Override
    public void onDestroyView() {
        if (favoriteUserDatabaseUtils != null) {
            favoriteUserDatabaseUtils.close();
        }
        super.onDestroyView();
    }
}
