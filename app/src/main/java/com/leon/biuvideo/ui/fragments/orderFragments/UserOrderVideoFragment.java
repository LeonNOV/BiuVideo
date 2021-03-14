package com.leon.biuvideo.ui.fragments.orderFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userOrderAdapters.UserOrderVideoFolderAdapter;
import com.leon.biuvideo.adapters.userOrderAdapters.UserOrderVideoFolderDetailAdapter;
import com.leon.biuvideo.beans.orderBeans.UserFolder;
import com.leon.biuvideo.beans.orderBeans.UserFolderData;
import com.leon.biuvideo.ui.SimpleLoadDataThread;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseLazyFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.UserFolderParser;
import com.leon.biuvideo.values.ImagePixelSize;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * PlayListFragment中的video片段
 */
public class UserOrderVideoFragment extends BaseLazyFragment {
    private final long mid;

    private long nowFolderId;
    private int pageNum = 1;
    private boolean dataState = true;

    private List<UserFolder> userFolders;
    private UserFolderData userFolderData;
    private UserFolderParser userFolderParser;

    private LinearLayout fragment_favorite_video_linearLayout;
    private SmartRefreshLayout fragment_favorite_smartRefresh;

    private RecyclerView folderList;
    private RecyclerView folderDetail;

    private UserOrderVideoFolderDetailAdapter userOrderVideoFolderDetailAdapter;
    private Handler handler;

    public UserOrderVideoFragment(long mid) {
        this.mid = mid;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_user_order_video;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        fragment_favorite_video_linearLayout = findView(R.id.fragment_favorite_video_linearLayout);
        folderList = findView(R.id.fragment_favorite_video_recyclerView_folderList);
        folderDetail = findView(R.id.fragment_favorite_video_recyclerView_folderDetail);

        fragment_favorite_smartRefresh = findView(R.id.fragment_favorite_smartRefresh);
        fragment_favorite_smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void loadData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                userFolderParser = new UserFolderParser(context);
                userFolders = userFolderParser.parseUserFolder(mid);

                //获取第一个收藏夹ID
                nowFolderId = userFolders.get(0).id;
                userFolderData = userFolderParser.parseUserFolderData(nowFolderId, pageNum);
                pageNum ++;

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                fragment_favorite_video_linearLayout.setVisibility(View.GONE);

                if (loadState) {
                    initValues();
                }

                return true;
            }
        });
    }

    @Override
    public void initValues() {
        // 设置收藏夹参数
        setFolderInfo(userFolderData);

        UserOrderVideoFolderAdapter userOrderVideoFolderAdapter = new UserOrderVideoFolderAdapter(userFolders, context);
        userOrderVideoFolderAdapter.setOnClickFolderListener(new UserOrderVideoFolderAdapter.OnClickFolderListener() {
            @Override
            public void OnClick(long folderId) {
                if (!InternetUtils.checkNetwork(context)) {
                    SimpleSnackBar.make(view, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                    return;
                }

                pageNum = 1;
                dataState = true;
                nowFolderId = folderId;
                fragment_favorite_smartRefresh.setEnabled(true);

                UserFolderData innerUserFolderData = userFolderParser.parseUserFolderData(nowFolderId, pageNum);
                userOrderVideoFolderDetailAdapter.reset(innerUserFolderData.medias);
                setFolderInfo(innerUserFolderData);
                pageNum ++;
            }
        });

        userOrderVideoFolderDetailAdapter = new UserOrderVideoFolderDetailAdapter(userFolderData.medias, context, view);
        folderList.setAdapter(userOrderVideoFolderAdapter);
        folderDetail.setAdapter(userOrderVideoFolderDetailAdapter);

        folderList.setLayoutManager(new LinearLayoutManager(context));
        folderDetail.setLayoutManager(new LinearLayoutManager(context));

        Handler handler = new Handler();

        //添加加载更多监听事件
        fragment_favorite_smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    SimpleSnackBar.make(view, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();

                    //结束加载更多动画
                    fragment_favorite_smartRefresh.finishLoadMore();

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
                                getFavoriteVideo();

                                //添加新数据
                                userOrderVideoFolderDetailAdapter.append(userFolderData.medias);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        fragment_favorite_smartRefresh.setEnabled(false);

                        SimpleSnackBar.make(view, R.string.isDone, SimpleSnackBar.LENGTH_SHORT).show();
                    }
                }

                //结束加载更多动画
                fragment_favorite_smartRefresh.finishLoadMore();
            }
        });
    }

    /**
     * 设置收藏夹参数
     */
    private void setFolderInfo(UserFolderData userFolderData) {
        BindingUtils bindingUtils = new BindingUtils(view, context);
        bindingUtils
                .setImage(R.id.fragment_favorite_video_imageView_cover, userFolderData.cover, ImagePixelSize.COVER)
                .setText(R.id.fragment_favorite_video_textView_creator, userFolderData.userName)
                .setText(R.id.fragment_favorite_video_textView_total, userFolderData.total + "个内容")
                .setText(R.id.fragment_favorite_video_textView_ctime, "创建于" + ValueUtils.generateTime(userFolderData.ctime, true, false, "/"));
    }

    /**
     * 获取收藏的视频
     */
    private void getFavoriteVideo() {
        userFolderData = userFolderParser.parseUserFolderData(nowFolderId, pageNum);

        if (userFolderData.medias.size() < 20) {
            dataState = false;
            fragment_favorite_smartRefresh.setEnabled(false);
        }

        pageNum++;
    }
}