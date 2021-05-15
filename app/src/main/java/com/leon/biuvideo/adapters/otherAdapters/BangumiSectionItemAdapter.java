package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiSection;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc 番剧/电视剧等 PV、花絮等视频适配器
 */
public class BangumiSectionItemAdapter extends BaseAdapter<BangumiSection.Episode> {
    private final List<BangumiSection.Episode> episodeList;
    private final MainActivity mainActivity;

    public BangumiSectionItemAdapter(List<BangumiSection.Episode> beans, MainActivity mainActivity, Context context) {
        super(beans, context);
        this.episodeList = beans;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.bangumi_section_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BangumiSection.Episode episode = episodeList.get(position);

        holder
                .setImage(R.id.bangumi_section_item_cover, episode.cover, ImagePixelSize.COVER)
                .setText(R.id.bangumi_section_item_title, episode.title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPublicFragment(mainActivity, FragmentType.VIDEO, episode.bvid);
                    }
                });
    }
}
