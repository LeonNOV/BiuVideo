package com.leon.biuvideo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.activitys.UserActivity;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.util.List;

/**
 * 已关注用户列表适配器
 */
public class FavoriteUserAdapter extends BaseAdapter<Favorite> {
    private final List<Favorite> favorites;
    private final Context context;

    private FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;

    public FavoriteUserAdapter(List<Favorite> favorites, Context context) {
        super(favorites, context);
        this.favorites = favorites;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.fragment_favorite_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);

        holder.setImage(R.id.favorite_circleImageView_face, favorite.faceUrl, ImagePixelSize.FACE)
                //设置昵称
                .setText(R.id.favorite_textView_name, favorite.name)

                //设置简介
                .setText(R.id.favorite_textView_desc, favorite.desc)

                //设置跳转监听,跳转到UpMasterActivity中
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                        if (!isHaveNetwork) {
                            Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(context, UserActivity.class);
                        intent.putExtra("mid", favorite.mid);
                        context.startActivity(intent);
                    }
                })

                //设置'取消关注'按钮监听
                .setOnClickListener(R.id.favorite_imageView_cancel_favoriteIcon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.FavoriteUp);
                        favoriteUserDatabaseUtils = (FavoriteUserDatabaseUtils) sqLiteHelperFactory.getInstance();
                        favoriteUserDatabaseUtils.removeFavorite(favorites.get(position).mid);

                        favorites.remove(position);
                        notifyItemRemoved(position);
                    }
                });
    }

    /**
     * 加载数据使用
     */
    public void refresh(List<Favorite> addOns) {
        //清空原有数据
        if (addOns.size() > 0) {
            if (favorites.size() > 0) {
                favorites.clear();
            }

            favorites.addAll(addOns);
            notifyDataSetChanged();
        }
    }
}