package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.activitys.SearchResultActivity;
import com.leon.biuvideo.ui.activitys.UserActivity;
import com.leon.biuvideo.ui.activitys.VideoActivity;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.IDUtils;
import com.leon.biuvideo.utils.InternetUtils;

/**
 * 主fragment
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText home_fragment_editText_keyword;

    //spinner索引
    private int spinnerIndex;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_home;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {

        Spinner home_spinner = findView(R.id.home_spinner);
        home_spinner.setOnItemSelectedListener(this);

        home_fragment_editText_keyword = findView(R.id.home_fragment_editText_keyword);

        bindingUtils
                .setOnClickListener(R.id.home_button_confirm, this)
                .setOnClickListener(R.id.home_fragment_imageView_clear, this);
    }

    @Override
    public void initValues() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_fragment_imageView_clear:
                home_fragment_editText_keyword.getText().clear();
                break;
            case R.id.home_button_confirm:
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                if (!isHaveNetwork) {
                    SimpleSnackBar.make(view, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                    return;
                }

                switch (spinnerIndex) {
                    case 0:
                        //获取输入内容
                        String keywordUnCoded = home_fragment_editText_keyword.getText().toString();
                        if (!keywordUnCoded.equals("")) {

                            Intent intent = new Intent(context, SearchResultActivity.class);
                            intent.putExtra("keyword", keywordUnCoded);
                            startActivity(intent);
                        } else {
                            SimpleSnackBar.make(view, "不输点儿啥吗？", SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    //获取数据，进入VideoActivity
                    case 1:
                        String value_bvid = home_fragment_editText_keyword.getText().toString().trim();

                        if (value_bvid.length() != 0) {
                            String bvid = IDUtils.getBvid(value_bvid);

                            if (bvid != null) {
                                Intent intent = new Intent(context, VideoActivity.class);
                                intent.putExtra("bvid", bvid);
                                startActivity(intent);
                            } else {
                                SimpleSnackBar.make(view, "使用姿势有点不对哦~\n可通过“帮助”来了解正确的姿势", SimpleSnackBar.LENGTH_SHORT).show();
                            }
                        }
                        break;

                    //获取数据，进入UpMasterActivity
                    case 2:
                        //获取mid
                        String value_mid = home_fragment_editText_keyword.getText().toString().trim();

                        if (value_mid.length() != 0) {

                            long mid = IDUtils.getMid(value_mid);

                            if (mid != 0) {
                                Intent intent = new Intent(context, UserActivity.class);
                                intent.putExtra("mid", mid);
                                startActivity(intent);
                            } else {
                                SimpleSnackBar.make(view, "使用姿势有点不对哦~\n可通过“帮助”来了解正确的姿势", SimpleSnackBar.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    default:
                        break;
                }

                break;
            default:
                break;
        }
    }

    /**
     * PreferenceActivity被onDestroy后调用该方法，用来初始化Hero
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initValues();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            home_fragment_editText_keyword.setHint(R.string.main_editText_hint1);
        } else {
            home_fragment_editText_keyword.setHint(R.string.main_editText_hint2);
        }
        spinnerIndex = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
