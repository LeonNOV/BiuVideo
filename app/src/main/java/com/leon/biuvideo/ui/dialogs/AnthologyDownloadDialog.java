package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userAdapters.AnthologyDownloadDialogAdapter;
import com.leon.biuvideo.beans.videoBean.view.AnthologyInfo;
import com.leon.biuvideo.ui.activitys.DownloadedActivity;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.values.Quality;

import java.util.List;

public class AnthologyDownloadDialog extends AlertDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final List<AnthologyInfo> anthologyInfoList;
    private final Context context;
    private int qualityId;
    public View view;

    private RecyclerView anthology_download_dialog_recyclerView;
    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;

    public AnthologyDownloadDialog(@NonNull Context context, List<AnthologyInfo> anthologyInfoList) {
        super(context);
        this.context = context;
        this.anthologyInfoList = anthologyInfoList;

        this.view = getWindow().getDecorView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_anthology_download);

        initView();
        initValue();
    }

    private OnDownloadListener onDownloadListener;

    public interface OnDownloadListener {
        void onDownload(int qualityId, long cid, int position, String subTitle);
        void onSaveAll(int qualityId);
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }

    private void initView() {
        Spinner anthology_download_dialog_spinner = findViewById(R.id.anthology_download_dialog_spinner);
        anthology_download_dialog_spinner.setSelection(3);
        anthology_download_dialog_spinner.setOnItemSelectedListener(this);

        BindingUtils bindingUtils = new BindingUtils(getWindow().getDecorView(), getContext());
        bindingUtils
                .setOnClickListener(R.id.anthology_download_dialog_textView_qualityWarn, this)
                .setOnClickListener(R.id.anthology_download_dialog_imageView_close, this)
                .setOnClickListener(R.id.anthology_download_dialog_textView_saveAll, this)
                .setOnClickListener(R.id.anthology_download_dialog_textView_checkAll, this);

        anthology_download_dialog_recyclerView = findViewById(R.id.anthology_download_dialog_recyclerView);
    }

    private void initValue() {
        anthology_download_dialog_recyclerView.setLayoutManager(new LinearLayoutManager(context));

        if (downloadRecordsDatabaseUtils == null) {
            downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(context);
        }

        for (AnthologyInfo anthologyInfo : anthologyInfoList) {
            anthologyInfo.isDownloaded = downloadRecordsDatabaseUtils.queryVideo(anthologyInfo.mainId, String.valueOf(anthologyInfo.cid));
        }

        AnthologyDownloadDialogAdapter anthologyDownloadDialogAdapter = new AnthologyDownloadDialogAdapter(anthologyInfoList, context);
        anthologyDownloadDialogAdapter.setOnAnthologyItemClickListener(new AnthologyDownloadDialogAdapter.OnAnthologyItemClickListener() {
            @Override
            public void onItemClickListener(long cid, int position, String subTitle) {
                if (onDownloadListener != null) {
                    onDownloadListener.onDownload(qualityId, cid, position, subTitle);
                }
            }
        });
        anthology_download_dialog_recyclerView.setAdapter(anthologyDownloadDialogAdapter);
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
            case R.id.anthology_download_dialog_textView_qualityWarn:
                //跳转到源网站
                Intent intentHelp = new Intent();
                intentHelp.setAction("android.intent.action.VIEW");
                intentHelp.setData(Uri.parse(context.getString(R.string.anthologyHelpLink)));
                context.startActivity(intentHelp);
                break;
            case R.id.anthology_download_dialog_textView_saveAll:
                if (onDownloadListener != null) {
                    onDownloadListener.onSaveAll(qualityId);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /*switch (position) {
            case 0:
                qualityId = Quality.F120;
                break;
            case 1:
                qualityId = Quality.F116;
                break;
            case 2:
                qualityId = Quality.F112;
                break;
            case 3:
                qualityId = Quality.F80;
                break;
            case 4:
                qualityId = Quality.F64;
                break;
            case 5:
                qualityId = Quality.F32;
                break;
            case 6:
                qualityId = Quality.F16;
                break;
            default:
                break;
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void dismiss() {
        if (downloadRecordsDatabaseUtils != null) {
            downloadRecordsDatabaseUtils.close();
        }
        super.dismiss();
    }
}
