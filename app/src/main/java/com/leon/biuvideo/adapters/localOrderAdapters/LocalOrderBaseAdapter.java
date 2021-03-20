package com.leon.biuvideo.adapters.localOrderAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.ui.activitys.ArticleActivity;
import com.leon.biuvideo.ui.activitys.BangumiActivity;
import com.leon.biuvideo.ui.activitys.MusicActivity;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiParser;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.LocalOrderType;
import com.leon.biuvideo.values.SortType;

import java.util.List;

/**
 * 本地订阅：显示Bangumi、Article和Audio，需要配合LocalOrderType使用
 */
public class LocalOrderBaseAdapter extends BaseAdapter<LocalOrder> {
    private final List<LocalOrder> localOrderList;
    private final Context context;
    private final LocalOrderType localOrderType;

    public LocalOrderBaseAdapter(Context context, List<LocalOrder> localOrderList, LocalOrderType localOrderType) {
        super(localOrderList, context);

        this.context = context;
        this.localOrderList = localOrderList;
        this.localOrderType = localOrderType;
    }

    @Override
    public int getLayout(int viewType) {
        switch (localOrderType) {
            case BANGUMI:
                return R.layout.order_item;
            case ARTICLE:
                return R.layout.favorite_article_item;
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
                                if (!InternetUtils.checkNetwork(context)) {
                                    SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                                    return;
                                }

                                int pageNum = 1;
                                Bangumi targetBangumi = null;

                                BangumiParser bangumiParser = new BangumiParser();
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
                /*holder
                        .setText(R.id.user_article_item_title, jsonObject.getString("title"))
                        .setText(R.id.user_article_item_desc, jsonObject.getString("summary"))
                        .setImage(R.id.user_article_item_cover, jsonObject.getString("cover"), ImagePixelSize.COVER)
                        .setText(R.id.user_article_item_addTime, "收藏于" + ValueUtils.generateTime(localOrder.addTime, false, false, "/"))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!InternetUtils.checkNetwork(context)) {
                                    SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                                    return;
                                }

                                FavoriteArticle favoriteArticle = new FavoriteArticle();
                                favoriteArticle.mid = Long.parseLong(localOrder.subId);
                                favoriteArticle.face = jsonObject.getString("face");
                                favoriteArticle.articleId = Long.parseLong(localOrder.mainId);
                                favoriteArticle.title = jsonObject.getString("title");
                                favoriteArticle.summary = jsonObject.getString("summary");
                                favoriteArticle.author = jsonObject.getString("author");
                                favoriteArticle.coverUrl = jsonObject.getString("cover");
                                favoriteArticle.category = jsonObject.getString("category");
                                favoriteArticle.ctime = jsonObject.getLongValue("ctime");
                                favoriteArticle.favoriteTime = jsonObject.getLongValue("favoriteTime");
                                favoriteArticle.view = jsonObject.getIntValue("view");
                                favoriteArticle.like = jsonObject.getIntValue("like");
                                favoriteArticle.reply = jsonObject.getIntValue("reply");

                                Intent intent = new Intent(context, ArticleActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("favoriteArticle", favoriteArticle);
                                intent.putExtras(bundle);

                                context.startActivity(intent);
                            }
                        });
                break;*/
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
                                    if (!InternetUtils.checkNetwork(context)) {
                                        SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                                        return;
                                    }

                                    Intent videoIntent = new Intent(context, VideoActivity.class);
                                    videoIntent.putExtra("bvid", localOrder.subId);
                                    context.startActivity(videoIntent);
                                }
                            }
                        })
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!InternetUtils.checkNetwork(context)) {
                                    SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                                    return;
                                }

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
