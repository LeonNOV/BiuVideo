package com.leon.biuvideo.ui.fragments.userFragments;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.UserPictureAdapter;
import com.leon.biuvideo.beans.upMasterBean.Picture;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.PictureParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * UpMasterActivity-pic fragment
 */
public class UserPicturesFragment extends BaseFragment {
    private final long mid;

    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefresh;
    private TextView no_data;

    private PictureParser pictureParser;

    private int pageNum = 0;
    private int currentCount;
    private boolean dataState = true;
    private int count;

    private List<Picture> pictures;

    private UserPictureAdapter userPictureAdapter;
    private GridLayoutManager gridLayoutManager;

    public UserPicturesFragment(long mid) {
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
        pictureParser = new PictureParser(context);

        count = pictureParser.getPictureTotal(mid);

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
            pictures = pictureParser.parsePicture(mid, pageNum);
            currentCount += pictures.size();
            pageNum++;

            if (currentCount == count) {
                dataState = false;
                smartRefresh.setEnabled(false);
            }

            if (gridLayoutManager == null || userPictureAdapter == null) {
                gridLayoutManager = new GridLayoutManager(context, 2);
                userPictureAdapter = new UserPictureAdapter(pictures, context);
            }

            initAttr();
        }
    }

    private void initAttr() {
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(userPictureAdapter);

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
                        pageNum++;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //获取新数据
                                getPictures();

                                //添加新数据
                                userPictureAdapter.append(pictures);
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
     * 获取下一页数据
     */
    private void getPictures() {
        pictures = pictureParser.parsePicture(mid, pageNum);

        //记录获取的总数
        currentCount += pictures.size();

        //判断是否已获取完所有的数据
        if (currentCount == count) {
            dataState = false;
            smartRefresh.setEnabled(false);
        }
    }
}
