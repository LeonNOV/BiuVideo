package com.leon.biuvideo.adapters.home;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.Follow;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;

import java.util.List;

/**
 * 已关注用户列表适配器
 */
public class MyFollowsAdapter extends BaseAdapter<Follow> {
    private final List<Follow> follows;
    private final Context context;

    private FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;

    public MyFollowsAdapter(List<Follow> follows, Context context) {
        super(follows, context);
        this.follows = follows;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.my_follow_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Follow follow = follows.get(position);
        Fuck.blue(position + "");
        holder
                .setOnClickListener(R.id.my_follow_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "点击了第" + position + "个item", Toast.LENGTH_SHORT).show();
                    }
                })
                .setText(R.id.my_follow_item_name, follow.name)
                .setText(R.id.my_follow_item_desc, follow.desc)
                .setOnClickListener(R.id.my_follow_item_cancel_follow, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "删除第" + position + "个item", Toast.LENGTH_SHORT).show();
                    }
                });

        /*
        holder.setImage(R.id.favorite_circleImageView_face, follow.faceUrl, ImagePixelSize.FACE)
                //设置昵称
                .setText(R.id.favorite_textView_name, follow.name)

                //设置简介
                .setText(R.id.favorite_textView_desc, follow.desc)

                //设置跳转监听,跳转到UpMasterActivity中
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        if (!InternetUtils.checkNetwork(context)) {
                            SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(context, UserActivity.class);
                        intent.putExtra("mid", follow.mid);
                        context.startActivity(intent);
                    }
                })

                //设置'取消关注'按钮监听
                .setOnClickListener(R.id.favorite_button_cancel_favorite, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (favoriteUserDatabaseUtils == null) {
                            favoriteUserDatabaseUtils = new FavoriteUserDatabaseUtils(context);
                        }

                        favoriteUserDatabaseUtils.removeFavorite(follow.mid);
                        remove(follow);
                    }
                });

         */
    }

    /**
     * 加载数据使用
     */
    public void refresh(List<Follow> addOns) {
        if (addOns.size() > 0) {
            //清空原有数据
            removeAll();
            append(addOns);
        }
    }
}