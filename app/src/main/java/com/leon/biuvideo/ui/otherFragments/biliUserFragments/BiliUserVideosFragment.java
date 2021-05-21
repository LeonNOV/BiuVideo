package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import android.view.View;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.biliUserResourcesAdapters.BiliUserVideosAdapter;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils.BiliUserVideoParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/9
 * @Desc B站用户视频数据
 */
public class BiliUserVideosFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private final String mid;
    private final Map<Integer, TextView> textViewMap = new HashMap<>(3);

    private String order = BiliUserVideoParser.ORDER_DEFAULT;
    private DataLoader<BiliUserVideo> biliUserVideoDataLoader;

    public BiliUserVideosFragment(String mid) {
        this.mid = mid;
    }

    @Override
    protected int setLayout() {
        return R.layout.bili_user_videos_fragment;
    }

    @Override
    protected void initView() {
        TextView biliUserVideosOrderDefault = findView(R.id.bili_user_videos_order_default);
        biliUserVideosOrderDefault.setOnClickListener(this);
        textViewMap.put(0, biliUserVideosOrderDefault);

        TextView biliUserVideosOrderClick = findView(R.id.bili_user_videos_order_click);
        biliUserVideosOrderClick.setOnClickListener(this);
        textViewMap.put(1, biliUserVideosOrderClick);

        TextView biliUserVideosOrderStow = findView(R.id.bili_user_videos_order_stow);
        biliUserVideosOrderStow.setOnClickListener(this);
        textViewMap.put(2, biliUserVideosOrderStow);

        biliUserVideoDataLoader = new DataLoader<>(context, new BiliUserVideoParser(mid, order),
                R.id.bili_user_videos_data,
                new BiliUserVideosAdapter(getMainActivity(), context),
                this);
    }

    @Override
    protected void onLazyLoad() {
        biliUserVideoDataLoader.insertData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bili_user_videos_order_default:
                if (order.equals(BiliUserVideoParser.ORDER_DEFAULT)) {
                    return;
                }

                order = null;
                BiliUserFragment.changeText(0, textViewMap, context);
                break;
            case R.id.bili_user_videos_order_click:
                if (order.equals(BiliUserVideoParser.ORDER_CLICK)) {
                    return;
                }

                order = BiliUserVideoParser.ORDER_CLICK;
                BiliUserFragment.changeText(1, textViewMap, context);
                break;
            case R.id.bili_user_videos_order_stow:
                if (order.equals(BiliUserVideoParser.ORDER_STOW)) {
                    return;
                }

                order = BiliUserVideoParser.ORDER_STOW;
                BiliUserFragment.changeText(2, textViewMap, context);
                break;
            default:
                break;
        }

        biliUserVideoDataLoader.reset(new BiliUserVideoParser(mid, order));
    }
}
