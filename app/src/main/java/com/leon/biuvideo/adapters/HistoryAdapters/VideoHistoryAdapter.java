package com.leon.biuvideo.adapters.HistoryAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.values.ImagePixelSize;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoHistoryAdapter extends BaseAdapter<History.InnerHistory> {
    private final List<History.InnerHistory> historys;
    private final Context context;

    public VideoHistoryAdapter(List<History.InnerHistory> historys, Context context) {
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

        holder.setImage(R.id.history_item_imageView_cover, history.cover, ImagePixelSize.COVER);

        String progressStr = ValueFormat.lengthGenerate(history.progress);
        String durationStr = ValueFormat.lengthGenerate(history.duration);
        holder.setText(R.id.history_item_textView_length, progressStr + ":" + durationStr);

        ProgressBar progressBar = holder.findById(R.id.history_item_progress);
        progressBar.setMax(history.duration);
        progressBar.setProgress(history.progress);

        holder.setText(R.id.history_item_textView_title, history.title)
                .setText(R.id.history_item_textView_view_time, new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA).format(new Date(history.viewDate)))
                .setText(R.id.history_item_textView_uname, history.authorName);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("bvid", history.bvid);
                context.startActivity(intent);
            }
        });
    }
}
