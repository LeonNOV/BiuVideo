package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.videoBean.view.SingleVideoInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.WebViewUtils;
import com.leon.biuvideo.utils.mediaParseUtils.MediaParseUtils;

import java.util.List;

/**
 * videoActivity播放列表控件的适配器
 */
public class AnthologyAdapter extends BaseAdapter<SingleVideoInfo> {
    private final ViewPage viewPage;
    private final List<SingleVideoInfo> singleVideoInfos;
    private final Context context;

    private final WebView webView;
    private WebViewUtils webViewUtils;

    //当前webView中播放的选集索引，默认为0
    private int singleVideoSelectedIndex = 0;

    public AnthologyAdapter(ViewPage viewPage, Context context, WebView webView) {
        super(viewPage.singleVideoInfoList, context);
        this.viewPage = viewPage;
        this.singleVideoInfos = viewPage.singleVideoInfoList;
        this.context = context;
        this.webView = webView;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.single_video_listview_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SingleVideoInfo singleVideoInfo = singleVideoInfos.get(position);

        //设置选集封面
        holder.setImage(R.id.single_video_item_imageView_cover, viewPage.coverUrl, ImagePixelSize.COVER)

                //设置选集序号
                .setText(R.id.single_video_item_textView_index, "P" + (position + 1))

                //设置选集标题
                .setText(R.id.single_video_item_textView_title, singleVideoInfo.part)

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        webViewUtils = new WebViewUtils(webView);

                        //判断当前观看的视频cid是否和选择的一样
                        if (singleVideoSelectedIndex != position) {
                            //设置webView的链接
                            webViewUtils.setWebViewUrl(viewPage.aid, singleVideoInfo.cid, position);

                            //重置nowPosition
                            singleVideoSelectedIndex = position;

                            //重置当前play变量
                            VideoActivity.play = MediaParseUtils.parseMedia(viewPage.bvid, viewPage.aid, singleVideoInfo.cid);
                        } else {
                            Toast.makeText(context, "选择的视频已经在播放了~~", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
