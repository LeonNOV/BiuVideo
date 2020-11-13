package com.leon.biuvideo.ui.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.FavoriteAdapter;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.beans.upMasterBean.UpInfo;
import com.leon.biuvideo.ui.fragments.UserFragments.UserArticlesFragment;
import com.leon.biuvideo.ui.fragments.UserFragments.UserAudioListFragment;
import com.leon.biuvideo.ui.fragments.UserFragments.UserPictureListFragment;
import com.leon.biuvideo.ui.fragments.UserFragments.UserVideoListFragment;
import com.leon.biuvideo.utils.WebpSizes;
import com.leon.biuvideo.utils.dataUtils.FavoriteDatabaseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpInfoParseUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户界面activity
 * 由于用户粉丝数、获赞数和观看数的获取需要使用cookie获取，所以暂不添加该三项
 */
public class UpMasterActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ImageView up_imageView_cover, up_imageView_back;
    private CircleImageView up_circleImageView_face;
    private TextView up_textView_name;
    private ImageView up_imageView_favoriteIconState;
    private TextView up_textView_favoriteStrState;
    private ExpandableTextView up_textView_sign;
    private TextView user_textView_video, user_textView_audio, user_textView_articles, user_textView_picture;
    private ViewPager up_viewPage;

    private ViewPageAdapter viewPageAdapter;

    private long mid;
    private UpInfo upInfo;

    private FavoriteDatabaseUtils favoriteDatabaseUtils;

    private int point_bilibili_pink = R.drawable.shape_bilibili_pink;
    private int point_bilibili_pink_lite = R.drawable.ripple_user_bilibili_pink_lite;

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

        up_imageView_back = findViewById(R.id.up_imageView_back);
        up_imageView_back.setOnClickListener(this);

        up_circleImageView_face = findViewById(R.id.up_circleImageView_face);
        up_textView_name = findViewById(R.id.up_textView_name);

        up_imageView_favoriteIconState = findViewById(R.id.up_imageView_favoriteIconState);
        up_imageView_favoriteIconState.setOnClickListener(this);

        up_textView_favoriteStrState = findViewById(R.id.up_textView_favoriteStrState);

        up_textView_sign = findViewById(R.id.up_textView_sign);

        user_textView_video = findViewById(R.id.user_textView_video);
        user_textView_video.setOnClickListener(this);

        user_textView_audio = findViewById(R.id.user_textView_audio);
        user_textView_audio.setOnClickListener(this);

        user_textView_articles = findViewById(R.id.user_textView_articles);
        user_textView_articles.setOnClickListener(this);

        user_textView_picture = findViewById(R.id.user_textView_picture);
        user_textView_picture.setOnClickListener(this);

        up_viewPage = findViewById(R.id.up_viewPage);
        up_viewPage.addOnPageChangeListener(this);
    }

    //初始化数据
    private void initValue() {
        favoriteDatabaseUtils = new FavoriteDatabaseUtils(getApplicationContext());

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

        //获取关注状态
        boolean favorite_state = favoriteDatabaseUtils.queryFavoriteState(mid);

        if (favorite_state) {
            up_imageView_favoriteIconState.setImageResource(R.drawable.favorite);
            up_textView_favoriteStrState.setText("已关注");
        } else {
            up_imageView_favoriteIconState.setImageResource(R.drawable.no_favorite);
            up_textView_favoriteStrState.setText("未关注");
        }
    }

    //设置ViewPage
    private void initViewPage() {
        List<Fragment> fragments = new ArrayList<>();

        //获取视频数据
        fragments.add(new UserVideoListFragment(mid, 1, getApplicationContext()));

        //获取音频数据
        fragments.add(new UserAudioListFragment(mid, 1, getApplicationContext()));

        //获取文章数据
        fragments.add(new UserArticlesFragment(mid, 1, getApplicationContext()));

        //获取相簿数据
        fragments.add(new UserPictureListFragment(mid, 0, getApplicationContext()));

        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), fragments);
        up_viewPage.setAdapter(viewPageAdapter);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                user_textView_video.setBackgroundResource(point_bilibili_pink);
                user_textView_audio.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_articles.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_picture.setBackgroundResource(point_bilibili_pink_lite);

                break;
            case 1:
                user_textView_video.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_audio.setBackgroundResource(point_bilibili_pink);
                user_textView_articles.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_picture.setBackgroundResource(point_bilibili_pink_lite);

                break;
            case 2:
                user_textView_video.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_audio.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_articles.setBackgroundResource(point_bilibili_pink);
                user_textView_picture.setBackgroundResource(point_bilibili_pink_lite);

                break;
            case 3:
                user_textView_video.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_audio.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_articles.setBackgroundResource(point_bilibili_pink_lite);
                user_textView_picture.setBackgroundResource(point_bilibili_pink);

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_imageView_back:
                this.finish();
                break;
            case R.id.up_imageView_favoriteIconState:

                //判断是否存在于数据库中
                boolean state = favoriteDatabaseUtils.queryFavoriteState(mid);

                if (state) {
                    //从数据库中移除
                    up_imageView_favoriteIconState.setImageResource(R.drawable.no_favorite);
                    up_textView_favoriteStrState.setText("未关注");

                    favoriteDatabaseUtils.removeFavorite(mid);
                } else {
                    //添加至数据库中
                    up_imageView_favoriteIconState.setImageResource(R.drawable.favorite);
                    up_textView_favoriteStrState.setText("已关注");

                    Favorite favorite = new Favorite();
                    favorite.desc = upInfo.sign;
                    favorite.mid = mid;
                    favorite.faceUrl = upInfo.face;
                    favorite.name = upInfo.name;

                    favoriteDatabaseUtils.addFavorite(favorite);
                }

                //通知数据已更改
                new FavoriteAdapter(favoriteDatabaseUtils.queryFavorites(), getApplicationContext()).notifyDataSetChanged();

                break;
            case R.id.user_textView_video:
                up_viewPage.setCurrentItem(0);
                break;
            case R.id.user_textView_audio:
                up_viewPage.setCurrentItem(1);
                break;
            case R.id.user_textView_articles:
                up_viewPage.setCurrentItem(2);
                break;
            case R.id.user_textView_picture:
                up_viewPage.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * 权限回调
     *
     * @param requestCode   请求码
     * @param permissions   文件读写权限
     * @param grantResults  授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1024) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "权限申请成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}