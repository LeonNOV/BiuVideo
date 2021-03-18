package com.leon.biuvideo.adapters.userAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.Follow;
import com.leon.biuvideo.beans.searchBean.SearchBiliUser;
import com.leon.biuvideo.ui.activitys.UserActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;

import java.util.List;

/**
 * 搜索结果-用户列表适配器
 */
public class BiliUserAdapter extends BaseAdapter<SearchBiliUser> {
    private final List<SearchBiliUser> searchBiliUsers;
    private final Context context;
    private final FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;

    public BiliUserAdapter(List<SearchBiliUser> searchBiliUsers, Context context, FavoriteUserDatabaseUtils favoriteUserDatabaseUtils) {
        super(searchBiliUsers, context);
        this.searchBiliUsers = searchBiliUsers;
        this.context = context;
        this.favoriteUserDatabaseUtils = favoriteUserDatabaseUtils;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_bili_user_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SearchBiliUser searchBiliUser = searchBiliUsers.get(position);

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                    return;
                }

                //跳转至UpMasterActivity
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("mid", searchBiliUser.mid);

                context.startActivity(intent);
            }
        });

        holder
                .setImage(R.id.search_bili_user_face, searchBiliUser.face, ImagePixelSize.FACE)
                .setText(R.id.search_bili_user_textView_name, searchBiliUser.name);

        boolean followState = favoriteUserDatabaseUtils.queryFavoriteState(searchBiliUser.mid);

        if (followState) {
            holder.setText(R.id.search_bili_user_button_follow, "已关注");
        } else {
            holder.setText(R.id.search_bili_user_button_follow, "关注");
        }

        //设置按钮监听
        holder.setOnClickListener(R.id.search_bili_user_button_follow, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean followState = favoriteUserDatabaseUtils.queryFavoriteState(searchBiliUser.mid);

                //已关注的话则进行移除
                if (followState) {
                    favoriteUserDatabaseUtils.removeFavorite(searchBiliUser.mid);
                    holder.setText(R.id.search_bili_user_button_follow, "关注");

                    SimpleSnackBar.make(v, "已将'" + searchBiliUser.name + "'从关注列表中移除", SimpleSnackBar.LENGTH_SHORT).show();
                } else {
                    Follow follow = new Follow();
                    follow.mid = searchBiliUser.mid;
                    follow.name = searchBiliUser.name;
                    follow.faceUrl = searchBiliUser.face;
                    follow.desc = searchBiliUser.usign;

                    favoriteUserDatabaseUtils.addFavorite(follow);
                    holder.setText(R.id.search_bili_user_button_follow, "已关注");

                    SimpleSnackBar.make(v, "已将'" + searchBiliUser.name + "'添加至关注列表", SimpleSnackBar.LENGTH_SHORT).show();
                }
            }
        });

        //设置稿件数
        String videos = "稿件：" + searchBiliUser.videos;
        holder.setText(R.id.search_bili_user_textView_works, videos);

        //设置粉丝数
        String fans = "粉丝：" + ValueUtils.generateCN(searchBiliUser.fans);
        holder
                .setText(R.id.search_bili_user_textView_fans, fans)
                .setText(R.id.search_bili_user_textView_sign, searchBiliUser.usign);
    }
}
