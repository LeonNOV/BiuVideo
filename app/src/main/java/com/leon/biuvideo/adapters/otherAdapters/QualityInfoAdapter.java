package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.utils.Fuck;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/19
 * @Desc
 */
public class QualityInfoAdapter extends BaseAdapter<String[]> {
    private final List<String[]> qualityList;
    private int currentQuality;

    public QualityInfoAdapter(int currentQuality, List<String[]> beans, Context context) {
        super(beans, context);
        this.currentQuality = currentQuality;
        this.qualityList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        String[] strings = qualityList.get(position);

        TextView textView = holder.findById(android.R.id.text1);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        if (currentQuality == Integer.parseInt(strings[0])) {
            textView.setTextColor(Color.parseColor("#FB7299"));
        } else {
            textView.setTextColor(Color.WHITE);
        }
        Fuck.blue(strings[1]);
        textView.setText(strings[1]);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, strings[0] + "----" + strings[1], Toast.LENGTH_SHORT).show();
            }
        });
    }

    
}
