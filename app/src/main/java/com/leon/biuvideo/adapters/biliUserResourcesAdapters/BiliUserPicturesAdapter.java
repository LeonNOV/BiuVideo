package com.leon.biuvideo.adapters.biliUserResourcesAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserPicture;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

/**
 * @Author Leon
 * @Time 2021/4/10
 * @Desc B站用户相簿数据适配器
 */
public class BiliUserPicturesAdapter extends BaseAdapter<BiliUserPicture> {
    public BiliUserPicturesAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.bili_user_picture_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BiliUserPicture biliUserPicture = getAllData().get(position);

        TextView biliUserPictureItemCount = holder.findById(R.id.bili_user_picture_item_count);
        if (biliUserPicture.count == 1) {
            biliUserPictureItemCount.setVisibility(View.GONE);
        } else {
            biliUserPictureItemCount.setText(biliUserPicture.count + "P");
        }

        holder
                .setImage(R.id.bili_user_picture_item_cover, biliUserPicture.cover, ImagePixelSize.COVER)
                .setText(R.id.bili_user_picture_item_desc, biliUserPicture.desc)
                .setText(R.id.bili_user_picture_item_view, biliUserPicture.view)
                .setText(R.id.bili_user_picture_item_like, biliUserPicture.like)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            startPublicFragment(FragmentType.PICTURE, biliUserPicture.id);
                        }
                    }
                });
    }
}
