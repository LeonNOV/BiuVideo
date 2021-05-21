package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.ui.views.PictureViewer;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/13
 * @Desc 图片列表适配器
 */
public class PictureListAdapter extends BaseAdapter<String[]> {
    private final List<String[]> pictureUrls;

    public PictureListAdapter(List<String[]> beans, Context context) {
        super(beans, context);
        this.pictureUrls = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.picture_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ImagePixelSize imagePixelSize;

        //判断要显示的列数
        if (pictureUrls.size() % 3 == 0) {
            imagePixelSize = ImagePixelSize.MORE;
        } else if (pictureUrls.size() % 2 == 0) {
            imagePixelSize = ImagePixelSize.DOUBLE;
        } else {
            imagePixelSize = ImagePixelSize.SINGLE;
        }

        holder
                .setImage(R.id.picture_list_item, pictureUrls.get(position)[0], imagePixelSize)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            //创建图片查看器
                            PictureViewer pictureViewer = new PictureViewer(context, position, pictureUrls);
                            pictureViewer.showAtLocation(holder.getItemView(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                        }
                    }
                });
    }
}
