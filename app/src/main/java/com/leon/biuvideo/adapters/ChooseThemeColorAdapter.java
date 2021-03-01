package com.leon.biuvideo.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;

import java.util.List;

public class ChooseThemeColorAdapter extends BaseAdapter<String> {
    private final List<String> colors;
    private final Context context;
    private final int selectedThemeColorPosition;
    private final SharedPreferences initValue;

    public ChooseThemeColorAdapter(List<String> colors, Context context) {
        super(colors, context);

        this.colors = colors;
        this.context = context;
        this.initValue = context.getSharedPreferences(context.getResources().getString(R.string.preference), Context.MODE_PRIVATE);
        this.selectedThemeColorPosition = initValue.getInt("themeColorPosition", 12);
    }

    private ColorItemClickListener colorItemClickListener;

    public interface ColorItemClickListener {
        void onClick(int position);
    }

    public void setColorItemClickListener(ColorItemClickListener colorItemClickListener) {
        this.colorItemClickListener = colorItemClickListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.choose_theme_color_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        String color = this.colors.get(position);
        String[] split = color.split("\\|");

        int colorId = Integer.parseInt(split[1]);
        if (selectedThemeColorPosition == position) {
            holder.setVisibility(R.id.choose_theme_color_chooseState, View.VISIBLE);
        }

        holder
                .setText(R.id.choose_theme_color_name, split[0])
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (colorItemClickListener != null) {
                            // 获取之前的position，去除选中状态
                            int beforePosition = initValue.getInt("themeColorPosition", 12);
                            parent.getChildAt(beforePosition).findViewById(R.id.choose_theme_color_chooseState).setVisibility(View.GONE);

                            // 显示当前的设置状态
                            holder.setVisibility(R.id.choose_theme_color_chooseState, View.VISIBLE);

                            colorItemClickListener.onClick(position);
                        }
                    }
                })
                .findById(R.id.choose_theme_color_item).setBackgroundTintList(ColorStateList.valueOf(context.getColor(colorId)));
    }
}
