package com.leon.biuvideo.ui.mainFragments;

import android.view.View;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;

/**
 * @Author Leon
 * @Time 2021/3/22
 * @Desc 分区页面
 */
public class PartitionFragment extends BaseSupportFragment implements View.OnClickListener {
    @Override
    protected int setLayout() {
        return R.layout.partition_fragment;
    }

    @Override
    protected void initView() {
        BindingUtils bindingUtils = new BindingUtils(view, context);
        bindingUtils
                .setOnClickListener(R.id.partition_anime, this)
                .setOnClickListener(R.id.partition_guochuang, this)
                .setOnClickListener(R.id.partition_cinema, this)
                .setOnClickListener(R.id.partition_documentary, this)
                .setOnClickListener(R.id.partition_article, this)
                .setOnClickListener(R.id.partition_douga, this)
                .setOnClickListener(R.id.partition_music, this)
                .setOnClickListener(R.id.partition_dance, this)
                .setOnClickListener(R.id.partition_game, this)
                .setOnClickListener(R.id.partition_technology, this)
                .setOnClickListener(R.id.partition_digital, this)
                .setOnClickListener(R.id.partition_life, this)
                .setOnClickListener(R.id.partition_food, this)
                .setOnClickListener(R.id.partition_animal, this)
                .setOnClickListener(R.id.partition_kichiku, this)
                .setOnClickListener(R.id.partition_fashion, this)
                .setOnClickListener(R.id.partition_ent, this)
                .setOnClickListener(R.id.partition_cinephile, this)
                .setOnClickListener(R.id.partition_movie, this)
                .setOnClickListener(R.id.partition_teleplay, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.partition_anime:
                break;
            case R.id.partition_guochuang:
                break;
            case R.id.partition_cinema:
                break;
            case R.id.partition_documentary:
                break;
            case R.id.partition_article:
                break;
            case R.id.partition_douga:
                break;
            case R.id.partition_music:
                break;
            case R.id.partition_dance:
                break;
            case R.id.partition_game:
                break;
            case R.id.partition_technology:
                break;
            case R.id.partition_digital:
                break;
            case R.id.partition_life:
                break;
            case R.id.partition_food:
                break;
            case R.id.partition_animal:
                break;
            default:
                break;

        }
    }
}
