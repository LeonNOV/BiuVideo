package com.leon.biuvideo.ui.mainFragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.otherFragments.LoginFragment;
import com.leon.biuvideo.ui.user.UserInfoFragment;
import com.leon.biuvideo.ui.views.CardTitle;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 用户页面
 */
public class UserFragment extends BaseSupportFragment implements View.OnClickListener {
    @Override
    protected int setLayout() {
        return R.layout.user_fragment;
    }

    @Override
    protected void initView() {
        CircleImageView userFace = findView(R.id.user_face);
        userFace.setOnClickListener(this);

        ImageView userVipMark = findView(R.id.user_vip_mark);
        LinearLayout userBaseInfoLinearLayout = findView(R.id.user_baseInfo_linearLayout);
        LinearLayout userAccountInfoLinearLayout = findView(R.id.user_accountInfo_linearLayout);
        RecyclerView userFavoriteRecyclerView = findView(R.id.user_favorite_recyclerView);
        RecyclerView userBangumiRecyclerView = findView(R.id.user_bangumi_recyclerView);
        RecyclerView userCoinRecyclerView = findView(R.id.user_coin_recyclerView);

        TextView userInfo = findView(R.id.user_info);
        userInfo.setOnClickListener(this);

        CardTitle userFavoritesCardTitle = findView(R.id.user_favorite_cardTitle);
        userFavoritesCardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(FavoritesFragment.getInstance());
            }
        });

        CardTitle userBangumiCardTitle = findView(R.id.user_bangumi_cardTitle);
        userBangumiCardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                Toast.makeText(context, "订阅的番剧", Toast.LENGTH_SHORT).show();
            }
        });

        CardTitle userCoinCardTitle = findView(R.id.user_coin_cardTitle);
        userCoinCardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                Toast.makeText(context, "最近投币的视频", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info:
                ((NavFragment) getParentFragment()).startBrotherFragment(UserInfoFragment.getInstance());
                break;
            case R.id.user_face:
                ((NavFragment) getParentFragment()).startBrotherFragment(LoginFragment.getInstance());
                break;
            default:
                break;
        }
    }
}
