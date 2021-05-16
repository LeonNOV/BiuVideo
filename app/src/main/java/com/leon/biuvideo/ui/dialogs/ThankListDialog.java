package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.values.ThanksList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/15
 * @Desc 感谢列表弹窗
 */
public class ThankListDialog extends AlertDialog {
    private final Context context;

    public ThankListDialog(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thanks_list_dialog);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setWindowAnimations(R.style.paning_anim_style);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);

        initView();
    }

    private void initView() {
        findViewById(R.id.thanks_list_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        List<String[]> thanksDataList = new ArrayList<>();
        for (int i = 0; i < ThanksList.THANKS_LIST_TITLES.length; i++) {
            thanksDataList.add(new String[]{ThanksList.THANKS_LIST_TITLES[i]
            ,ThanksList.THANKS_LIST_DESC[i], ThanksList.THANKS_LIST_URL[i]});
        }

        LoadingRecyclerView thanksListData = findViewById(R.id.thanks_list_data);
        ThanksListAdapter thanksListAdapter = new ThanksListAdapter(thanksDataList, context);
        thanksListAdapter.setHasStableIds(true);
        thanksListData.setRecyclerViewAdapter(thanksListAdapter);

        thanksListData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }

    private static class ThanksListAdapter extends BaseAdapter<String[]> {
        private final List<String[]> thankDataList;

        public ThanksListAdapter(List<String[]> beans, Context context) {
            super(beans, context);

            this.thankDataList = beans;
        }

        @Override
        public int getLayout(int viewType) {
            return R.layout.thanks_list_item;
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
            String[] item = thankDataList.get(position);

            holder
                    .setText(R.id.thanks_list_item_title, item[0])
                    .setText(R.id.thanks_list_item_desc, item[1])
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (InternetUtils.checkNetwork(v)) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(Uri.parse(item[2]));
                                context.startActivity(intent);
                            }
                        }
                    });
        }
    }
}
