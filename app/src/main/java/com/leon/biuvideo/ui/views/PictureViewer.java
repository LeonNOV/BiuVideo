package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.PictureViewerAdapter;
import com.leon.biuvideo.adapters.ViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class PictureViewer extends PopupWindow implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private int position;
    private List<String> pictuires;
    private List<ImageView> imageViews;

    private Context context;
    private View pictureViewerView;
    private ImageView picture_viewer_imageView_back, picture_viewer_imageView_savePic;
    private ViewPager picture_viewer_viewPager;
    private TextView picture_viewer_textView_index;

    private PictureViewerAdapter pictureViewerAdapter;

    public PictureViewer(Context context, int position, List<String> pictures) {
        super(context);
        this.context = context;
        this.position = position;
        this.pictuires = pictures;

        init();
        initView(context);
        initValue();
    }

    private void init() {
        imageViews = new ArrayList<>();

        for (int i = 0; i < pictuires.size(); i++) {
            imageViews.add(new ImageView(context));
        }
    }

    private void initView(Context context) {
        pictureViewerView = LayoutInflater.from(context).inflate(R.layout.picture_viewer, null);

        picture_viewer_imageView_back = pictureViewerView.findViewById(R.id.picture_viewer_imageView_back);
        picture_viewer_imageView_back.setOnClickListener(this);

        picture_viewer_imageView_savePic = pictureViewerView.findViewById(R.id.picture_viewer_imageView_savePic);
        picture_viewer_imageView_savePic.setOnClickListener(this);

        picture_viewer_viewPager = pictureViewerView.findViewById(R.id.picture_viewer_viewPager);
        picture_viewer_viewPager.addOnPageChangeListener(this);

        picture_viewer_textView_index = pictureViewerView.findViewById(R.id.picture_viewer_textView_index);
        picture_viewer_textView_index.setOnClickListener(this);
    }

    private void initValue() {
        String indexStr = (position + 1) + "/" + pictuires.size();
        picture_viewer_textView_index.setText(indexStr);

        pictureViewerAdapter = new PictureViewerAdapter(context, pictuires, imageViews);
        picture_viewer_viewPager.setAdapter(pictureViewerAdapter);

        this.setContentView(pictureViewerView);
        this.setWidth(ViewGroup.MarginLayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.MarginLayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(0xfff));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture_viewer_imageView_back:
                this.dismiss();
                break;
            case R.id.picture_viewer_imageView_savePic:
                Toast.makeText(context, "保存第" + pictureViewerAdapter.getPosition() + "张图片", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        String indexStr = (position + 1) + "/" + pictuires.size();
        picture_viewer_textView_index.setText(indexStr);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
