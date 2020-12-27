package com.leon.biuvideo.ui.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

public class PictureViewer extends PopupWindow implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private final int selectedPosition;
    private final List<String> pictures;
    private List<ImageView> imageViews;

    private final Context context;
    private View pictureViewerView;
    private ImageView picture_viewer_imageView_back, picture_viewer_imageView_savePic;
    private ViewPager picture_viewer_viewPager;
    private TextView picture_viewer_textView_index;

    private PictureViewerAdapter pictureViewerAdapter;

    public PictureViewer(Context context, int selectedPosition, List<String> pictures) {
        super(context);
        this.context = context;
        this.selectedPosition = selectedPosition;
        this.pictures = pictures;

        init();
        initView();
        initValue();
    }

    private void init() {
        imageViews = new ArrayList<>();

        for (int i = 0; i < pictures.size(); i++) {
            imageViews.add(new ImageView(context));
        }
    }

    private void initView() {
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
        String indexStr = (selectedPosition + 1) + "/" + pictures.size();
        picture_viewer_textView_index.setText(indexStr);

        pictureViewerAdapter = new PictureViewerAdapter(context, pictures, imageViews);
        picture_viewer_viewPager.setAdapter(pictureViewerAdapter);
        picture_viewer_viewPager.setCurrentItem(selectedPosition);
        picture_viewer_viewPager.setPageMargin(40);

        this.setContentView(pictureViewerView);
        this.setWidth(ViewGroup.MarginLayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.MarginLayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(0xfff));
        this.setAnimationStyle(R.style.picture_viewer_anim_style);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture_viewer_imageView_back:
                this.dismiss();
                break;
            case R.id.picture_viewer_imageView_savePic:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean saveState = ResourceUtils.savePicture(context, pictures.get(picture_viewer_viewPager.getCurrentItem()));

                        Looper.prepare();
                        Toast.makeText(context, saveState ? "保存成功" : "保存失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();

                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        String indexStr = (position + 1) + "/" + pictures.size();
        picture_viewer_textView_index.setText(indexStr);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * PictureViewer适配器
     */
    static class PictureViewerAdapter extends PagerAdapter {
        private final Context context;
        private final List<String> pictures;
        private final List<ImageView> imageViews;

        public PictureViewerAdapter(Context context, List<String> pictures, List<ImageView> imageViews) {
            this.context = context;
            this.pictures = pictures;
            this.imageViews = imageViews;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            Glide.with(context).load(pictures.get(position) + ImagePixelSize.VIEWER.value).into(imageViews.get(position));

            return imageViews.get(position);
        }

        @Override
        public int getCount() {
            return pictures.size();
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
    }
}