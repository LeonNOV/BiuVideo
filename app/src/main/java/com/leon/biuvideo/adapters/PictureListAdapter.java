package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.ui.views.PictureViewer;
import com.leon.biuvideo.utils.ImagePixelSize;

import java.util.List;

/**
 * 相簿列表适配器
 */
public class PictureListAdapter extends BaseAdapter<String> {
    private final Context context;
    private final List<String> pictures;

    public PictureListAdapter(List<String> pictures, Context context) {
        super(pictures, context);

        this.context = context;
        this.pictures = pictures;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.picture_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ImagePixelSize imagePixelSize;

        //判断要显示的列数
        if (pictures.size() % 3 == 0) {
            imagePixelSize = ImagePixelSize.MORE;
        } else if (pictures.size() % 2 == 0) {
            imagePixelSize = ImagePixelSize.DOUBLE;
        } else {
            imagePixelSize = ImagePixelSize.SINGLE;
        }

        holder.setImage(R.id.picture_imageView_item, pictures.get(position), imagePixelSize)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //创建图片查看器
                        PictureViewer pictureViewer = new PictureViewer(context, position, pictures);
                        pictureViewer.showAtLocation(holder.getItemView(), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                });
    }
}
