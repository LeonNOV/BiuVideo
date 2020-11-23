package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.BiliUser;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.ui.activitys.UpMasterActivity;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.dataBaseUtils.Tables;

import java.util.List;

public class BiliUserAdapter extends BaseAdapter<BiliUser> {
    private List<BiliUser> biliUsers;
    private Context context;

    private SQLiteHelperFactory sqLiteHelperFactory;
    private FavoriteDatabaseUtils favoriteDatabaseUtils;

    public BiliUserAdapter(List<BiliUser> biliUsers, Context context) {
        super(biliUsers, context);
        this.biliUsers = biliUsers;
        this.context = context;
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
                //跳转至UpMasterActivity
                Intent intent = new Intent(context, UpMasterActivity.class);
                intent.putExtra("mid", biliUser.mid);

                context.startActivity(intent);
            }
        });

        holder.setImage(R.id.search_bili_user_face, biliUser.face, ImagePixelSize.FACE)
                .setText(R.id.search_bili_user_textView_name, biliUser.name);

        //查询对应用户是否存在于Favorite_up中
        sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.FavoriteUp);
        favoriteDatabaseUtils = (FavoriteDatabaseUtils) sqLiteHelperFactory.getInstance();
        boolean state = favoriteDatabaseUtils.queryFavoriteState(biliUser.mid);

        if (state) {
            holder.setText(R.id.search_bili_user_button_follow, "已关注");
        } else {
            holder.setText(R.id.search_bili_user_button_follow, "关注");
        }

        favoriteDatabaseUtils.close();

        //设置按钮监听
        holder.setOnClickListener(R.id.search_bili_user_button_follow, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.FavoriteUp);
                favoriteDatabaseUtils = (FavoriteDatabaseUtils) sqLiteHelperFactory.getInstance();

                //已关注的话则进行移除
                if (state) {
                    favoriteDatabaseUtils.removeFavorite(biliUser.mid);
                    holder.setText(R.id.search_bili_user_button_follow, "已关注");

                    Toast.makeText(context, "已将'" + biliUser.name + "'从关注列表中移除", Toast.LENGTH_SHORT).show();
                } else {
                    Favorite favorite = new Favorite();
                    favorite.mid = biliUser.mid;
                    favorite.name = biliUser.name;
                    favorite.faceUrl = biliUser.face;
                    favorite.desc = biliUser.usign;

                    favoriteDatabaseUtils.addFavorite(favorite);
                    holder.setText(R.id.search_bili_user_button_follow, "关注");

                    Toast.makeText(context, "已将'" + biliUser.name + "'添加至关注列表", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //设置稿件数
        String videos = "稿件：" + biliUser.videos;
        holder.setText(R.id.search_bili_user_textView_works, videos);

        //设置粉丝数
        String fans = "粉丝：" + ValueFormat.generateCN(biliUser.fans);
        holder.setText(R.id.search_bili_user_textView_fans, fans)
                .setText(R.id.search_bili_user_textView_sign, biliUser.usign);
    }

    //加载数据使用
    public void refresh(List<BiliUser> addOns) {
        int position = biliUsers.size();

        biliUsers.addAll(position, addOns);
        notifyDataSetChanged();
    }
}
