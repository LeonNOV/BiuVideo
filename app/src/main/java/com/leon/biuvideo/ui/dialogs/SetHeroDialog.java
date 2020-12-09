package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.fragments.mainFragments.HomeFragment;
import com.leon.biuvideo.utils.HeroImages;

public class SetHeroDialog extends AlertDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {
    private RadioGroup set_hero_radio_group;
    private RadioButton set_hero_radioButton_auto, set_hero_radioButton_custom;
    private Spinner set_hero_spinner;
    private ImageView set_hero_imageView_preview;

    private boolean isAutoChange;
    private int customHeroIndex;

    private Context context;

    public SetHeroDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_hero_dialog);

        //获取window
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        initView();
        initValue();
    }

    private void initView() {
        set_hero_radio_group = findViewById(R.id.set_hero_radio_group);
        set_hero_radio_group.setOnCheckedChangeListener(this);

        set_hero_radioButton_auto = findViewById(R.id.set_hero_radioButton_auto);
        set_hero_radioButton_custom = findViewById(R.id.set_hero_radioButton_custom);

        set_hero_spinner = findViewById(R.id.set_hero_spinner);
        set_hero_spinner.setOnItemSelectedListener(this);

        set_hero_imageView_preview = findViewById(R.id.set_hero_imageView_preview);

        TextView set_hero_textView_save = findViewById(R.id.set_hero_textView_save);
        set_hero_textView_save.setOnClickListener(this);

        TextView set_hero_textView_close = findViewById(R.id.set_hero_textView_close);
        set_hero_textView_close.setOnClickListener(this);
    }

    private void initValue() {
        context = getContext();

        //获取上一次保存的值
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        isAutoChange = initValues.getBoolean("isAutoChange", true);
        customHeroIndex = initValues.getInt("customHeroIndex", 0);

        //判断是否为自动更换
        if (isAutoChange) {
            set_hero_radioButton_auto.setChecked(true);

            //设置上一次设置的heroIndex
            set_hero_spinner.setEnabled(false);
        } else {
            set_hero_radioButton_custom.setChecked(true);

            //设置上一次设置的heroIndex
            set_hero_spinner.setEnabled(true);
        }

        set_hero_spinner.setSelection(customHeroIndex);
        set_hero_imageView_preview.setImageResource(HeroImages.heroImages[customHeroIndex]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_hero_textView_save:
                //保存当前选项
                SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = initValues.edit();
                editor.putBoolean("isAutoChange", isAutoChange);
                editor.putInt("customHeroIndex", customHeroIndex);
                editor.apply();

                dismiss();
                break;
            case R.id.set_hero_textView_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.set_hero_radioButton_auto:
                isAutoChange = true;
                set_hero_radioButton_auto.setTypeface(Typeface.DEFAULT_BOLD);
                set_hero_radioButton_custom.setTypeface(Typeface.DEFAULT);
                set_hero_spinner.setEnabled(false);

                break;
            case R.id.set_hero_radioButton_custom:
                isAutoChange = false;
                set_hero_radioButton_auto.setTypeface(Typeface.DEFAULT);
                set_hero_radioButton_custom.setTypeface(Typeface.DEFAULT_BOLD);
                set_hero_spinner.setEnabled(true);

                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.customHeroIndex = position;
        set_hero_imageView_preview.setImageResource(HeroImages.heroImages[customHeroIndex]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
