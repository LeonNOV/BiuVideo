package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;

public class SetHeroDialog extends AlertDialog {

    protected SetHeroDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_hero_dialog);

        initView();
    }

    private void initView() {

    }
}
