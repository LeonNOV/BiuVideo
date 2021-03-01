package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.videoBean.view.AnthologyInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;

import java.util.List;

/**
 * videoActivity播放列表控件的适配器
 */
public class AnthologyAdapter extends BaseAdapter<AnthologyInfo> {
    private final List<AnthologyInfo> anthologyInfos;
    private final Context context;

    //当前webView中播放的选集索引，默认为0
    private int anthologySelectedIndex = 0;

    public AnthologyAdapter(ViewPage viewPage, Context context) {
        super(viewPage.anthologyInfoList, context);
        this.anthologyInfos = viewPage.anthologyInfoList;
        this.context = context;
    }

    private OnClickAnthologyListener onClickAnthologyListener;

    public interface OnClickAnthologyListener {
        void onClick(int position, long cid);
    }

    public void setOnClickAnthologyListener(OnClickAnthologyListener onClickAnthologyListener) {
        this.onClickAnthologyListener = onClickAnthologyListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.single_video_listview_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        AnthologyInfo anthologyInfo = anthologyInfos.get(position);

        holder
                //设置选集序号
                .setText(R.id.anthology_item_textView_index, "P" + (position + 1))

                .setText(R.id.anthology_item_textView_duration, ValueUtils.lengthGenerate(anthologyInfo.duration))

                //设置选集标题
                .setText(R.id.anthology_item_textView_title, anthologyInfo.part)

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!InternetUtils.checkNetwork(context)) {
                            SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        //判断当前观看的视频cid是否和选择的一样
                        if (anthologySelectedIndex != position) {
                            if (onClickAnthologyListener != null) {
                                anthologySelectedIndex = position;
                                onClickAnthologyListener.onClick(anthologySelectedIndex, anthologyInfo.cid);
                            }
                        } else {
                            SimpleSnackBar.make(v, "选择的视频已经在播放了~", SimpleSnackBar.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
