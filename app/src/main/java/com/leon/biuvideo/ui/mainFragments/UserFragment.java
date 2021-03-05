package com.leon.biuvideo.ui.mainFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.user.UserInfoFragment;
import com.leon.biuvideo.ui.views.CardTitle;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 用户页面
 */
public class UserFragment extends SupportFragment implements View.OnClickListener {
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        context = getContext();

        initView(view);

        return view;
    }

    private void initView(View view) {
        TextView user_fragment_info = view.findViewById(R.id.user_fragment_info);
        user_fragment_info.setOnClickListener(this);

        CardTitle user_favorites_cardTitle = view.findViewById(R.id.user_favorites_cardTitle);
        user_favorites_cardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                ((NavFragment) getParentFragment()).startBrotherFragment(FavoritesFragment.getInstance());
            }
        });

        CardTitle user_bangumi_cardTitle = view.findViewById(R.id.user_bangumi_cardTitle);
        user_bangumi_cardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                Toast.makeText(context, "订阅的番剧", Toast.LENGTH_SHORT).show();
            }
        });

        CardTitle user_coin_cardTitle = view.findViewById(R.id.user_coin_cardTitle);
        user_coin_cardTitle.setOnClickActionListener(new CardTitle.OnClickActionListener() {
            @Override
            public void onClickAction() {
                Toast.makeText(context, "最近投币的视频", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        ((NavFragment) getParentFragment()).startBrotherFragment(UserInfoFragment.newInstance());
    }
}
