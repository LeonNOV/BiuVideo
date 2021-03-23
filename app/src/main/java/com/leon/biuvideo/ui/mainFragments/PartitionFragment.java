package com.leon.biuvideo.ui.mainFragments;

import android.view.View;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.NavFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.mainFragments.partitionFragments.PartitionBaseFragment;
import com.leon.biuvideo.values.Partitions;

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
        switch (v.getId()) {
            case R.id.partition_anime:
                jumpToPartitionBaseFragment(Partitions.ANIME);
                break;
            case R.id.partition_guochuang:
                jumpToPartitionBaseFragment(Partitions.GUOCHUANG);
                break;
            case R.id.partition_douga:
                jumpToPartitionBaseFragment(Partitions.DOUGA);
                break;
            case R.id.partition_ent:
                jumpToPartitionBaseFragment(Partitions.ENT);
                break;
            case R.id.partition_article:
//                jumpToPartitionBaseFragment(Partitions.ANIME);
                break;
            case R.id.partition_music:
                jumpToPartitionBaseFragment(Partitions.MUSIC);
                break;
            case R.id.partition_dance:
                jumpToPartitionBaseFragment(Partitions.DANCE);
                break;
            case R.id.partition_documentary:
                jumpToPartitionBaseFragment(Partitions.DOCUMENTARY);
                break;
            case R.id.partition_game:
                jumpToPartitionBaseFragment(Partitions.GAME);
                break;
            case R.id.partition_technology:
                jumpToPartitionBaseFragment(Partitions.TECHNOLOGY);
                break;
            case R.id.partition_digital:
                jumpToPartitionBaseFragment(Partitions.DIGITAL);
                break;
            case R.id.partition_life:
                jumpToPartitionBaseFragment(Partitions.LIFE);
                break;
            case R.id.partition_food:
                jumpToPartitionBaseFragment(Partitions.FOOD);
                break;
            case R.id.partition_animal:
                jumpToPartitionBaseFragment(Partitions.ANIMAL);
                break;
            case R.id.partition_kichiku:
                jumpToPartitionBaseFragment(Partitions.KICHIKU);
                break;
            case R.id.partition_fashion:
                jumpToPartitionBaseFragment(Partitions.FASHION);
                break;
            case R.id.partition_cinephile:
                jumpToPartitionBaseFragment(Partitions.CINEPHILE);
                break;
            case R.id.partition_movie:
                jumpToPartitionBaseFragment(Partitions.MOVIE);
                break;
            case R.id.partition_teleplay:
                jumpToPartitionBaseFragment(Partitions.TELEPLAY);
                break;
            case R.id.partition_channel:
//                jumpToPartitionBaseFragment(Partitions.);
                break;
            default:
                break;

        }
    }

    private void jumpToPartitionBaseFragment(Partitions partitions) {
        ((NavFragment) getParentFragment()).startBrotherFragment(new PartitionBaseFragment(partitions));
    }
}
