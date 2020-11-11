package com.leon.biuvideo.ui.fragments.UserFragments;

import android.content.Context;
import android.content.Intent;
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
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.ui.activitys.PictureActivity;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.resourcesParseUtils.UpPictureParseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * UpMasterActivity中的pic片段
 */
public class UserPictureListFragment extends Fragment {
    private long mid;
    private int pageNum;
    private Context context;

    private LayoutInflater inflater;

    //记录数据是否已全部获取完
    private boolean dataState;
    private int valueCount;

    private View view;
    private RecyclerView picture_recyclerView;
    private SmartRefreshLayout picture_smartRefresh;

    public UserPictureListFragment() {
    }

    public UserPictureListFragment(long mid, int pageNum, Context context) {
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
        picture_recyclerView = view.findViewById(R.id.user_recyclerView_space);
        picture_smartRefresh = view.findViewById(R.id.user_smartRefresh);

        //关闭下拉刷新
        picture_smartRefresh.setEnableRefresh(false);
    }

    private void initValue() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        picture_recyclerView.setLayoutManager(gridLayoutManager);

        //获取初始数据
        List<UpPicture> upPictures = getUpPictures(mid, pageNum);

        //判断获取的数据条目是否为0
        if (upPictures.size() == 0) {

            //设置无数据提示界面
            view = inflater.inflate(R.layout.fragment_up_no_data, null);
        }

        UserPictureAdapter userPictureAdapter = new UserPictureAdapter(upPictures, context);
        userPictureAdapter.setOnPictureItemClickListener(new UserPictureAdapter.OnPictureItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //跳转到PictureActivity
                Intent intent = new Intent(context, PictureActivity.class);

                //传递整个UpPicture对象
                Bundle bundle = new Bundle();
                bundle.putSerializable("picture", upPictures.get(position));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        picture_recyclerView.setAdapter(userPictureAdapter);

        //添加加载更多监听事件
        picture_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                if (dataState) {
                    pageNum++;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //获取新数据
                            List<UpPicture> addOns = getUpPictures(mid, pageNum);

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
     * 获取数据
     *
     * @param mid     up主id
     * @param pageNum 页码
     * @return 返回UpVideo集合
     */
    private List<UpPicture> getUpPictures(long mid, int pageNum) {
        List<UpPicture> temp = UpPictureParseUtils.parsePicture(mid, pageNum);

        //记录获取的总数
        valueCount += temp.size();

        //判断是否已获取完所有的数据
        if (temp.size() < 30 || valueCount == valueCount) {
            dataState = false;
        } else {
            dataState = true;
        }

        return temp;
    }
}
