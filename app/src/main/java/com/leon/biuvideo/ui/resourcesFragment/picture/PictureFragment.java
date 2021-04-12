package com.leon.biuvideo.ui.resourcesFragment.picture;

import android.os.Message;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.PictureDetail;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.PictureDetailParser;
import com.leon.biuvideo.values.ImagePixelSize;

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
        SimpleTopBar pictureTopBar = findView(R.id.picture_topBar);
        pictureTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

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
