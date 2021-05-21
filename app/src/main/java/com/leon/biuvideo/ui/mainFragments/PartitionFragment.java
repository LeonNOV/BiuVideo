package com.leon.biuvideo.ui.mainFragments;

import android.view.View;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.mainFragments.partitionFragments.PartitionBaseFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.Partitions;

/**
 * @Author Leon
 * @Time 2021/3/22
 * @Desc 分区主页面
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
                .setOnClickListener(R.id.partition_douga, this)
                .setOnClickListener(R.id.partition_ent, this)
                .setOnClickListener(R.id.partition_article, this)
                .setOnClickListener(R.id.partition_music, this)
                .setOnClickListener(R.id.partition_dance, this)
                .setOnClickListener(R.id.partition_documentary, this)
                .setOnClickListener(R.id.partition_game, this)
                .setOnClickListener(R.id.partition_technology, this)
                .setOnClickListener(R.id.partition_digital, this)
                .setOnClickListener(R.id.partition_life, this)
                .setOnClickListener(R.id.partition_food, this)
                .setOnClickListener(R.id.partition_animal, this)
                .setOnClickListener(R.id.partition_kichiku, this)
                .setOnClickListener(R.id.partition_fashion, this)
                .setOnClickListener(R.id.partition_cinephile, this)
                .setOnClickListener(R.id.partition_movie, this)
                .setOnClickListener(R.id.partition_teleplay, this)
                .setOnClickListener(R.id.partition_channel, this);
    }

    @Override
    public void onClick(View v) {
        Partitions partitions = null;

        switch (v.getId()) {
            case R.id.partition_anime:
                partitions = Partitions.ANIME;
                break;
            case R.id.partition_guochuang:
                partitions = Partitions.GUOCHUANG;
                break;
            case R.id.partition_douga:
                partitions = Partitions.DOUGA;
                break;
            case R.id.partition_ent:
                partitions = Partitions.ENT;
                break;
            case R.id.partition_article:
                SimpleSnackBar.make(v, "该分区还在施工中~", SimpleSnackBar.LENGTH_LONG).show();
                return;
            case R.id.partition_music:
                partitions = Partitions.MUSIC;
                break;
            case R.id.partition_dance:
                partitions = Partitions.DANCE;
                break;
            case R.id.partition_documentary:
                partitions = Partitions.DOCUMENTARY;
                break;
            case R.id.partition_game:
                partitions = Partitions.GAME;
                break;
            case R.id.partition_technology:
                partitions = Partitions.TECHNOLOGY;
                break;
            case R.id.partition_digital:
                partitions = Partitions.DIGITAL;
                break;
            case R.id.partition_life:
                partitions = Partitions.LIFE;
                break;
            case R.id.partition_food:
                partitions = Partitions.FOOD;
                break;
            case R.id.partition_animal:
                partitions = Partitions.ANIMAL;
                break;
            case R.id.partition_kichiku:
                partitions = Partitions.KICHIKU;
                break;
            case R.id.partition_fashion:
                partitions = Partitions.FASHION;
                break;
            case R.id.partition_cinephile:
                partitions = Partitions.CINEPHILE;
                break;
            case R.id.partition_movie:
                partitions = Partitions.MOVIE;
                break;
            case R.id.partition_teleplay:
                partitions = Partitions.TELEPLAY;
                break;
            case R.id.partition_channel:
                SimpleSnackBar.make(v, "该分区还在施工中~", SimpleSnackBar.LENGTH_LONG).show();
                return;
            default:
                break;
        }

        if (InternetUtils.checkNetwork(v)) {
            if (partitions != null) {
                jumpToPartitionBaseFragment(partitions);
            }
        }
    }

    private void jumpToPartitionBaseFragment(Partitions partitions) {
        ((NavFragment) getParentFragment()).startBrotherFragment(new PartitionBaseFragment(partitions));
    }
}
