package com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteVideoFolderDetail;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @Author Leon
 * @Time 2021/3/19
 * @Desc 用户收藏的文件夹详情列表适配器
 */
public class FavoriteVideoFolderDetailAdapter extends BaseAdapter<FavoriteVideoFolderDetail.Media> {
    private final List<FavoriteVideoFolderDetail.Media> favoriteVideoFolderDetail;
    private final MainActivity mainActivity;

    public FavoriteVideoFolderDetailAdapter(List<FavoriteVideoFolderDetail.Media> favoriteVideoFolderDetail, MainActivity mainActivity, Context context) {
        super(favoriteVideoFolderDetail, context);
        this.favoriteVideoFolderDetail = favoriteVideoFolderDetail;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.favorite_video_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        FavoriteVideoFolderDetail.Media media = favoriteVideoFolderDetail.get(position);

        //设置封面
        holder
                .setImage(R.id.favorite_video_imageView_cover, media.cover, ImagePixelSize.COVER)

                //设置播放时长
                .setText(R.id.favorite_video_textView_duration, ValueUtils.lengthGenerate(media.duration))

                //设置收藏日期
                .setText(R.id.favorite_video_textView_ftime, "收藏于" + new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(media.addTime * 1000)))

                //设置标题部分
                .setText(R.id.favorite_video_textView_title, media.title)

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (media.isFailed) {
                            SimpleSnackBar.make(v, context.getString(R.string.videoFailed), SimpleSnackBar.LENGTH_LONG).show();
                        } else {
                            if (InternetUtils.checkNetwork(v)) {
                                startPublicFragment(mainActivity, FragmentType.VIDEO, media.bvid);
                            }
                        }
                    }
                });
    }
}
