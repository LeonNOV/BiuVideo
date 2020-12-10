package com.leon.biuvideo.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.AboutBean;

import java.util.List;

/**
 * 关于dialog适配器
 */
public class AboutDialogAdapter extends BaseAdapter<AboutBean> {
    private final List<AboutBean> aboutBeans;
    private final Context context;

    public AboutDialogAdapter(List<AboutBean> aboutBeans, Context context) {
        super(aboutBeans, context);
        this.aboutBeans = aboutBeans;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.about_dialog_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        AboutBean aboutBean = aboutBeans.get(position);

        //设置标题
        holder.setText(R.id.about_dialog_item_textView_title, aboutBean.title)

                //设置链接
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentOriginUrl = new Intent();
                        intentOriginUrl.setAction("android.intent.action.VIEW");
                        intentOriginUrl.setData(Uri.parse(aboutBean.orgUrl));
                        context.startActivity(intentOriginUrl);
                    }
                })

                //设置内容
                .setText(R.id.about_dialog_item_textView_desc, aboutBean.desc);
    }
}