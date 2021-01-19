package com.leon.biuvideo.adapters.localOrderAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * 本地订阅-显示视频文件夹中的所有视频
 */
public class LocalOrderVideoDetailAdapter extends BaseAdapter<LocalOrder> {
    private final Context context;
    private final List<LocalOrder> localOrderList;

    public LocalOrderVideoDetailAdapter(Context context, List<LocalOrder> localOrderList) {
        super(localOrderList, context);

        this.context = context;
        this.localOrderList = localOrderList;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.favorite_video_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        LocalOrder localOrder = localOrderList.get(position);
        JSONObject jsonObject = localOrder.jsonObject;

        holder
                .setImage(R.id.favorite_video_imageView_cover, jsonObject.getString("cover"), ImagePixelSize.COVER)
                .setText(R.id.favorite_video_textView_duration, ValueFormat.lengthGenerate(jsonObject.getIntValue("duration")))
                .setText(R.id.favorite_video_textView_title, jsonObject.getString("title"))
                .setText(R.id.favorite_video_textView_ftime, "收藏于" + ValueFormat.generateTime(localOrder.addTime, false, false, "/"))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("bvid", localOrder.mainId);
                        context.startActivity(intent);
                    }
                });
    }

    /**
     * 重置数据
     *
     * @param addOns    新添加的数据
     */
    public void reset(List<LocalOrder> addOns) {
        if (localOrderList != null) {
            this.localOrderList.clear();
            this.localOrderList.addAll(addOns);

            notifyDataSetChanged();
        }
    }
}
