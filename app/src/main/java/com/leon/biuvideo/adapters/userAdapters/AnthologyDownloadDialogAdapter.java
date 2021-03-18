package com.leon.biuvideo.adapters.userAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.videoBean.view.AnthologyInfo;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.PreferenceUtils;

import java.util.List;

/**
 * 缓存视频选集时用到的选集列表适配器
 */
public class AnthologyDownloadDialogAdapter extends BaseAdapter<AnthologyInfo> {
    private final List<AnthologyInfo> anthologyInfoList;
    private final Context context;

    public AnthologyDownloadDialogAdapter(List<AnthologyInfo> anthologyInfoList, Context context) {
        super(anthologyInfoList, context);
        this.anthologyInfoList = anthologyInfoList;
        this.context = context;
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

        if (anthologyInfo.badge != null) {
            holder.setText(R.id.anthology_download_item_textView_badge, anthologyInfo.badge);
        } else {
            holder.setVisibility(R.id.anthology_download_item_textView_badge, View.GONE);
        }

        holder
                .setVisibility(R.id.anthology_download_item_textView_isDownloaded, anthologyInfo.isDownloaded ? View.VISIBLE : View.GONE)
                .setText(R.id.anthology_download_item_textView_name, anthologyInfo.part)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (anthologyInfo.isVIP) {
                            // 查询当前用户是否为大会员
                            if (!PreferenceUtils.getVipStatus()) {
                                SimpleSnackBar.make(v, "该视频需要成为大会员才能下载", SimpleSnackBar.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (anthologyInfo.isDownloaded) {
                            SimpleSnackBar.make(v, "该视频已下载过了哦~", SimpleSnackBar.LENGTH_SHORT).show();
                        } else {
                            if (onAnthologyItemClickListener != null) {
                                onAnthologyItemClickListener.onItemClickListener(anthologyInfo.cid, position, anthologyInfo.part);
                            }
                        }
                    }
                });
    }
}
