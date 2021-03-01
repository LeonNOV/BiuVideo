package com.leon.biuvideo.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.views.BottomSheetTopBar;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.values.apis.AmapAPIs;
import com.leon.biuvideo.values.apis.AmapKey;

import java.util.HashMap;
import java.util.Map;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 设置页面
 */
public class SettingsFragment extends SupportFragment implements View.OnClickListener {
    private Context context;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch settings_fragment_imgOriginalMode_switch;
    private BottomSheetDialog bottomSheetDialog;
    private EditText set_location_keyword;
    private TextView settings_fragment_location;
    private RecyclerView set_location_result;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        context = getContext();
        initView(view);
        return view;
    }

    private void initView(View view) {
        SimpleTopBar settings_fragment_topBar = view.findViewById(R.id.settings_fragment_topBar);
        settings_fragment_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                _mActivity.onBackPressed();
            }

            @Override
            public void onRight() {

            }
        });

        settings_fragment_imgOriginalMode_switch = view.findViewById(R.id.settings_fragment_imgOriginalMode_switch);
        settings_fragment_location = view.findViewById(R.id.settings_fragment_location);
        settings_fragment_location.setText(PreferenceUtils.getLocation(context) + "," + "未选择");

        view.findViewById(R.id.settings_fragment_imgOriginalMode).setOnClickListener(this);
        view.findViewById(R.id.settings_fragment_setLocation).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_fragment_imgOriginalMode:
                settings_fragment_imgOriginalMode_switch.setChecked(!settings_fragment_imgOriginalMode_switch.isChecked());
                break;
            case R.id.settings_fragment_setLocation:
                showBottomSheet();
                break;
            default:
                break;
        }
    }

    /**
     * 显示BottomSheet
     */
    private void showBottomSheet() {
        View view = View.inflate(context, R.layout.settings_fragment_set_location_bottom_sheet, null);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        ((BottomSheetTopBar) view.findViewById(R.id.set_location_topBar)).setOnCloseListener(new BottomSheetTopBar.OnCloseListener() {
            @Override
            public void OnClose() {
                bottomSheetDialog.dismiss();
            }
        });

        set_location_keyword = view.findViewById(R.id.set_location_keyword);
        set_location_result = view.findViewById(R.id.set_location_result);

        view.findViewById(R.id.set_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = set_location_keyword.getText().toString();
                // 获取结果
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> params = new HashMap<>();
                        params.put("key", AmapKey.amapKey);
                        params.put("keywords", keyword);
                        params.put("subdistrict", "0");
                        params.put("filter", PreferenceUtils.getAdcode(context));

                        HttpUtils httpUtils = new HttpUtils(AmapAPIs.amapDistrict, params);
                        JSONObject response = JSONObject.parseObject(httpUtils.getData());
                        JSONArray districts = response.getJSONArray("districts");

                        Map<String, String> districtsMap = new HashMap<>();
                        for (Object district : districts) {
                            JSONObject object = (JSONObject) district;
                            districtsMap.put(object.getString("adcode"), object.getString("name"));
                        }

                        Log.d("Fuck-blue", districtsMap.toString());
                    }
                }).start();

                // 搜索完之后隐藏软键盘
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        // 设置底部透明
        FrameLayout bottom = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        if (bottom != null) {
            bottom.setBackgroundResource(android.R.color.transparent);
        }
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetDialog.show();
    }
}
