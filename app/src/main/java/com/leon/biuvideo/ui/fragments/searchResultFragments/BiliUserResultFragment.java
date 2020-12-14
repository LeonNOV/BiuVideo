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
import com.leon.biuvideo.adapters.UserFragmentAdapters.BiliUserAdapter;
import com.leon.biuvideo.beans.BiliUser;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.OrderType;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BiliUserParser;
import com.leon.biuvideo.values.SortType;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchResultActivity-BiliUser Fragment
 */
public class BiliUserResultFragment extends Fragment {
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;
    private TextView search_result_no_data;

    private String keyword;

    private int count;
    private int currentCount;

    private BiliUserParser biliUserParser;
    private List<BiliUser> biliUsers;

    private Context context;
    private BiliUserAdapter biliUserAdapter;
    private LinearLayoutManager linearLayoutManager;

    private boolean dataState = true;
    private int pageNum = 1;

    private View view;

    public BiliUserResultFragment() {
    }

    public BiliUserResultFragment(String keyword) {
        this.keyword = keyword;
    }

    public static BiliUserResultFragment getInstance(String keyword) {
        BiliUserResultFragment biliUserResultFragment = new BiliUserResultFragment(keyword);

        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        biliUserResultFragment.setArguments(bundle);

        return biliUserResultFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.smart_refresh_layout_fragment, container, false);

        initView();
        initVisibility();

        return view;
    }

    private void initView() {
        context = getContext();

        search_result_no_data = view.findViewById(R.id.smart_refresh_layout_fragment_no_data);
        search_result_smartRefresh = view.findViewById(R.id.smart_refresh_layout_fragment_smartRefresh);
        search_result_recyclerView = view.findViewById(R.id.smart_refresh_layout_fragment_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    /**
     * 初始化控件Visibility
     */
    private void initVisibility() {
        if (getDataState()) {
            //设置无数据提示界面
            search_result_no_data.setVisibility(View.VISIBLE);
            search_result_recyclerView.setVisibility(View.GONE);
            search_result_smartRefresh.setEnabled(false);
        } else {
            search_result_no_data.setVisibility(View.GONE);
            search_result_recyclerView.setVisibility(View.VISIBLE);
            search_result_smartRefresh.setEnabled(true);

            if (biliUserParser == null) {
                biliUserParser = new BiliUserParser();
            }

            List<BiliUser> newBiliUsers = biliUserParser.userParse(keyword, pageNum, SortType.DEFAULT);

            currentCount += newBiliUsers.size();

            if (count == newBiliUsers.size()) {
                dataState = false;
                //关闭上滑加载
                search_result_smartRefresh.setEnabled(false);
            }

            if (linearLayoutManager == null || biliUserAdapter == null) {
                linearLayoutManager = new LinearLayoutManager(context);
                biliUserAdapter = new BiliUserAdapter(newBiliUsers, context);
            }

            biliUsers = newBiliUsers;

            initAttr();
        }
    }

    /**
     * 初始化控件属性
     */
    private void initAttr() {
        search_result_recyclerView.setLayoutManager(linearLayoutManager);
        search_result_recyclerView.setAdapter(biliUserAdapter);

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

                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<BiliUser> addOns = getBiliUsers(pageNum);

                            Log.d(Fuck.blue, "成功获取了第" + pageNum + "页的" + addOns.size() + "条数据");

                            //添加新数据
                            biliUserAdapter.append(addOns);
                        }
                    }, 1000);
                } else {
                    //关闭上滑刷新
                    search_result_smartRefresh.setEnabled(false);

                    Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
                }

                //结束加载更多动画
                search_result_smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取数据状态，同时对总数进行赋值
     *
     * @return  返回是否等于0
     */
    public boolean getDataState() {
        //获取结果总数，最大为1000， 最小为0
        count = BiliUserParser.getSearchUserCount(keyword);

        return count == 0;
    }

    /**
     * 获取下一页用户数据
     *
     * @param pageNum   页码
     * @return  返回下一页用户数据
     */
    public List<BiliUser> getBiliUsers(int pageNum) {
        List<BiliUser> biliUsers = biliUserParser.userParse(keyword, pageNum, SortType.DEFAULT);

        //记录获取的总数
        currentCount += biliUsers.size();

        //判断是否已获取完所有的数据
        if (biliUsers.size() < 20 || currentCount == count) {
            dataState = false;
        }

        return biliUsers;
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

        initVisibility();

        ArrayList<BiliUser> temp = new ArrayList<>(biliUsers);

        biliUserAdapter.removeAll();
        biliUserAdapter.append(temp);
    }
}
