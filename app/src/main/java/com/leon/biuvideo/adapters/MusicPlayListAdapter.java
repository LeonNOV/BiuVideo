package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.adapters.userFragmentAdapters.OnMusicListListener;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;

import java.util.List;

/**
 * 音乐播放界面中播放列表的适配器
 */
public class MusicPlayListAdapter extends BaseAdapter<LocalOrder> {
    public List<LocalOrder> localOrderList;

    public MusicPlayListAdapter(List<LocalOrder> localOrderList, Context context) {
        super(localOrderList, context);

        this.localOrderList = localOrderList;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.music_play_list_item;
    }

    private OnMusicListListener onMusicListListener;

    public void setOnMusicListListener(OnMusicListListener onMusicListListener) {
        this.onMusicListListener = onMusicListListener;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        LocalOrder localOrder = localOrderList.get(position);
        JSONObject jsonObject = localOrder.jsonObject;

        holder
                .setText(R.id.music_textView_playList_title, jsonObject.getString("title"))
                .setText(R.id.music_textView_playList_author, jsonObject.getString("author"))
                .setOnClickListener(R.id.music_imageView_playList_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            localOrderList.remove(position);
                        } catch (IndexOutOfBoundsException e) {
                            localOrderList.remove(localOrder);
                        }

                        //通知数据已发生改变
                        notifyItemRemoved(position);

                        //刷新UpSongActivity红心的状态
                        if (onMusicListListener != null) {
                            onMusicListListener.onRefreshFavoriteIcon(localOrder);
                        }
                    }
                })
                //切换歌曲
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onMusicListListener != null) {
                            onMusicListListener.onSwitchMusic(localOrder.mainId);
                        }
                    }
                });
    }
}
