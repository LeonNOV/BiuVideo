package com.leon.biuvideo.adapters.homeAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.userBeans.Follow;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.Role;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

/**
 * @Author Leon
 * @Time 2020/10/25
 * @Desc 关注数据适配器
 */
public class FollowsAdapter extends BaseAdapter<Follow> {
    private final MainActivity mainActivity;
    private final boolean isBiliUser;

    public FollowsAdapter(MainActivity mainActivity, Context context, boolean isBiliUser) {
        super(context);
        this.mainActivity = mainActivity;
        this.isBiliUser = isBiliUser;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.follow_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Follow follow = getAllData().get(position);

        ((SwipeMenuLayout) holder.findById(R.id.follow_item_swipeLayout)).setSwipeEnable(!isBiliUser);

        ImageView followItemVerifyMark = holder.findById(R.id.follow_item_verify_mark);
        if (follow.role == Role.NONE) {
            if (follow.vipStatus) {
                followItemVerifyMark.setVisibility(View.VISIBLE);
                followItemVerifyMark.setImageResource(R.drawable.ic_vip_mark);
            } else {
                followItemVerifyMark.setVisibility(View.GONE);
            }
        } else {
            followItemVerifyMark.setVisibility(View.VISIBLE);
            followItemVerifyMark.setImageResource(follow.role == Role.PERSON ? R.drawable.ic_person_verify : R.drawable.ic_official_verify);
        }

        TextView followsItemName = holder.findById(R.id.follow_item_name);
        followsItemName.setText(follow.userName);
        if (follow.vipStatus) {
            followsItemName.setTextColor(context.getColor(R.color.BiliBili_pink));
        } else {
            followsItemName.setTextColor(context.getColor(R.color.black));
        }

        holder
                .setImage(R.id.follow_item_face, follow.userFace, ImagePixelSize.FACE)
                .setText(R.id.follow_item_sign, follow.sign)
                .setOnClickListener(R.id.follow_item_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            startPublicFragment(mainActivity, FragmentType.BILI_USER, String.valueOf(follow.followerMid));
                        }
                    }
                });

        if (!isBiliUser) {
            TextView followItemOperation = holder.findById(R.id.follow_item_operation);
            if (follow.userStatus == 0) {
                followItemOperation.setText(R.string.cancel_follow);
            } else if (follow.userStatus == 6) {
                followItemOperation.setText(R.string.is_mutual_follower);
            }
            followItemOperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 取消关注操作
                    SimpleSnackBar.make(v, context.getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG).show();
                }
            });
        }
    }
}