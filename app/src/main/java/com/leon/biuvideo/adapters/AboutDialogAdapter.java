package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.AboutBean;

import java.util.List;

/**
 * 关于dialog适配器
 */
public class AboutDialogAdapter extends RecyclerView.Adapter<AboutDialogAdapter.ViewHolder> {
    private List<AboutBean> aboutBeans;
    private final Context context;

    public AboutDialogAdapter(List<AboutBean> aboutBeans, Context context) {
        this.aboutBeans = aboutBeans;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.about_dialog_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutDialogAdapter.ViewHolder holder, int position) {
        AboutBean aboutBean = aboutBeans.get(position);

        holder.about_dialog_item_textView_title.setText(aboutBean.title);
        holder.about_dialog_item_textView_license.setText(aboutBean.license);
        holder.about_dialog_item_textView_desc.setText(aboutBean.desc);
    }

    @Override
    public int getItemCount() {
        return aboutBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                about_dialog_item_textView_title,
                about_dialog_item_textView_license,
                about_dialog_item_textView_desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            about_dialog_item_textView_title = itemView.findViewById(R.id.about_dialog_item_textView_title);
            about_dialog_item_textView_license = itemView.findViewById(R.id.about_dialog_item_textView_license);
            about_dialog_item_textView_desc = itemView.findViewById(R.id.about_dialog_item_textView_desc);
        }
    }
}
