package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.biliUserResourcesAdapters.BiliUserAudioAdapter;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserAudio;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils.BiliUserAudiosParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/9
 * @Desc B站用户音频数据
 */
public class BiliUserAudiosFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private final String mid;
    private final List<BiliUserAudio> biliUserAudioList = new ArrayList<>();
    private final Map<Integer, TextView> textViewMap = new HashMap<>(3);

    private SmartRefreshRecyclerView<BiliUserAudio> biliUserAudiosData;

    private int order = 1;
    private BiliUserAudiosParser biliUserAudiosParser;
    private BiliUserAudioAdapter biliUserAudioAdapter;

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

        biliUserAudioAdapter = new BiliUserAudioAdapter(biliUserAudioList, context);
        biliUserAudioAdapter.setHasStableIds(true);
        biliUserAudiosData = findView(R.id.bili_user_audios_data);
        biliUserAudiosData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getAudios(1);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<BiliUserAudio> biliUserAudioList = (List<BiliUserAudio>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (biliUserAudioList != null && biliUserAudioList.size() > 0) {
                            biliUserAudioAdapter.append(biliUserAudioList);
                            biliUserAudiosData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        } else {
                            biliUserAudiosData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            biliUserAudiosData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;

                    case 1:
                        if (biliUserAudioList != null && biliUserAudioList.size() > 0) {
                            biliUserAudioAdapter.append(biliUserAudioList);

                            if (!biliUserAudiosParser.dataStatus) {
                                biliUserAudiosData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            }
                        } else {
                            biliUserAudiosData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        biliUserAudiosData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        getAudios(0);
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

    private void getAudios(int what) {
        if (biliUserAudiosParser == null) {
            biliUserAudiosParser = new BiliUserAudiosParser(mid, order);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<BiliUserAudio> biliUserAudioList = biliUserAudiosParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = biliUserAudioList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    private void reset () {
        biliUserAudiosParser = new BiliUserAudiosParser(mid, order);
        biliUserAudiosData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        biliUserAudioAdapter.removeAll();
        getAudios(0);
    }
}
