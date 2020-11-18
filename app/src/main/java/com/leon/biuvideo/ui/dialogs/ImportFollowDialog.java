package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;

public class ImportFollowDialog extends AlertDialog implements View.OnClickListener {
    private EditText import_follow_editText;
    private Button import_follow_button_cancel, import_follow_button_confirm;

    protected ImportFollowDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_follow_dialog);

        initView();
    }

    private void initView() {
        import_follow_editText = findViewById(R.id.import_follow_editText);

        import_follow_button_cancel = findViewById(R.id.import_follow_button_cancel);
        import_follow_button_cancel.setOnClickListener(this);

        import_follow_button_confirm = findViewById(R.id.import_follow_button_confirm);
        import_follow_button_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.import_follow_button_confirm:



                break;
            case R.id.import_follow_button_cancel:



                break;
            default:
                break;
        }
    }
}