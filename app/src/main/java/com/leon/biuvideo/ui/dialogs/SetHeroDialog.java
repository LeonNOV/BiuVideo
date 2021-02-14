package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.HeroImages;

/**
 * 设置主界面‘Hero’弹窗
 */
public class SetHeroDialog extends AlertDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private SwitchCompat set_hero_dialog_switch;
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
        set_hero_dialog_switch = findViewById(R.id.set_hero_dialog_switch);
        set_hero_dialog_switch.setOnCheckedChangeListener(this);

        set_hero_spinner = findViewById(R.id.set_hero_dialog_spinner);
        set_hero_spinner.setOnItemSelectedListener(this);

        set_hero_imageView_preview = findViewById(R.id.set_hero_dialog_imageView_preview);

        TextView set_hero_textView_save = findViewById(R.id.set_hero_dialog_textView_save);
        set_hero_textView_save.setOnClickListener(this);

        TextView set_hero_textView_close = findViewById(R.id.set_hero_dialog_textView_close);
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
            set_hero_dialog_switch.setChecked(true);

            //设置上一次设置的heroIndex
            set_hero_spinner.setEnabled(false);
        } else {
            set_hero_dialog_switch.setChecked(false);

            //设置上一次设置的heroIndex
            set_hero_spinner.setEnabled(true);
        }

        set_hero_spinner.setSelection(customHeroIndex);
        set_hero_imageView_preview.setImageResource(HeroImages.heroImages[customHeroIndex]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_hero_dialog_textView_save:
                //保存当前选项
                SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = initValues.edit();
                editor.putBoolean("isAutoChange", isAutoChange);
                editor.putInt("customHeroIndex", customHeroIndex);
                editor.apply();

                dismiss();
                break;
            case R.id.set_hero_dialog_textView_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isAutoChange = isChecked;
        set_hero_spinner.setEnabled(!isChecked);
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
