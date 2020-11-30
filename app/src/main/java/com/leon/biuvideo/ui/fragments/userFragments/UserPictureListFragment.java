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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.UserPictureAdapter;
import com.leon.biuvideo.beans.upMasterBean.Picture;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.PictureParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * UpMasterActivity-pic fragment
 */
public class UserPictureListFragment extends Fragment {
    private long mid;
    private Context context;
    private int pageNum = 0;

    private LayoutInflater inflater;

    //记录数据是否已全部获取完
    private boolean dataState = true;
    private int total;
    private int currentCount;

    private View view;
    private RecyclerView picture_recyclerView;
    private SmartRefreshLayout picture_smartRefresh;

    public UserPictureListFragment() {
    }

    public UserPictureListFragment(long mid, Context context) {
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
        picture_recyclerView = view.findViewById(R.id.user_recyclerView_space);
        picture_smartRefresh = view.findViewById(R.id.user_smartRefresh);

        //关闭下拉刷新
        picture_smartRefresh.setEnableRefresh(false);
    }

    private void initValue() {
        total = PictureParseUtils.getPictureTotal(mid);

        //判断获取的数据条目是否为0
        if (total == 0) {
            //设置无数据提示界面
            view = inflater.inflate(R.layout.fragment_no_data, null);
            return;
        }

        //获取初始数据
        List<Picture> initPictures = PictureParseUtils.parsePicture(mid, pageNum);
        currentCount += initPictures.size();

        //判断第一次是否已加载完所有数据
        if (total == currentCount) {
            dataState = false;
            picture_smartRefresh.setEnabled(false);
        }

        UserPictureAdapter userPictureAdapter = new UserPictureAdapter(initPictures, getContext());
        picture_recyclerView.setAdapter(userPictureAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        picture_recyclerView.setLayoutManager(gridLayoutManager);

        //添加加载更多监听事件
        picture_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

                    //结束加载更多动画
                    picture_smartRefresh.finishLoadMore();

                    return;
                }

                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<Picture> addOns = getNextPictures(mid, pageNum);

                            Log.d(Fuck.blue, "成功获取了" + mid + "的第" + pageNum + "页的" + addOns.size() + "条数据");

                            //添加新数据
                            userPictureAdapter.refresh(addOns);
                        }
                    }, 2000);
                } else {
                    //关闭上滑刷新
                    picture_smartRefresh.setEnabled(false);

                    Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
                }

                //结束加载更多动画
                picture_smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 获取下一页数据
     *
     * @param mid     用户ID
     * @param pageNum 页码
     * @return  返回下一页数据
     */
    private List<Picture> getNextPictures(long mid, int pageNum) {
        List<Picture> pictures = PictureParseUtils.parsePicture(mid, pageNum);

        //记录获取的总数
        currentCount += pictures.size();

        //判断是否已获取完所有的数据
        if (pictures.size() < 30 || total == currentCount) {
            dataState = false;
            picture_smartRefresh.setEnabled(false);
        }

        return pictures;
    }
}
