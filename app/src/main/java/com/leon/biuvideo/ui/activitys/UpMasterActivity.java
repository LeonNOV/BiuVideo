package com.leon.biuvideo.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UpMasterViewPageAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpInfo;
import com.leon.biuvideo.ui.fragments.UpAudioFragment;
import com.leon.biuvideo.ui.fragments.UpPictureFragment;
import com.leon.biuvideo.ui.fragments.UpVideoFragment;
import com.leon.biuvideo.utils.resourcesParseUtils.UpInfoParseUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class UpMasterActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ImageView up_imageView_cover;
    private CircleImageView up_circleImageView_face;
    private TextView up_textView_name;
    private ImageView up_imageView_favoriteIconState;
    private TextView up_textView_favoriteStrState;
    private ExpandableTextView up_textView_sign;
//    private TextView up_textView_follower, up_textView_like, up_textView_view;
    private TextView up_textView_item1, up_textView_item2, up_textView_item3;
    private ViewPager up_viewPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_master);

        initView();

        initValue();
    }

    private void initView() {
        up_imageView_cover = findViewById(R.id.up_imageView_cover);

        up_circleImageView_face = findViewById(R.id.up_circleImageView_face);
        up_textView_name = findViewById(R.id.up_textView_name);

        up_imageView_favoriteIconState = findViewById(R.id.up_imageView_favoriteIconState);
        up_textView_favoriteStrState = findViewById(R.id.up_textView_favoriteStrState);

        up_textView_sign = findViewById(R.id.up_textView_sign);

//        up_textView_follower = findViewById(R.id.up_textView_follower);
//        up_textView_like = findViewById(R.id.up_textView_like);
//        up_textView_view = findViewById(R.id.up_textView_view);

        up_textView_item1 = findViewById(R.id.up_textView_item1);
        up_textView_item2 = findViewById(R.id.up_textView_item2);
        up_textView_item3 = findViewById(R.id.up_textView_item3);

        up_viewPage = findViewById(R.id.up_viewPage);
        up_viewPage.addOnPageChangeListener(this);
    }

    private void initValue() {
        //获取mid
        Intent intent = getIntent();
        long mid = intent.getExtras().getLong("mid");

        //设置控件数据
        setValue(mid);

        setViewPage();
    }

    //设置控件的数据
    private void setValue(long mid) {
        UpInfo upInfo = UpInfoParseUtils.parseUpInfo(mid);

        //设置顶部图片
        Glide.with(getApplicationContext()).load(upInfo.topPhoto).into(up_imageView_cover);

        //设置头像
        Glide.with(getApplicationContext()).load(upInfo.face).into(up_circleImageView_face);

        //设置昵称
        up_textView_name.setText(upInfo.name);

        //设置签名
        up_textView_sign.setText(upInfo.sign);
    }

    //设置ViewPage
    private void setViewPage() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new UpAudioFragment(null, getApplicationContext()));
        fragments.add(new UpVideoFragment(null, getApplicationContext()));
        fragments.add(new UpPictureFragment(null, getApplicationContext()));

        UpMasterViewPageAdapter viewPageAdapter = new UpMasterViewPageAdapter(getSupportFragmentManager(), fragments);
        up_viewPage.setAdapter(viewPageAdapter);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                up_textView_item1.setTextColor(R.color.bilibilib_pink);
                up_textView_item2.setTextColor(R.color.normal);
                up_textView_item3.setTextColor(R.color.normal);

                break;
            case 1:
                up_textView_item1.setTextColor(R.color.normal);
                up_textView_item2.setTextColor(R.color.bilibilib_pink);
                up_textView_item3.setTextColor(R.color.normal);

                break;
            case 2:
                up_textView_item1.setTextColor(R.color.normal);
                up_textView_item2.setTextColor(R.color.normal);
                up_textView_item3.setTextColor(R.color.bilibilib_pink);

                break;
            default:break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}