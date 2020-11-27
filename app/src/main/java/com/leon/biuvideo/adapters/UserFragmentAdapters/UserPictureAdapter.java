package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.ui.activitys.PictureActivity;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.ValueFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 用户界面，相簿fragment适配器
 */
public class UserPictureAdapter extends BaseAdapter<UpPicture> {
    private List<UpPicture> upPictures;
    private final Context context;

    public UserPictureAdapter(List<UpPicture> upPictures, Context context) {
        super(upPictures, context);
        this.upPictures = upPictures;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_picture_list_view_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        UpPicture upPicture = upPictures.get(position);

        //设置封面
        holder.setImage(R.id.up_picture_imageView_cover, upPicture.pictures.get(0), ImagePixelSize.COVER);//设置相簿封面，即pics中的第一个

        //设置相簿count
        //总数大于2则进行显示
        if (upPicture.count > 1) {
            String p = upPicture.count + "P";
            holder.setText(R.id.up_picture_textView_count, p);
        } else {
            holder.setVisibility(R.id.up_picture_textView_count, View.INVISIBLE);
        }

        Fuck.blue("标题:" + upPicture.description + "---" + "第" + position + "个相簿的图片个数为" + upPicture.count + "个");

        //设置标题
        holder.setText(R.id.up_picture_textView_desc, upPicture.description)

                //设置查看次数
                .setText(R.id.up_picture_textView_view, ValueFormat.generateCN(upPicture.view))

                //设置时间
                .setText(R.id.up_picture_textView_ctime, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(upPicture.ctime * 1000)))

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到PictureActivity
                        Intent intent = new Intent(context, PictureActivity.class);

                        //传递整个UpPicture对象
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("picture", upPicture);
                        intent.putExtras(bundle);

                        context.startActivity(intent);
                    }
                });
    }

    //加载数据使用
    public void refresh(List<UpPicture> addOns) {
        int position = upPictures.size();

        upPictures.addAll(position, addOns);

        notifyDataSetChanged();
    }
}