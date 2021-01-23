package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;

public class LoadingDialog extends AlertDialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setCanceledOnTouchOutside(false);
    }
}
