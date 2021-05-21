package com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteVideoFolder;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.favoriteFragments.favoriteVideo.FavoriteVideoFolderDetailFragment;
import com.leon.biuvideo.utils.InternetUtils;

/**
 * @Author Leon
 * @Time 2021/3/19
 * @Desc 用户视频收藏夹适配器
 */
public class FavoriteVideoFolderAdapter extends BaseAdapter<FavoriteVideoFolder> {
    private final MainActivity mainActivity;

    public FavoriteVideoFolderAdapter(MainActivity mainActivity, Context context) {
        super(context);
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.favorite_video_folder_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        FavoriteVideoFolder favoriteVideoFolder = getAllData().get(position);

        holder
                .setText(R.id.favorite_video_folder_item_name, favoriteVideoFolder.title)
                .setText(R.id.favorite_video_folder_item_count, String.valueOf(favoriteVideoFolder.count))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            ((BaseSupportFragment)mainActivity.getTopFragment()).start(new FavoriteVideoFolderDetailFragment(favoriteVideoFolder.title, favoriteVideoFolder.id));
                        }
                    }
                });
    }
}
