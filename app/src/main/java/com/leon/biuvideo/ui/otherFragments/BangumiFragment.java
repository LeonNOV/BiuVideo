package com.leon.biuvideo.ui.otherFragments;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.mediaBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.BangumiDetailParser;

/**
 * @Author Leon
 * @Time 2021/3/21
 * @Desc
 */
public class BangumiFragment extends BaseSupportFragment {
    private final String seasonId;

    public BangumiFragment(String seasonId) {
        this.seasonId = seasonId;
    }

    @Override
    protected int setLayout() {
        return R.layout.bangumi_fragment;
    }

    @Override
    protected void initView() {
        ImageView bangumiCover = findView(R.id.bangumi_cover);
        ProgressBar bangumiProgress = findView(R.id.bangumi_progress);

        getBangumiDetail();

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                Bangumi bangumi = (Bangumi) msg.obj;
                Glide.with(context).load(bangumi.cover).into(bangumiCover);
                bangumiProgress.setVisibility(View.GONE);
            }
        });
    }

    private void getBangumiDetail() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                BangumiDetailParser bangumiDetailParser = new BangumiDetailParser(seasonId);
                Bangumi bangumi = bangumiDetailParser.parseData();

                Message message = receiveDataHandler.obtainMessage();
                message.obj = bangumi;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
