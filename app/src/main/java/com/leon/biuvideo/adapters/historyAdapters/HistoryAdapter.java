package com.leon.biuvideo.adapters.historyAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.ui.activitys.ArticleActivity;
import com.leon.biuvideo.ui.activitys.BangumiActivity;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.parseDataUtils.articleParseUtils.ArticleParser;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiParser;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.SortType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 根据HistoryType创建对应的Item
 */
public class HistoryAdapter extends BaseAdapter<History.InnerHistory> {
    private final List<History.InnerHistory> historys;
    private final Context context;
    private ArticleParser articleParser;

    public HistoryAdapter(List<History.InnerHistory> historys, Context context) {
        super(historys, context);

        this.historys = historys;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.history_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        History.InnerHistory history = historys.get(position);

        //直播状态
        int liveStateId = holder.findById(R.id.history_item_imageView_live_state).getId();

        //视频长度
        int videoLengthId = holder.findById(R.id.history_item_textView_length).getId();

        //视频观看进度条
        int progressId = holder.findById(R.id.history_item_progress).getId();

        int badge = holder.findById(R.id.history_item_textView_badge).getId();

        int bangumiProgress = holder.findById(R.id.history_item_textView_progress).getId();
        int newDesc = holder.findById(R.id.history_item_textView_newDesc).getId();

        int time = holder.findById(R.id.history_item_linearLayout_time).getId();
        int upName = holder.findById(R.id.history_item_linearLayout_upName).getId();

        //判断展示的历史记录为啥
        switch (history.historyType) {
            case VIDEO:
                holder
                        .setVisibility(liveStateId, View.GONE)
                        .setVisibility(bangumiProgress, View.GONE)
                        .setVisibility(newDesc, View.GONE)
                        .setText(badge, history.badge);

                int progress = history.progress;
                int duration = history.duration;
                holder.setText(videoLengthId, progress == -1 ? "已看完" : ValueFormat.lengthGenerate(progress) + "/" + ValueFormat.lengthGenerate(duration));

                ProgressBar progressBar = holder.findById(progressId);
                progressBar.setMax(duration);
                progressBar.setProgress(progress == -1 ? duration : progress);

                break;
            case LIVE:
                holder
                        .setVisibility(bangumiProgress, View.GONE)
                        .setVisibility(newDesc, View.GONE)
                        .setVisibility(videoLengthId, View.GONE)
                        .setVisibility(badge, View.GONE)
                        .setVisibility(progressId, View.GONE);
                //是否正在直播
                if (history.liveState) {
                    holder.setImage(liveStateId, R.drawable.icon_live);
                }

                break;
            case ARTICLE:
                holder
                        .setVisibility(bangumiProgress, View.GONE)
                        .setVisibility(newDesc, View.GONE)
                        .setVisibility(liveStateId, View.GONE)
                        .setVisibility(videoLengthId, View.GONE)
                        .setVisibility(progressId, View.GONE)
                        .setText(badge, history.badge);

                break;
            case BANGUMI:
                holder
                        .setVisibility(time, View.GONE)
                        .setVisibility(upName, View.GONE)
                        .setText(bangumiProgress, history.showTitle)
                        .setText(newDesc, history.newDesc)
                        .setText(badge, history.badge);
                break;
            default:
                break;
        }

        holder
                .setImage(R.id.history_item_imageView_cover, history.cover, ImagePixelSize.COVER)
                .setText(R.id.history_item_textView_title, history.title)
                .setText(R.id.history_item_textView_view_time, new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA).format(new Date(history.viewDate * 1000)))
                .setText(R.id.history_item_textView_uname, history.authorName)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!InternetUtils.checkNetwork(context)) {
                            Snackbar.make(v, R.string.networkWarn, Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        if (history.badge.equals("纪录片")) {
                            Snackbar.make(v, "该功能尚未开发，还请谅解(￣﹏￣；)", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent;
                        switch (history.historyType) {
                            case VIDEO:
                                intent = new Intent(context, VideoActivity.class);
                                intent.putExtra("bvid", history.bvid);
                                context.startActivity(intent);
                                break;
                            case ARTICLE:
                                intent = new Intent(context, ArticleActivity.class);

                                if (articleParser == null) {
                                    articleParser = new ArticleParser(context);
                                }

                                Article article = articleParser.getArticle(history.oid);
                                article.coverUrl = history.cover;
                                article.face = history.authorFace;
                                article.title = history.title;

                                Bundle articleBundle = new Bundle();
                                articleBundle.putSerializable("article", article);
                                intent.putExtras(articleBundle);
                                intent.putExtra("isHistory", true);

                                context.startActivity(intent);
                                break;
                            case LIVE:
                                Snackbar.make(v, "该功能尚未开发，还请谅解(￣﹏￣；)", Snackbar.LENGTH_SHORT).show();
                                break;
                            case BANGUMI:
                                int pageNum = 1;
                                Bangumi targetBangumi = null;

                                BangumiParser bangumiParser = new BangumiParser(context);
                                List<Bangumi> bangumiList;

                                while (targetBangumi == null) {
                                    bangumiList = bangumiParser.bangumiParse(history.title, pageNum, SortType.DEFAULT);
                                    pageNum++;
                                    for (Bangumi bangumi : bangumiList) {
                                        if (bangumi.title.equals(history.title)) {
                                            targetBangumi = bangumi;
                                            break;
                                        }
                                    }
                                }

                                if (targetBangumi != null) {
                                    intent = new Intent(context, BangumiActivity.class);
                                    Bundle bangumiBundle = new Bundle();
                                    bangumiBundle.putSerializable("bangumi", targetBangumi);
                                    intent.putExtras(bangumiBundle);

                                    context.startActivity(intent);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
    }
}
