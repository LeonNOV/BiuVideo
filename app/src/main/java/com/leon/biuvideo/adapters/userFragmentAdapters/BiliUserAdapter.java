package com.leon.biuvideo.adapters.userFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchBean.BiliUser;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.activitys.UserActivity;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;

import java.util.List;

/**
 * 搜索结果-用户列表适配器
 */
public class BiliUserAdapter extends BaseAdapter<BiliUser> {
    private final List<BiliUser> biliUsers;
    private final Context context;
    private final FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;

    public BiliUserAdapter(List<BiliUser> biliUsers, Context context, FavoriteUserDatabaseUtils favoriteUserDatabaseUtils) {
        super(biliUsers, context);
        this.biliUsers = biliUsers;
        this.context = context;
        this.favoriteUserDatabaseUtils = favoriteUserDatabaseUtils;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_bili_user_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BiliUser biliUser = biliUsers.get(position);

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //跳转至UpMasterActivity
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("mid", biliUser.mid);

                context.startActivity(intent);
            }
        });

        holder
                .setImage(R.id.search_bili_user_face, biliUser.face, ImagePixelSize.FACE)
                .setText(R.id.search_bili_user_textView_name, biliUser.name);

        boolean followState = favoriteUserDatabaseUtils.queryFavoriteState(biliUser.mid);

        if (followState) {
            holder.setText(R.id.search_bili_user_button_follow, "已关注");
        } else {
            holder.setText(R.id.search_bili_user_button_follow, "关注");
        }

        //设置按钮监听
        holder.setOnClickListener(R.id.search_bili_user_button_follow, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean followState = favoriteUserDatabaseUtils.queryFavoriteState(biliUser.mid);

                //已关注的话则进行移除
                if (followState) {
                    favoriteUserDatabaseUtils.removeFavorite(biliUser.mid);
                    holder.setText(R.id.search_bili_user_button_follow, "关注");

                    Snackbar.make(v, "已将'" + biliUser.name + "'从关注列表中移除", Snackbar.LENGTH_SHORT).show();
                } else {
                    Favorite favorite = new Favorite();
                    favorite.mid = biliUser.mid;
                    favorite.name = biliUser.name;
                    favorite.faceUrl = biliUser.face;
                    favorite.desc = biliUser.usign;

                    favoriteUserDatabaseUtils.addFavorite(favorite);
                    holder.setText(R.id.search_bili_user_button_follow, "已关注");

                    Snackbar.make(v, "已将'" + biliUser.name + "'添加至关注列表", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //设置稿件数
        String videos = "稿件：" + biliUser.videos;
        holder.setText(R.id.search_bili_user_textView_works, videos);

        //设置粉丝数
        String fans = "粉丝：" + ValueFormat.generateCN(biliUser.fans);
        holder
                .setText(R.id.search_bili_user_textView_fans, fans)
                .setText(R.id.search_bili_user_textView_sign, biliUser.usign);
    }
}