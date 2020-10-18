package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.view.ViewPage;
import com.leon.biuvideo.utils.WebpSizes;

/**
 * video_listView_singleVideoList控件的适配器
 */
public class AnthologyAdapter extends BaseAdapter {
    private final Context context;
    private final ViewPage viewPage;

    public AnthologyAdapter(Context context, ViewPage viewPage) {
        this.context = context;
        this.viewPage = viewPage;
    }

    @Override
    public int getCount() {
        return viewPage.singleVideoInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return viewPage.singleVideoInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context, R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.item_imageView_cover);
            viewHolder.textView = view.findViewById(R.id.item_textView_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(viewPage.coverUrl + WebpSizes.cover).into(viewHolder.imageView);
        viewHolder.textView.setText(viewPage.singleVideoInfoList.get(i).part);
        return view;
    }

    public static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
