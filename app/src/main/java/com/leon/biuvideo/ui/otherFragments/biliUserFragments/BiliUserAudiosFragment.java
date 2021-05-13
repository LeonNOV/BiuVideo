package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import android.view.View;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.biliUserResourcesAdapters.BiliUserAudioAdapter;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserAudio;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils.BiliUserAudiosParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/9
 * @Desc B站用户音频数据
 */
public class BiliUserAudiosFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private final String mid;
    private final Map<Integer, TextView> textViewMap = new HashMap<>(3);

    private int order = 1;
    private DataLoader<BiliUserAudio> biliUserAudioDataLoader;

    public BiliUserAudiosFragment(String mid) {
        this.mid = mid;
    }

    @Override
    protected int setLayout() {
        return R.layout.bili_user_audios_fragment;
    }

    @Override
    protected void initView() {
        TextView biliUserAudiosOrderDefault = findView(R.id.bili_user_audios_order_default);
        biliUserAudiosOrderDefault.setOnClickListener(this);
        textViewMap.put(0, biliUserAudiosOrderDefault);

        TextView biliUserAudiosOrderClick = findView(R.id.bili_user_audios_order_click);
        biliUserAudiosOrderClick.setOnClickListener(this);
        textViewMap.put(1, biliUserAudiosOrderClick);

        TextView biliUserAudiosOrderStow = findView(R.id.bili_user_audios_order_stow);
        biliUserAudiosOrderStow.setOnClickListener(this);
        textViewMap.put(2, biliUserAudiosOrderStow);

        biliUserAudioDataLoader = new DataLoader<>(context, new BiliUserAudiosParser(mid, order), R.id.bili_user_audios_data,
                new BiliUserAudioAdapter(context), this);
    }

    @Override
    protected void onLazyLoad() {
        biliUserAudioDataLoader.insertData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bili_user_audios_order_default:
                if (order == BiliUserAudiosParser.ORDER_DEFAULT) {
                    return;
                }

                order = 1;
                BiliUserFragment.changeText(0, textViewMap, context);
                break;
            case R.id.bili_user_audios_order_click:
                if (order == BiliUserAudiosParser.ORDER_CLICK) {
                    return;
                }

                order = BiliUserAudiosParser.ORDER_CLICK;
                BiliUserFragment.changeText(1, textViewMap, context);
                break;
            case R.id.bili_user_audios_order_stow:
                if (order == BiliUserAudiosParser.ORDER_STOW) {
                    return;
                }

                order = BiliUserAudiosParser.ORDER_STOW;
                BiliUserFragment.changeText(2, textViewMap, context);
                break;
            default:
                break;
        }

        reset();
    }

    private void reset () {
        biliUserAudioDataLoader.reset(new BiliUserAudiosParser(mid, order));
    }
}
