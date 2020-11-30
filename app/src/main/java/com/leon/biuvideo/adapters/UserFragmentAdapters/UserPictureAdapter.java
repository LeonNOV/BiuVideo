package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.upMasterBean.Picture;
import com.leon.biuvideo.ui.activitys.PictureActivity;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 用户界面，相簿fragment适配器
 */
public class UserPictureAdapter extends BaseAdapter<Picture> {
    private List<Picture> pictures;
    private final Context context;

    public UserPictureAdapter(List<Picture> pictures, Context context) {
        super(pictures, context);
        this.pictures = pictures;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_picture_list_view_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Picture picture = pictures.get(position);

        //设置封面
        holder.setImage(R.id.up_picture_imageView_cover, picture.pictures.get(0), ImagePixelSize.COVER);//设置相簿封面，即pics中的第一个

        //设置相簿count
        //总数大于2则进行显示
        if (picture.count > 1) {
            String p = picture.count + "P";
            holder.setText(R.id.up_picture_textView_count, p);
        } else {
            holder.setVisibility(R.id.up_picture_textView_count, View.INVISIBLE);
        }

        //设置标题
        holder.setText(R.id.up_picture_textView_title, picture.title)

                //设置查看次数
                .setText(R.id.up_picture_textView_view, ValueFormat.generateCN(picture.view))

                //设置时间
                .setText(R.id.up_picture_textView_ctime, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(picture.ctime * 1000)))

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                        if (!isHaveNetwork) {
                            Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //跳转到PictureActivity
                        Intent intent = new Intent(context, PictureActivity.class);

                        //传递整个UpPicture对象
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("picture", picture);
                        intent.putExtras(bundle);

                        context.startActivity(intent);
                    }
                });
    }

    //加载数据使用
    public void refresh(List<Picture> addOns) {
        int position = pictures.size();

        pictures.addAll(position, addOns);

        notifyDataSetChanged();
    }
}