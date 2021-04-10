package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.beans.upMasterBean.BiliUserInfo;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.utils.parseDataUtils.BiliUserParser;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.Role;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/8
 * @Desc 用户界面
 */
public class BiliUserFragment extends BaseSupportFragment {
    private final String mid;

    private BiliUserInfo biliUserInfo;
    private int[] status;
    private int[] upStatus;

    private MainActivity.OnTouchListener onTouchListener;

    public BiliUserFragment(String mid) {
        this.mid = mid;
    }

    @Override
    protected int setLayout() {
        return R.layout.bili_user_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.bili_user_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                initViewData();
            }
        });

        getUserInfo();
    }

    /**
     * 初始化界面数据
     */
    private void initViewData() {
        Glide
                .with(context)
                .load(biliUserInfo.topPhoto += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ?
                        ImagePixelSize.COVER.value : "")
                .into((ImageView) findView(R.id.bili_user_banner));

        Glide
                .with(context)
                .load(biliUserInfo.userFace += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ?
                        ImagePixelSize.FACE.value : "")
                .into((ImageView) findView(R.id.bili_user_face));

        TextView biliUserName = findView(R.id.bili_user_name);
        biliUserName.setText(biliUserInfo.userName);
        if (biliUserInfo.isVip) {
            biliUserName.setTextColor(context.getColor(R.color.BiliBili_pink));
        }

        TextView biliUserFollow = findView(R.id.bili_user_follow);
        biliUserFollow.setSelected(biliUserInfo.attentionState);
        Drawable wrap = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ripple_round_corners6dp_bg));
        if (biliUserInfo.attentionState) {
            DrawableCompat.setTint(wrap, context.getColor(R.color.infoColor));
            biliUserFollow.setBackground(wrap);
            biliUserFollow.setText("已关注");
        } else {
            DrawableCompat.setTint(wrap, context.getColor(R.color.BiliBili_pink));
            biliUserFollow.setBackground(wrap);
            biliUserFollow.setText("关注");
        }

        ImageView biliUserGender = findView(R.id.bili_user_gender);
        switch (biliUserInfo.gender) {
            case "男":
                biliUserGender.setImageResource(R.drawable.bili_user_gender_man);
                break;
            case "女":
                biliUserGender.setImageResource(R.drawable.bili_user_gender_woman);
                break;
            default:
                biliUserGender.setVisibility(View.GONE);
                break;
        }

        ((ImageView) findView(R.id.bili_user_level)).getDrawable().setLevel(biliUserInfo.level);

        TextView biliUserVipMark = findView(R.id.bili_user_vipMark);
        if (biliUserInfo.vipLabel != null) {
            biliUserVipMark.setText(biliUserInfo.vipLabel);
        } else {
            biliUserVipMark.setVisibility(View.GONE);
        }

        ((ExpandableTextView) findView(R.id.bili_user_sign))
                .setText(biliUserInfo.sign == null ? context.getText(R.string.default_sign) : biliUserInfo.sign);


        String[] tabLayoutTitles = {"视频", "音频", "专栏", "相簿"};

        TabLayout biliUserTabLayout = findView(R.id.bili_user_tabLayout);
        ViewPager2 biliUserViewPager = findView(R.id.bili_user_viewPager);

        List<Fragment> viewPagerFragments = new ArrayList<>(4);
        viewPagerFragments.add(new BiliUserVideosFragment(mid));
        viewPagerFragments.add(new BiliUserAudiosFragment(mid));
        viewPagerFragments.add(new BiliUserArticlesFragment(mid));
        viewPagerFragments.add(new BiliUserPicturesFragment(mid));

        biliUserViewPager.setAdapter(new ViewPager2Adapter(this, viewPagerFragments));

        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), biliUserTabLayout, biliUserViewPager, tabLayoutTitles, 3);

        if (biliUserInfo.role == Role.NONE) {
            findView(R.id.bili_user_verify_container).setVisibility(View.GONE);
        } else {
            String verifySign;

            ImageView biliUserVerifyMark = findView(R.id.bili_user_verify_mark);
            if (biliUserInfo.role == Role.PERSON) {
                verifySign = "bilibili个人认证: ";
                biliUserVerifyMark.setImageResource(R.drawable.ic_person_verify);
            } else {
                biliUserVerifyMark.setImageResource(R.drawable.ic_official_verify);
                verifySign = "bilibili机构认证: ";
            }

            ((TextView) findView(R.id.bili_user_bili_user_verify_sign)).setText(verifySign + biliUserInfo.verifyDesc);
        }

        ((TagView) findView(R.id.bili_user_following)).setLeftValue(ValueUtils.generateCN(status[0]));
        ((TagView) findView(R.id.bili_user_fans)).setLeftValue(ValueUtils.generateCN(status[1]));
        ((TagView) findView(R.id.bili_user_play)).setLeftValue(ValueUtils.generateCN(upStatus[0]));
        ((TagView) findView(R.id.bili_user_read)).setLeftValue(ValueUtils.generateCN(upStatus[1]));
        ((TagView) findView(R.id.bili_user_like)).setLeftValue(ValueUtils.generateCN(upStatus[2]));
    }

    /**
     * 获取B站用户数据
     */
    private void getUserInfo () {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                BiliUserParser biliUserParser = new BiliUserParser(mid);
                biliUserParser.setOnCallBack(new BiliUserParser.OnCallBack() {
                    @Override
                    public void callBack(BiliUserInfo biliUserInfo, int[] status, int[] upStatus) {
                        BiliUserFragment.this.biliUserInfo = biliUserInfo;
                        BiliUserFragment.this.status = status;
                        BiliUserFragment.this.upStatus = upStatus;

                        Message message = receiveDataHandler.obtainMessage();
                        receiveDataHandler.sendMessage(message);
                    }
                });
                biliUserParser.parseData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 取消注冊触摸事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);
    }

    public static void changeText (int position, Map<Integer, TextView> textViewMap, Context context) {
        for (Map.Entry<Integer, TextView> entry : textViewMap.entrySet()) {
            if (entry.getKey() == position) {
                TextView textView = entry.getValue();
                textView.setTextColor(context.getColor(R.color.BiliBili_pink));
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setBackground(context.getDrawable(R.drawable.round_corners6dp_bg));
            } else {
                TextView textView = entry.getValue();
                textView.setTextColor(context.getColor(R.color.black));
                textView.setTypeface(Typeface.DEFAULT);
                textView.setBackground(context.getDrawable(R.drawable.ripple_round_corners6dp_bg));
            }
        }
    }
}
