package com.leon.biuvideo.ui.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UpMaster.UpMasterViewPageAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpInfo;
import com.leon.biuvideo.ui.fragments.UpMasterFragments.UpAudioFragment;
import com.leon.biuvideo.ui.fragments.UpMasterFragments.UpPictureFragment;
import com.leon.biuvideo.ui.fragments.UpMasterFragments.UpVideoFragment;
import com.leon.biuvideo.utils.SQLiteHelper;
import com.leon.biuvideo.utils.WebpSizes;
import com.leon.biuvideo.utils.resourcesParseUtils.UpInfoParseUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class UpMasterActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ImageView up_imageView_cover;
    private CircleImageView up_circleImageView_face;
    private TextView up_textView_name;
    private ImageView up_imageView_favoriteIconState;
    private TextView up_textView_favoriteStrState;
    private ExpandableTextView up_textView_sign;
    //    private TextView up_textView_follower, up_textView_like, up_textView_view;
    private TextView up_textView_video, up_textView_audio, up_textView_picture;
    private ViewPager up_viewPage;

    private long mid;
    private UpInfo upInfo;

    public UpMasterActivity() {
        super();
    }

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
        up_imageView_favoriteIconState.setOnClickListener(this);

        up_textView_favoriteStrState = findViewById(R.id.up_textView_favoriteStrState);

        up_textView_sign = findViewById(R.id.up_textView_sign);

//        up_textView_follower = findViewById(R.id.up_textView_follower);
//        up_textView_like = findViewById(R.id.up_textView_like);
//        up_textView_view = findViewById(R.id.up_textView_view);

        up_textView_video = findViewById(R.id.up_textView_video);
        up_textView_video.setOnClickListener(this);

        up_textView_audio = findViewById(R.id.up_textView_audio);
        up_textView_audio.setOnClickListener(this);

        up_textView_picture = findViewById(R.id.up_textView_picture);
        up_textView_picture.setOnClickListener(this);

        up_viewPage = findViewById(R.id.up_viewPage);
        up_viewPage.addOnPageChangeListener(this);
    }

    //初始化数据
    private void initValue() {
        //获取mid
        Intent intent = getIntent();
        mid = intent.getLongExtra("mid", -1);

        if (mid == -1) {
            Toast.makeText(this, "信息获取失败", Toast.LENGTH_SHORT).show();
            onDestroy();
        }

        setValue(mid);
        initViewPage();
    }

    //设置控件的数据
    private void setValue(long mid) {
        upInfo = UpInfoParseUtils.parseUpInfo(mid);

        //设置顶部图片
        Glide.with(getApplicationContext()).load(upInfo.topPhoto).into(up_imageView_cover);

        //设置头像
        Glide.with(getApplicationContext()).load(upInfo.face + WebpSizes.face).into(up_circleImageView_face);

        //设置昵称
        up_textView_name.setText(upInfo.name);

        //设置签名
        up_textView_sign.setText(upInfo.sign);

        //设置关注状态
        boolean favorite_state = queryFavoriteState(mid);

        if (favorite_state) {
            up_imageView_favoriteIconState.setImageResource(R.drawable.favorite);
            up_textView_favoriteStrState.setText("已关注");
        }
    }

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
                up_textView_video.setTextColor(getResources().getColor(R.color.bilibilib_pink));
                up_textView_audio.setTextColor(getResources().getColor(R.color.normal));
                up_textView_picture.setTextColor(getResources().getColor(R.color.normal));

                break;
            case 1:
                up_textView_video.setTextColor(getResources().getColor(R.color.normal));
                up_textView_audio.setTextColor(getResources().getColor(R.color.bilibilib_pink));
                up_textView_picture.setTextColor(getResources().getColor(R.color.normal));

                break;
            case 2:
                up_textView_video.setTextColor(getResources().getColor(R.color.normal));
                up_textView_audio.setTextColor(getResources().getColor(R.color.normal));
                up_textView_picture.setTextColor(getResources().getColor(R.color.bilibilib_pink));

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.up_imageView_favoriteIconState:
                up_imageView_favoriteIconState.setImageResource(R.drawable.favorite);
                //将up的基本信息存入数据库
                addFavorite();
                break;
            case R.id.up_textView_video:
                up_viewPage.setCurrentItem(0);
                break;
            case R.id.up_textView_audio:
                up_viewPage.setCurrentItem(1);
                break;
            case R.id.up_textView_picture:
                up_viewPage.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    /**
     * 将UP的数据导入favorite_up库中
     */
    private void addFavorite() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext(), 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mid", mid);
        values.put("name", upInfo.name);
        values.put("faceUrl", upInfo.face);
        values.put("desc", upInfo.sign);
        values.put("isFavorite", 1);//1：正在关注；0：已取消关注

        database.insert("favorite_up", null, values);

        Toast.makeText(getApplicationContext(), upInfo.name + " 已加入到“我的收藏”中", Toast.LENGTH_SHORT).show();

        sqLiteHelper.close();
        database.close();
    }

    private boolean queryFavoriteState(long mid) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext(), 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        boolean state;

        Cursor favoriteUp = database.query("favorite_up", new String[]{"isFavorite"}, "mid=?", new String[]{mid + ""}, null, null, null);

        if (!favoriteUp.moveToNext()) {
            state = false;
        } else {
            int isFavorite = favoriteUp.getInt(0);
            state = isFavorite == 1;
        }

        favoriteUp.close();
        database.close();
        sqLiteHelper.close();

        return state;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}