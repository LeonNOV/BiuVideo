package com.leon.biuvideo.adapters.PlayListAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;
import com.leon.biuvideo.ui.activitys.UpSongActivity;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;

import java.util.List;

/**
 * music播放列表适配器
 */
public class MusicListAdapter extends BaseAdapter<MusicPlayList> {
    private List<MusicPlayList> musicPlayLists;
    private final Context context;

    public MusicListAdapter(List<MusicPlayList> musicPlayLists, Context context) {
        super(musicPlayLists, context);
        this.musicPlayLists = musicPlayLists;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        MusicPlayList musicPlayList = musicPlayLists.get(position);

        //设置序号
        holder.setText(R.id.play_list_music_textView_serial, String.valueOf(position + 1))

                //设置歌曲名称
                .setText(R.id.play_list_music_textView_musicName, musicPlayList.musicName)

                //设置作者
                .setText(R.id.play_list_video_author, musicPlayList.author)

                //设置是否显示video图标
                .setVisibility(R.id.play_list_music_imageView_isHaveVideo, musicPlayList.isHaveVideo ? View.VISIBLE : View.INVISIBLE)
                .setOnClickListener(R.id.play_list_music_imageView_isHaveVideo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                        if (!isHaveNetwork) {
                            Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //跳转到VideoActivity
                        Intent intent = new Intent(context, VideoActivity.class);
                        intent.putExtra("bvid", musicPlayLists.get(position).bvid);

                        Fuck.blue(musicPlayLists.get(position).bvid);
                        Fuck.blue(position + "");

                        context.startActivity(intent);
                    }
                })

                //设置监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                        if (!isHaveNetwork) {
                            Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        long[] sids = new long[musicPlayLists.size()];
                        for (int i = 0; i < musicPlayLists.size(); i++) {
                            sids[i] = musicPlayLists.get(i).sid;
                        }

                        //跳转到UpSongActivity
                        Intent intent = new Intent(context, UpSongActivity.class);
                        intent.putExtra("position", position);
                        intent.putExtra("sids", sids);

                        context.startActivity(intent);
                    }
                });
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.play_list_music_item;
    }

    //加载数据使用
    public void refresh(List<MusicPlayList> addOns) {
        //清空原有数据
        if (addOns.size() > 0) {
            if (musicPlayLists.size() > 0) {
                musicPlayLists.clear();
            }

            musicPlayLists.addAll(addOns);
            notifyDataSetChanged();
        }
    }
}
