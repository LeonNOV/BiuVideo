package com.leon.biuvideo.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.AboutBean;

import java.util.List;

/**
 * 关于dialog适配器
 */
public class AboutDialogAdapter extends BaseAdapter<AboutBean> {
    private List<AboutBean> aboutBeans;

    public AboutDialogAdapter(List<AboutBean> aboutBeans, Context context) {
        super(aboutBeans, context);
        this.aboutBeans = aboutBeans;
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

                //设置声明
                .setText(R.id.about_dialog_item_textView_license, aboutBean.license)

                //设置内容
                .setText(R.id.about_dialog_item_textView_desc, aboutBean.desc);
    }
}