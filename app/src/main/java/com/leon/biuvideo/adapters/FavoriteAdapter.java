package com.leon.biuvideo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.activitys.UpMasterActivity;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.SQLiteHelper;
import com.leon.biuvideo.utils.dataUtils.FavoriteDatabaseUtils;
import com.leon.biuvideo.utils.dataUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.dataUtils.Tables;

import java.util.List;

/**
 * 收藏fragment适配器
 */
public class FavoriteAdapter extends BaseAdapter<Favorite> {
    private List<Favorite> favorites;
    private Context context;

    private FavoriteDatabaseUtils favoriteDatabaseUtils;

    public FavoriteAdapter(List<Favorite> favorites, Context context) {
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
                        Intent intent = new Intent(context, UpMasterActivity.class);
                        intent.putExtra("mid", favorite.mid);
                        context.startActivity(intent);
                    }
                })

                //设置'取消关注'按钮监听
                .setOnClickListener(R.id.favorite_imageView_cancel_favoriteIcon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.FavoriteUp);
                        favoriteDatabaseUtils = (FavoriteDatabaseUtils) sqLiteHelperFactory.getInstance();
                        favoriteDatabaseUtils.removeFavorite(favorites.get(position).mid);

                        favorites.remove(position);
                        notifyItemRemoved(position);
                    }
                });
    }

    //加载数据使用
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