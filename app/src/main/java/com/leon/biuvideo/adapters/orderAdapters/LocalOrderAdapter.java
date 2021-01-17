package com.leon.biuvideo.adapters.orderAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.ui.activitys.ArticleActivity;
import com.leon.biuvideo.ui.activitys.BangumiActivity;
import com.leon.biuvideo.ui.activitys.MusicActivity;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiParser;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.LocalOrderType;
import com.leon.biuvideo.values.SortType;

import java.util.List;

public class LocalOrderAdapter extends BaseAdapter<LocalOrder> {
    private final List<LocalOrder> localOrderList;
    private final Context context;
    private final LocalOrderType localOrderType;

    public LocalOrderAdapter(Context context, List<LocalOrder> localOrderList, LocalOrderType localOrderType) {
        super(localOrderList, context);

        this.context = context;
        this.localOrderList = localOrderList;
        this.localOrderType = localOrderType;
    }

    @Override
    public int getLayout(int viewType) {
        switch (localOrderType) {
            case BANGUMI:
                return R.layout.order_bangumi_item;
            case ARTICLE:
                return R.layout.user_data_article_item;
            case AUDIO:
                return R.layout.play_list_music_item;
            default:
                return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        LocalOrder localOrder = localOrderList.get(position);
        JSONObject jsonObject = localOrder.jsonObject;

        switch (localOrderType) {
            case BANGUMI:
                holder
                        .setImage(R.id.order_item_imageView_cover, jsonObject.getString("cover"), ImagePixelSize.COVER)
                        .setText(R.id.order_item_textView_badge, jsonObject.getString("angleTitle"))
                        .setVisibility(R.id.order_item_textView_subtitle, View.GONE)
                        .setText(R.id.order_item_textView_title, jsonObject.getString("title"))
                        .setText(R.id.order_item_textView_desc, jsonObject.getString("desc"))
                        .setText(R.id.order_item_textView_area, jsonObject.getString("area"))
                        .setText(R.id.order_item_textView_total, jsonObject.getString("bangumiState"))
                        .setVisibility(R.id.order_item_textView_progress, View.GONE)
                        .setVisibility(R.id.order_item_view, View.GONE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pageNum = 1;
                                Bangumi targetBangumi = null;

                                BangumiParser bangumiParser = new BangumiParser(context);
                                List<Bangumi> bangumiList;

                                while (targetBangumi == null) {
                                    bangumiList = bangumiParser.bangumiParse(jsonObject.getString("title"), pageNum, SortType.DEFAULT);
                                    pageNum++;
                                    for (Bangumi bangumi : bangumiList) {
                                        if (bangumi.title.equals(jsonObject.getString("title"))) {
                                            targetBangumi = bangumi;
                                            break;
                                        }
                                    }
                                }

                                if (targetBangumi != null) {
                                    Intent intent = new Intent(context, BangumiActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("bangumi", targetBangumi);
                                    intent.putExtras(bundle);

                                    context.startActivity(intent);
                                }
                            }
                        });
                break;
            case ARTICLE:
                holder
                        .setText(R.id.user_article_item_title, jsonObject.getString("title"))
                        .setText(R.id.user_article_item_desc, jsonObject.getString("summary"))
                        .setImage(R.id.user_article_item_cover, jsonObject.getString("cover"), ImagePixelSize.COVER)
                        .setText(R.id.user_article_item_addTime, "收藏于" + ValueFormat.generateTime(localOrder.addTime, false, false, "/"))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Article article = new Article();
                                article.mid = Long.parseLong(localOrder.subId);
                                article.face = jsonObject.getString("face");
                                article.articleId = Long.parseLong(localOrder.mainId);
                                article.title = jsonObject.getString("title");
                                article.summary = jsonObject.getString("summary");
                                article.author = jsonObject.getString("author");
                                article.coverUrl = jsonObject.getString("cover");
                                article.category = jsonObject.getString("category");
                                article.ctime = jsonObject.getLongValue("ctime");
                                article.favoriteTime = jsonObject.getLongValue("favoriteTime");
                                article.view = jsonObject.getIntValue("view");
                                article.like = jsonObject.getIntValue("like");
                                article.reply = jsonObject.getIntValue("reply");

                                Intent intent = new Intent(context, ArticleActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("article", article);
                                intent.putExtras(bundle);

                                context.startActivity(intent);
                            }
                        });
                break;
            case AUDIO:
                holder
                        .setText(R.id.play_list_music_textView_serial, String.valueOf(position + 1))
                        .setText(R.id.play_list_music_textView_musicName, jsonObject.getString("title"))
                        .setText(R.id.play_list_video_author, jsonObject.getString("author"))
                        .setVisibility(R.id.play_list_music_imageView_isHaveVideo, localOrder.subId.equals("") ? View.GONE : View.VISIBLE)
                        .setOnClickListener(R.id.play_list_music_imageView_isHaveVideo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!localOrder.subId.equals("")) {
                                    Intent videoIntent = new Intent(context, VideoActivity.class);
                                    videoIntent.putExtra("bvid", localOrder.subId);
                                    context.startActivity(videoIntent);
                                }
                            }
                        })
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String[] sids = new String[localOrderList.size()];
                                for (int i = 0; i < localOrderList.size(); i++) {
                                    sids[i] = localOrderList.get(i).mainId;
                                }

                                Intent musicIntent = new Intent(context, MusicActivity.class);
                                musicIntent.putExtra("sids", sids);
                                musicIntent.putExtra("position", position);
                                context.startActivity(musicIntent);
                            }
                        });
                break;
            default:
                break;
        }
    }
}
