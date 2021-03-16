package com.leon.biuvideo.ui.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.FragmentViewPagerAdapter;
import com.leon.biuvideo.beans.Follow;
import com.leon.biuvideo.beans.upMasterBean.BiliUserInfo;
import com.leon.biuvideo.ui.dialogs.LoadingDialog;
import com.leon.biuvideo.ui.fragments.userFragments.UserArticlesFragment;
import com.leon.biuvideo.ui.fragments.userFragments.UserAudiosFragment;
import com.leon.biuvideo.ui.fragments.userFragments.UserPicturesFragment;
import com.leon.biuvideo.ui.fragments.userFragments.UserVideosFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.BiliUserInfoParser;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户界面activity
 * 由于用户粉丝数、获赞数和观看数的获取需要使用cookie获取，所以暂不添加该三项
 */
public class UserActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ImageView up_imageView_cover;
    private CircleImageView up_circleImageView_face;
    private TextView up_textView_name;
    private ImageView up_imageView_favoriteIconState;
    private TextView up_textView_favoriteStrState;
    private ExpandableTextView up_textView_sign;
    private ViewPager up_viewPage;

    private long mid;
    private BiliUserInfo biliUserInfo;

    private FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;

    private Handler handler;
    private Map<Integer, TextView> textViewMap;
    private CoordinatorLayout user_linearLayout;
    private LoadingDialog loadingDialog;

    public UserActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initView();
    }

    //初始化控件
    private void initView() {
        user_linearLayout = findViewById(R.id.user_linearLayout);
        up_imageView_cover = findViewById(R.id.up_imageView_cover);

        ImageView up_imageView_back = findViewById(R.id.up_imageView_back);
        up_imageView_back.setOnClickListener(this);

        up_circleImageView_face = findViewById(R.id.up_circleImageView_face);
        up_textView_name = findViewById(R.id.up_textView_name);

        up_imageView_favoriteIconState = findViewById(R.id.up_imageView_favoriteIconState);
        up_imageView_favoriteIconState.setOnClickListener(this);

        up_textView_favoriteStrState = findViewById(R.id.up_textView_favoriteStrState);

        up_textView_sign = findViewById(R.id.up_textView_sign);

        textViewMap = new HashMap<>();
        TextView user_textView_video = findViewById(R.id.user_textView_video);
        user_textView_video.setOnClickListener(this);
        textViewMap.put(0, user_textView_video);

        TextView user_textView_audio = findViewById(R.id.user_textView_audio);
        user_textView_audio.setOnClickListener(this);
        textViewMap.put(1, user_textView_audio);

        TextView user_textView_articles = findViewById(R.id.user_textView_articles);
        user_textView_articles.setOnClickListener(this);
        textViewMap.put(2, user_textView_articles);

        TextView user_textView_picture = findViewById(R.id.user_textView_picture);
        user_textView_picture.setOnClickListener(this);
        textViewMap.put(3, user_textView_picture);

        up_viewPage = findViewById(R.id.up_viewPage);
        up_viewPage.addOnPageChangeListener(this);
        up_viewPage.setOffscreenPageLimit(4);

        loadingDialog = new LoadingDialog(UserActivity.this);
        loadingDialog.show();

        loadData();
    }

    //初始化数据
    private void loadData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                //获取mid
                Intent intent = getIntent();
                mid = intent.getLongExtra("mid", -1);

                if (mid == -1) {
                    SimpleSnackBar.make(user_linearLayout, "信息获取失败", SimpleSnackBar.LENGTH_SHORT).show();
                    finish();
                }

                BiliUserInfoParser biliUserInfoParser = new BiliUserInfoParser();
                biliUserInfo = biliUserInfoParser.parseUpInfo(mid);

                favoriteUserDatabaseUtils = new FavoriteUserDatabaseUtils(getApplicationContext());

                //更新visit
                favoriteUserDatabaseUtils.updateVisit(mid);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");

                if (loadState) {
                    initValue();
                }
                loadingDialog.dismiss();

                return true;
            }
        });
    }

    //设置控件的数据
    private void initValue() {
        //设置顶部图片
        Glide.with(getApplicationContext()).load(biliUserInfo.topPhoto).into(up_imageView_cover);

        //设置头像
        Glide.with(getApplicationContext()).load(biliUserInfo.face + ImagePixelSize.FACE.value).into(up_circleImageView_face);

        //设置昵称
        up_textView_name.setText(biliUserInfo.name);

        //设置签名
        up_textView_sign.setText(biliUserInfo.sign);

        //获取关注状态
        boolean favorite_state = favoriteUserDatabaseUtils.queryFavoriteState(mid);

        if (favorite_state) {
            up_imageView_favoriteIconState.setImageResource(R.drawable.favorite);
            up_textView_favoriteStrState.setText("已关注");
        } else {
            up_imageView_favoriteIconState.setImageResource(R.drawable.no_favorite);
            up_textView_favoriteStrState.setText("未关注");
        }

        initViewPage();
    }

    //设置ViewPage
    private void initViewPage() {
        List<Fragment> fragments = new ArrayList<>();

        //获取视频数据
        fragments.add(new UserVideosFragment(mid));

        //获取音频数据
        fragments.add(new UserAudiosFragment(mid));

        //获取文章数据
        fragments.add(new UserArticlesFragment(mid));

        //获取相簿数据
        fragments.add(new UserPicturesFragment(mid));

        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);

        up_viewPage.setAdapter(fragmentViewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up_imageView_back:
                this.finish();
                break;
            case R.id.up_imageView_favoriteIconState:
                //判断是否存在于数据库中
                boolean state = favoriteUserDatabaseUtils.queryFavoriteState(mid);

                if (state) {
                    //从数据库中移除
                    up_imageView_favoriteIconState.setImageResource(R.drawable.no_favorite);
                    up_textView_favoriteStrState.setText("未关注");

                    boolean removeState = favoriteUserDatabaseUtils.removeFavorite(mid);

                    SimpleSnackBar.make(v, removeState ? "已取消关注" : "取消关注失败", SimpleSnackBar.LENGTH_SHORT).show();
                } else {
                    //添加至数据库中
                    up_imageView_favoriteIconState.setImageResource(R.drawable.favorite);
                    up_textView_favoriteStrState.setText("已关注");

                    Follow follow = new Follow();
                    follow.desc = biliUserInfo.sign;
                    follow.mid = mid;
                    follow.faceUrl = biliUserInfo.face;
                    follow.name = biliUserInfo.name;

                    boolean addState = favoriteUserDatabaseUtils.addFavorite(follow);

                    SimpleSnackBar.make(v, addState ? "已加入至关注列表" : "关注失败", SimpleSnackBar.LENGTH_SHORT).show();
                }

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
    public void onPageSelected(int position) {
        ViewUtils.changeText(textViewMap, position);
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
                SimpleSnackBar.make(user_linearLayout, "权限申请成功", SimpleSnackBar.LENGTH_SHORT).show();
            } else {
                SimpleSnackBar.make(user_linearLayout, "权限申请失败", SimpleSnackBar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (favoriteUserDatabaseUtils != null) {
            favoriteUserDatabaseUtils.close();
        }
        super.onDestroy();
    }
}