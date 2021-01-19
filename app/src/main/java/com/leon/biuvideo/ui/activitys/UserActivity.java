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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.beans.upMasterBean.UserInfo;
import com.leon.biuvideo.ui.fragments.userFragments.UserArticlesFragment;
import com.leon.biuvideo.ui.fragments.userFragments.UserAudiosFragment;
import com.leon.biuvideo.ui.fragments.userFragments.UserPicturesFragment;
import com.leon.biuvideo.ui.fragments.userFragments.UserVideosFragment;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.UserInfoParser;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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
    private UserInfo userInfo;

    private FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;

    private Handler handler;
    private Map<Integer, TextView> textViewMap;
    private CoordinatorLayout user_linearLayout;

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
        initValue();
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
    }

    //初始化数据
    private void initValue() {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.FavoriteUp);
        favoriteUserDatabaseUtils = (FavoriteUserDatabaseUtils) sqLiteHelperFactory.getInstance();

        //获取mid
        Intent intent = getIntent();
        mid = intent.getLongExtra("mid", -1);

        if (mid == -1) {
            Snackbar.make(user_linearLayout, "信息获取失败", Snackbar.LENGTH_SHORT).show();
            onDestroy();
        }

        //更新visit
        favoriteUserDatabaseUtils.updateVisit(mid);

        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(SimpleThreadPool.LoadTaskNum, SimpleThreadPool.LoadTask);
        simpleThreadPool.submit(new FutureTask<>(new UserActivityThread()), "loadUserInfo");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                userInfo = (UserInfo) msg.getData().getSerializable("userInfo");

                if (userInfo != null) {
                    setValue(mid);
                }

                return true;
            }
        });
    }

    //设置控件的数据
    private void setValue(long mid) {
        //设置顶部图片
        Glide.with(getApplicationContext()).load(userInfo.topPhoto).into(up_imageView_cover);

        //设置头像
        Glide.with(getApplicationContext()).load(userInfo.face + ImagePixelSize.FACE.value).into(up_circleImageView_face);

        //设置昵称
        up_textView_name.setText(userInfo.name);

        //设置签名
        up_textView_sign.setText(userInfo.sign);

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

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);

        up_viewPage.setAdapter(viewPageAdapter);
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

                    Snackbar.make(v, removeState ? "已取消关注" : "取消关注失败", Snackbar.LENGTH_SHORT).show();
                } else {
                    //添加至数据库中
                    up_imageView_favoriteIconState.setImageResource(R.drawable.favorite);
                    up_textView_favoriteStrState.setText("已关注");

                    Favorite favorite = new Favorite();
                    favorite.desc = userInfo.sign;
                    favorite.mid = mid;
                    favorite.faceUrl = userInfo.face;
                    favorite.name = userInfo.name;

                    boolean addState = favoriteUserDatabaseUtils.addFavorite(favorite);

                    Snackbar.make(v, addState ? "已加入至关注列表" : "关注失败", Snackbar.LENGTH_SHORT).show();
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
                Snackbar.make(user_linearLayout, "权限申请成功", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(user_linearLayout, "权限申请失败", Snackbar.LENGTH_SHORT).show();
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

    private class UserActivityThread implements Callable<String> {

        @Override
        public String call() {
            UserInfoParser userInfoParser = new UserInfoParser(getApplicationContext());
            UserInfo userInfo = userInfoParser.parseUpInfo(mid);

//            Message message = new Message();
            Message message = handler.obtainMessage();
            message.what = 0;

            Bundle bundle = new Bundle();
            bundle.putSerializable("userInfo", userInfo);

            message.setData(bundle);
            handler.sendMessage(message);

            return null;
        }
    }
}