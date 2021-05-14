package com.leon.biuvideo.ui.resourcesFragment.picture;

import android.os.Message;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.PictureListAdapter;
import com.leon.biuvideo.beans.resourcesBeans.PictureDetail;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.layoutManager.PictureGridLayoutManager;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.PictureDetailParser;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Leon
 * @Time 2021/4/12
 * @Desc 相簿页面
 */
public class PictureFragment extends BaseSupportFragment {
    public final String rid;
    private TextView pictureUserFollow;
    private LoadingRecyclerView pictureData;
    private TagView pictureComment;
    private TagView pictureLike;

    public PictureFragment(String rid) {
        this.rid = rid;
    }

    @Override
    protected int setLayout() {
        return R.layout.picture_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.picture_container).setBackgroundResource(R.color.white);
        setTopBar(R.id.picture_topBar);

        pictureUserFollow = findView(R.id.picture_user_follow);
        pictureData = findView(R.id.picture_data);

        pictureComment = findView(R.id.picture_comment);
        pictureLike = findView(R.id.picture_like);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                PictureDetail pictureDetail = (PictureDetail) msg.obj;
                if (pictureDetail != null) {
                    new BindingUtils(view, context)
                            .setImage(R.id.picture_face, pictureDetail.userFace, ImagePixelSize.FACE)
                            .setText(R.id.picture_user_name, pictureDetail.userName)
                            .setText(R.id.picture_pubTime, pictureDetail.pubTime)
                            .setText(R.id.picture_content, pictureDetail.desc);

                    pictureUserFollow.setSelected(pictureDetail.isFollow);
                    if (pictureDetail.isFollow) {
                        pictureUserFollow.setText("已关注");
                    } else {
                        pictureUserFollow.setText("关注");
                    }

                    pictureComment.setRightValue(pictureDetail.comment);
                    pictureLike.setRightValue(pictureDetail.like);

                    pictureData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

                    List<String[]> pictures = new ArrayList<>(pictureDetail.picturesCount);
                    Set<Map.Entry<Integer, String[]>> entries = pictureDetail.pictures.entrySet();
                    for (Map.Entry<Integer, String[]> entry : entries) {
                        pictures.add(entry.getValue());
                    }

                    int spanCount = 0;
                    //判断要显示的列数
                    if (pictureDetail.picturesCount % 3 == 0) {
                        spanCount = 3;
                    } else if (pictureDetail.picturesCount % 2 == 0) {
                        spanCount = 2;
                    } else {
                        spanCount = 1;
                    }

                    PictureListAdapter pictureListAdapter = new PictureListAdapter(pictures, context);
                    pictureData.setRecyclerViewAdapter(pictureListAdapter);
                    pictureData.setRecyclerViewLayoutManager( new PictureGridLayoutManager(context, spanCount));

                    pictureData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                }
            }
        });

        getPictureDetail();
    }

    private void getPictureDetail () {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                PictureDetail pictureDetail = PictureDetailParser.parseData(rid);

                Message message = receiveDataHandler.obtainMessage();
                message.obj = pictureDetail;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
