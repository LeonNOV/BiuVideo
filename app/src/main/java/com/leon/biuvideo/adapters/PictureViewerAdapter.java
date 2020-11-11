package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class PictureViewerAdapter extends PagerAdapter {
    private Context context;
    private List<String> pictuires;
    private List<ImageView> imageViews;

    private int position;

    public PictureViewerAdapter(Context context, List<String> pictuires, List<ImageView> imageViews) {
        this.context = context;
        this.pictuires = pictuires;
        this.imageViews = imageViews;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(imageViews.get(position));
        Glide.with(context).load(pictuires.get(position)).into(imageViews.get(position));
        this.position = position;
        return imageViews.get(position);
    }

    @Override
    public int getCount() {
        return pictuires.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (position == 0 && imageViews.size() == 0) {
            return;
        }
        if (position == imageViews.size()) {
            container.removeView(imageViews.get(--position));
        } else {
            container.removeView(imageViews.get(position));
        }

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    public int getPosition() {
        return position;
    }
}
