package com.leon.biuvideo.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UpMasterViewPageAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpAudio;
import com.leon.biuvideo.beans.upMasterBean.UpInfo;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.ui.fragments.UpAudioFragment;
import com.leon.biuvideo.ui.fragments.UpPictureFragment;
import com.leon.biuvideo.ui.fragments.UpVideoFragment;
import com.leon.biuvideo.utils.WebpSizes;
import com.leon.biuvideo.utils.resourcesParseUtils.UpAudioParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpInfoParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpPictureParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpVideoParseUtils;
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

    private long mid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_master);

        initView();

        initValue();
    }

    //初始化控件
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

        up_textView_item1 = findViewById(R.id.up_textView_video);
        up_textView_item2 = findViewById(R.id.up_textView_audio);
        up_textView_item3 = findViewById(R.id.up_textView_picture);

        up_viewPage = findViewById(R.id.up_viewPage);
        up_viewPage.addOnPageChangeListener(this);
    }

    //初始化数据
    private void initValue() {
        //获取mid
        Intent intent = getIntent();
        mid = intent.getExtras().getLong("mid");

        setValue(mid);
        initViewPage();
    }

    //设置控件的数据
    private void setValue(long mid) {
        UpInfo upInfo = UpInfoParseUtils.parseUpInfo(mid);

        //设置顶部图片
        Glide.with(getApplicationContext()).load(upInfo.topPhoto).into(up_imageView_cover);

        //设置头像
        Glide.with(getApplicationContext()).load(upInfo.face + WebpSizes.face).into(up_circleImageView_face);

        //设置昵称
        up_textView_name.setText(upInfo.name);

        //设置签名
        up_textView_sign.setText(upInfo.sign);
    }

    private List<UpAudio> upAudios;
    private List<UpPicture> upPictures;

    //设置ViewPage
    private void initViewPage() {
        List<Fragment> fragments = new ArrayList<>();

        //获取视频数据
        fragments.add(new UpVideoFragment(mid, 1, getApplicationContext()));

        //获取音频数据
        fragments.add(new UpAudioFragment(mid, 1, getApplicationContext()));

        //获取相簿数据
        fragments.add(new UpPictureFragment(mid, 0, getApplicationContext()));

        UpMasterViewPageAdapter viewPageAdapter = new UpMasterViewPageAdapter(getSupportFragmentManager(), fragments);
        up_viewPage.setAdapter(viewPageAdapter);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                up_textView_item1.setTextColor(getResources().getColor(R.color.bilibilib_pink));
                up_textView_item2.setTextColor(getResources().getColor(R.color.normal));
                up_textView_item3.setTextColor(getResources().getColor(R.color.normal));

                break;
            case 1:
                up_textView_item1.setTextColor(getResources().getColor(R.color.normal));
                up_textView_item2.setTextColor(getResources().getColor(R.color.bilibilib_pink));
                up_textView_item3.setTextColor(getResources().getColor(R.color.normal));

                break;
            case 2:
                up_textView_item1.setTextColor(getResources().getColor(R.color.normal));
                up_textView_item2.setTextColor(getResources().getColor(R.color.normal));
                up_textView_item3.setTextColor(getResources().getColor(R.color.bilibilib_pink));

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