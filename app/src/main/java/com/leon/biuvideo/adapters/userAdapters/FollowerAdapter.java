package com.leon.biuvideo.adapters.userAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.userBeans.Follower;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.Role;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/18
 * @Desc 粉丝数据适配器
 */
public class FollowerAdapter extends BaseAdapter<Follower> {
    private final List<Follower> followers;

    public FollowerAdapter(List<Follower> beans, Context context) {
        super(beans, context);
        this.followers = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.followers_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Follower follower = followers.get(position);

        ImageView followersItemVerifyMark = holder.findById(R.id.followers_item_verify_mark);
        if (follower.role == Role.NONE) {
            if (follower.vipStatus) {
                followersItemVerifyMark.setVisibility(View.VISIBLE);
                followersItemVerifyMark.setImageResource(R.drawable.ic_vip_mark);
            } else {
                followersItemVerifyMark.setVisibility(View.GONE);
            }
        } else {
            followersItemVerifyMark.setVisibility(View.VISIBLE);
            followersItemVerifyMark.setImageResource(follower.role == Role.PERSON ? R.drawable.ic_person_verify : R.drawable.ic_official_verify);
        }

        TextView followersItemName = holder.findById(R.id.followers_item_name);
        followersItemName.setText(follower.userName);
        if (follower.vipStatus) {
            followersItemName.setTextColor(context.getColor(R.color.BiliBili_pink));
        } else {
            followersItemName.setTextColor(context.getColor(R.color.black));
        }

        holder
                .setImage(R.id.followers_item_face, follower.userFace, ImagePixelSize.FACE)
                .setText(R.id.followers_item_sign, follower.sign)
                .setOnClickListener(R.id.followers_item_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(view, "点击了第" + position + "个Item,followerName：" + follower.userName, SimpleSnackBar.LENGTH_SHORT).show();
                    }
                })
                .setOnClickListener(R.id.followers_item_remove_follower, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(view, "移除粉丝：" + follower.userName, SimpleSnackBar.LENGTH_SHORT).show();
                    }
                });

        TextView followersItemMutualFollower = holder.findById(R.id.followers_item_mutual_follower);
        if (follower.userStatus == 0) {
            followersItemMutualFollower.setText(R.string.follow);
        } else if (follower.userStatus == 6) {
            followersItemMutualFollower.setText(R.string.is_mutual_follower);
        }
        followersItemMutualFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follower.userStatus == 0) {
                    followersItemMutualFollower.setText(R.string.is_mutual_follower);
                } else {
                    // 取消互粉操作
                    SimpleSnackBar.make(view, "取消互粉：" + follower.userName, SimpleSnackBar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
