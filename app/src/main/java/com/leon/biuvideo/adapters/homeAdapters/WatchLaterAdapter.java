package com.leon.biuvideo.adapters.homeAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.WatchLater;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

/**
 * @Author Leon
 * @Time 2021/3/14
 * @Desc 稍后观看适配器
 */
public class WatchLaterAdapter extends BaseAdapter<WatchLater> {
    private final MainActivity mainActivity;
    private final Context context;

    public WatchLaterAdapter(MainActivity mainActivity, Context context) {
        super(context);

        this.mainActivity = mainActivity;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.watch_later_video_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        WatchLater watchLater = getAllData().get(position);

        // 如果为观看过该视频，则不显示进度
        if (watchLater.progress > 0) {
            holder.setText(R.id.watch_later_watch_later_video_item_progress, ((watchLater.progress * 100) / watchLater.duration) + "%");
        }

        ImageView watchLaterVideoItemCover = holder.findById(R.id.watch_later_video_item_cover);
        Glide
                .with(context)
                .load(PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? watchLater.cover : watchLater.cover + ImagePixelSize.COVER.value)
                .into(watchLaterVideoItemCover);

        if (watchLater.isInvalid) {
            holder.setVisibility(R.id.watch_later_video_item_invalidMark, View.VISIBLE);
            watchLaterVideoItemCover.setForeground(context.getDrawable(R.color.invalid_foreground));
        } else {
            holder.setVisibility(R.id.watch_later_video_item_invalidMark, View.GONE);
            watchLaterVideoItemCover.setForeground(null);
        }

        holder
                .setText(R.id.watch_later_video_item_duration, ValueUtils.lengthGenerate(watchLater.duration))
                .setText(R.id.watch_later_video_item_title, watchLater.title)
                .setText(R.id.watch_later_video_item_pubName, watchLater.userName)
                .setOnClickListener(R.id.watch_later_video_item_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            if (watchLater.isInvalid) {
                                SimpleSnackBar.make(v, context.getString(R.string.videoFailed), SimpleSnackBar.LENGTH_SHORT).show();
                                return;
                            }

                            startPublicFragment(mainActivity, FragmentType.VIDEO, watchLater.bvid);
                        }
                    }
                })
                .setOnClickListener(R.id.watch_later_video_item_cover_remove, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(v, context.getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG);
                    }
                });
    }
}
