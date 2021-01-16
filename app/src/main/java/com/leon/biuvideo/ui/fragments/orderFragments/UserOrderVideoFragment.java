package com.leon.biuvideo.ui.fragments.orderFragments;

import android.os.Handler;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserDataAdapters.UserFavoriteFolderAdapter;
import com.leon.biuvideo.adapters.UserDataAdapters.UserFavoriteFolderDetailAdapter;
import com.leon.biuvideo.beans.orderBeans.UserFolder;
import com.leon.biuvideo.beans.orderBeans.UserFolderData;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.UserFolderParser;
import com.leon.biuvideo.values.ImagePixelSize;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * PlayListFragment中的video片段
 */
public class UserOrderVideoFragment extends BaseFragment {
    private final long mid;

    private long nowFolderId;
    private int pageNum = 1;
    private boolean dataState = true;
    private UserFolderData userFolderData;
    private UserFolderParser userFolderParser;

    private SmartRefreshLayout fragment_favorite_smartRefresh;

    private RecyclerView folderList;
    private RecyclerView folderDetail;

    private UserFavoriteFolderDetailAdapter userFavoriteFolderDetailAdapter;

    public UserOrderVideoFragment(long mid) {
        this.mid = mid;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_user_order_video;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        folderList = findView(R.id.fragment_favorite_video_recyclerView_folderList);
        folderDetail = findView(R.id.fragment_favorite_video_recyclerView_folderDetail);

        fragment_favorite_smartRefresh = findView(R.id.fragment_favorite_smartRefresh);
        fragment_favorite_smartRefresh.setEnableRefresh(false);
    }

    @Override
    public void initValues() {
        userFolderParser = new UserFolderParser(context);
        List<UserFolder> userFolders = userFolderParser.parseUserFolder(mid);

        //获取第一个收藏夹ID
        nowFolderId = userFolders.get(0).id;
        userFolderData = userFolderParser.parseUserFolderData(nowFolderId, pageNum);

        // 设置收藏夹参数
        setFolderInfo(userFolderData);

        UserFavoriteFolderAdapter userFavoriteFolderAdapter = new UserFavoriteFolderAdapter(userFolders, context);
        userFavoriteFolderAdapter.setOnClickFolderListener(new UserFavoriteFolderAdapter.OnClickFolderListener() {
            @Override
            public void OnClick(long folderId) {
                pageNum = 1;
                dataState = true;
                nowFolderId = folderId;
                fragment_favorite_smartRefresh.setEnabled(true);

                UserFolderData innerUserFolderData = userFolderParser.parseUserFolderData(nowFolderId, pageNum);
                userFavoriteFolderDetailAdapter.reset(innerUserFolderData.medias);
                setFolderInfo(innerUserFolderData);
            }
        });

        userFavoriteFolderDetailAdapter = new UserFavoriteFolderDetailAdapter(userFolderData.medias, context);
        folderList.setAdapter(userFavoriteFolderAdapter);
        folderDetail.setAdapter(userFavoriteFolderDetailAdapter);

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
                    Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();

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
                                userFavoriteFolderDetailAdapter.append(userFolderData.medias);
                            }
                        }, 1000);
                    } else {
                        //关闭上滑刷新
                        fragment_favorite_smartRefresh.setEnabled(false);

                        Toast.makeText(context, "只有这么多数据了~~~", Toast.LENGTH_SHORT).show();
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
                .setText(R.id.fragment_favorite_video_textView_ctime, "创建于" + ValueFormat.generateTime(userFolderData.ctime, true, false, "/"));
    }

    /**
     * 获取收藏的视频
     */
    private void getFavoriteVideo() {
        pageNum++;
        userFolderData = userFolderParser.parseUserFolderData(nowFolderId, pageNum);

        if (userFolderData.medias.size() < 20) {
            dataState = false;
            fragment_favorite_smartRefresh.setEnabled(false);
        }
    }
}