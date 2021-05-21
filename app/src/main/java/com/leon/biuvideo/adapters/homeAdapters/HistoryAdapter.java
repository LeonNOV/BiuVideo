package com.leon.biuvideo.adapters.homeAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.beans.userBeans.HistoryType;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

/**
 * @Author Leon
 * @Time 2021/3/5
 * @Desc 历史记录适配器
 */
public class HistoryAdapter extends BaseAdapter<History> {
    private final MainActivity mainActivity;

    public HistoryAdapter(MainActivity mainActivity, Context context) {
        super(context);
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        if (viewType == 1) {
            return R.layout.history_item_article;
        } else {
            return R.layout.history_item_video;
        }
    }

    @Override
    public int getItemViewType(int position) {
        History history = getAllData().get(position);
        if (history.historyType == HistoryType.ARTICLE) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        History history = getAllData().get(position);

        if (history.historyType == HistoryType.ARTICLE) {
            initArticleItemView(holder, history);
        } else {
            initVideoItemView(holder, history);
        }
    }

    /**
     * 初始化video条目
     * @param holder    holder
     * @param history   history
     */
    private void initVideoItemView(BaseViewHolder holder, History history) {
        ImageView historyItemVideoPlatform = holder.findById(R.id.history_item_video_platform);
        switch (history.historyPlatformType) {
            case PHOTO:
                historyItemVideoPlatform.setImageResource(R.drawable.history_platform_photo);
                break;
            case PC:
                historyItemVideoPlatform.setImageResource(R.drawable.history_platform_pc);
                break;
            case PAD:
                historyItemVideoPlatform.setImageResource(R.drawable.history_platform_pad);
                break;
            case TV:
                historyItemVideoPlatform.setImageResource(R.drawable.history_platform_tv);
                break;
            default:
                historyItemVideoPlatform.setImageResource(R.drawable.ic_view_time);
                break;
        }

        ImageView historyItemVideoLiveStatus = holder.findById(R.id.history_item_video_live_status);
        ProgressBar historyItemVideoProgress = holder.findById(R.id.history_item_video_progress);
        TextView historyItemVideoDuration = holder.findById(R.id.history_item_video_duration);

        if (history.historyType == HistoryType.LIVE) {
            historyItemVideoLiveStatus.setVisibility(View.VISIBLE);
            historyItemVideoProgress.setVisibility(View.GONE);
            historyItemVideoDuration.setVisibility(View.GONE);
            if (history.liveState) {
                historyItemVideoLiveStatus.setImageResource(R.drawable.ic_lived);
            } else {
                historyItemVideoLiveStatus.setImageResource(R.drawable.ic_not_live);
            }
        } else {
            historyItemVideoLiveStatus.setVisibility(View.GONE);

            historyItemVideoDuration.setVisibility(View.VISIBLE);
            historyItemVideoDuration.setText(ValueUtils.lengthGenerate(history.duration));

            historyItemVideoProgress.setVisibility(View.VISIBLE);
            historyItemVideoProgress.setMax(history.duration);
            historyItemVideoProgress.setProgress(history.progress);
        }

        TextView historyItemVideoBiliUser = holder.findById(R.id.history_item_video_biliUser);
        if (history.historyType == HistoryType.BANGUMI) {
            historyItemVideoBiliUser.setVisibility(View.GONE);
            if (history.subTitle != null) {
                holder.setText(R.id.history_item_video_subTitle, history.subTitle);
            } else {
                holder.setVisibility(R.id.history_item_video_subTitle, View.GONE);
            }
        } else {
            historyItemVideoBiliUser.setVisibility(View.VISIBLE);
            historyItemVideoBiliUser.setText(history.authorName);

            holder.setVisibility(R.id.history_item_video_subTitle, View.GONE);
        }

        holder
                .setImage(R.id.history_item_video_cover, history.cover, ImagePixelSize.COVER)
                .setText(R.id.history_item_video_title, history.title)
                .setText(R.id.history_item_video_time, ValueUtils.generateTime(history.viewTime, "MM-dd HH:mm", true))
                .setOnClickListener(R.id.history_item_video_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            switch (history.historyType) {
                                case VIDEO:
                                    startPublicFragment(mainActivity, FragmentType.VIDEO, history.bvid);
                                    break;
                                case BANGUMI:
                                    startPublicFragment(mainActivity, FragmentType.BANGUMI, history.seasonId);
                                    break;
                                case LIVE:
                                    SimpleSnackBar.make(v, context.getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG).show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                })
                .setOnClickListener(R.id.history_item_video_operation, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(v, context.getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * 初始化article条目
     * @param holder    holder
     * @param history   history
     */
    private void initArticleItemView(BaseViewHolder holder, History history) {
        TextView historyItemArticlePlatform = holder.findById(R.id.history_item_article_platform);
        String text;
        if (history.historyPlatformType != null) {
            switch (history.historyPlatformType) {
                case PHOTO:
                    text = "已在手机端上进行阅读";
                    break;
                case PC:
                    text = "已在PC端上进行阅读";
                    break;
                case PAD:
                    text = "已在Pad端上进行阅读";
                    break;
                case TV:
                    text = "已在TV端上进行阅读";
                    break;
                default:
                    text = "使用未知设备阅读";
                    break;
            }
        } else {
            text = "使用未知设备阅读";
        }

        historyItemArticlePlatform.setText(text);

        holder.setImage(R.id.history_item_article_cover, history.cover, ImagePixelSize.COVER)
                .setText(R.id.history_item_article_title, history.title)
                .setText(R.id.history_item_article_biliUser, history.authorName)
                .setOnClickListener(R.id.history_item_article_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            startPublicFragment(mainActivity, FragmentType.ARTICLE, history.articleId);
                        }
                    }
                })
                .setOnClickListener(R.id.history_item_article_operation, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(v, context.getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG).show();
                    }
                });
    }
}
