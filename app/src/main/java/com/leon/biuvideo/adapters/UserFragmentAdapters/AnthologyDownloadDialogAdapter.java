package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.videoBean.view.AnthologyInfo;

import java.util.List;

/**
 * 缓存视频选集时用到的选集列表适配器
 */
public class AnthologyDownloadDialogAdapter extends BaseAdapter<AnthologyInfo> {
    private final List<AnthologyInfo> anthologyInfoList;

    public AnthologyDownloadDialogAdapter(List<AnthologyInfo> anthologyInfoList, Context context) {
        super(anthologyInfoList, context);
        this.anthologyInfoList = anthologyInfoList;
    }

    private OnAnthologyItemClickListener onAnthologyItemClickListener;

    public interface OnAnthologyItemClickListener {
        /**
         * 缓存已点击的选集
         *
         * @param cid   选集cid
         * @param position  选集position
         * @param subTitle  选集标题
         */
        void onItemClickListener(long cid, int position, String subTitle);
    }

    public void setOnAnthologyItemClickListener(OnAnthologyItemClickListener onAnthologyItemClickListener) {
        this.onAnthologyItemClickListener = onAnthologyItemClickListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.anthology_download_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        AnthologyInfo anthologyInfo = anthologyInfoList.get(position);

        holder.setText(R.id.anthology_download_item_textView_name, anthologyInfo.part)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onAnthologyItemClickListener != null) {
                            onAnthologyItemClickListener.onItemClickListener(anthologyInfo.cid, position, anthologyInfo.part);
                        }
                    }
                });
    }
}
