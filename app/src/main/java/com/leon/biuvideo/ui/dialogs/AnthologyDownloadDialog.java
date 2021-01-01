package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.AnthologyDownloadDialogAdapter;
import com.leon.biuvideo.beans.videoBean.view.AnthologyInfo;
import com.leon.biuvideo.ui.activitys.DownloadedActivity;

import java.util.List;

public class AnthologyDownloadDialog extends AlertDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final List<AnthologyInfo> anthologyInfoList;
    private final Context context;

    private RecyclerView anthology_download_dialog_recyclerView;

    public AnthologyDownloadDialog(@NonNull Context context, List<AnthologyInfo> anthologyInfoList) {
        super(context);
        this.context = context;
        this.anthologyInfoList = anthologyInfoList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anthology_download_dialog);

        initView();
        initValue();
    }

    private void initView() {
        Spinner anthology_download_dialog_spinner = findViewById(R.id.anthology_download_dialog_spinner);
        anthology_download_dialog_spinner.setOnItemSelectedListener(this);

        ImageView anthology_download_dialog_imageView_close = findViewById(R.id.anthology_download_dialog_imageView_close);
        anthology_download_dialog_imageView_close.setOnClickListener(this);

        TextView anthology_download_dialog_textView_saveAll = findViewById(R.id.anthology_download_dialog_textView_saveAll);
        anthology_download_dialog_textView_saveAll.setOnClickListener(this);

        TextView anthology_download_dialog_textView_checkAll = findViewById(R.id.anthology_download_dialog_textView_checkAll);
        anthology_download_dialog_textView_checkAll.setOnClickListener(this);

        anthology_download_dialog_recyclerView = findViewById(R.id.anthology_download_dialog_recyclerView);
    }

    private void initValue() {
        anthology_download_dialog_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        anthology_download_dialog_recyclerView.setAdapter(new AnthologyDownloadDialogAdapter(anthologyInfoList, context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anthology_download_dialog_imageView_close:
                dismiss();
                break;
            case R.id.anthology_download_dialog_textView_checkAll:
                Intent intent = new Intent(context, DownloadedActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
