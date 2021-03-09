package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.District;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/8
 * @Desc
 */
public class SettingChoiceAddressAdapter extends BaseAdapter<District> {
    private final List<District> districtList;

    public SettingChoiceAddressAdapter(List<District> beans, Context context) {
        super(beans, context);

        this.districtList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.settings_choice_address_item;
    }

    public interface OnSettingChoiceAddressListener {
        void onSelectAddress(District district);
    }

    private OnSettingChoiceAddressListener onSettingChoiceAddressListener;

    public void setOnSettingChoiceAddressListener(OnSettingChoiceAddressListener onSettingChoiceAddressListener) {
        this.onSettingChoiceAddressListener = onSettingChoiceAddressListener;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        District district = districtList.get(position);
        holder
                .setText(R.id.settings_choice_address_district, district.name)
                .setText(R.id.settings_choice_address_city, district.address[0] + "," + district.address[1] + "," + district.address[2])
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSettingChoiceAddressListener != null) {
                            onSettingChoiceAddressListener.onSelectAddress(district);
                        }
                    }
                });
    }
}
