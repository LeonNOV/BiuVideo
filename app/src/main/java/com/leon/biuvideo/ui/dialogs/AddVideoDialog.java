package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.orderAdapters.LocalOrderAddVideoFolderAdapter;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.beans.orderBeans.LocalVideoFolder;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.LocalOrdersDatabaseUtils;

import java.util.List;

public class AddVideoDialog extends AlertDialog implements View.OnClickListener {
    private final Context context;

    private RecyclerView video_add_recyclerView;
    private LocalOrdersDatabaseUtils localOrdersDatabaseUtils;
    private LocalOrderAddVideoFolderAdapter localOrderAddVideoFolderAdapter;

    public AddVideoDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_add_dialog);

        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);

        initView();
        initValues();
    }

    private OnAddOrderCallback onAddOrderCallback;

    public interface OnAddOrderCallback {
        LocalOrder callBack(LocalVideoFolder localVideoFolder);

        void onFavoriteIcon(boolean addState);
    }

    public void setOnAddOrderCallback(OnAddOrderCallback onAddOrderCallback) {
        this.onAddOrderCallback = onAddOrderCallback;
    }

    private void initView() {
        BindingUtils bindingUtils = new BindingUtils(getWindow().getDecorView(), context);
        bindingUtils
                .setOnClickListener(R.id.video_add_dialog_imageView_close, this)
                .setOnClickListener(R.id.video_add_dialog_imageView_add, this);

        video_add_recyclerView = findViewById(R.id.video_add_recyclerView);
    }

    private void initValues() {
        localOrdersDatabaseUtils = new LocalOrdersDatabaseUtils(context);

        List<LocalVideoFolder> localVideoFolderList = localOrdersDatabaseUtils.queryAllLocalVideoFolder();

        localOrderAddVideoFolderAdapter = new LocalOrderAddVideoFolderAdapter(context, localVideoFolderList);
        localOrderAddVideoFolderAdapter.setOnVideoFolderClickListener(new LocalOrderAddVideoFolderAdapter.OnVideoFolderClickListener() {
            @Override
            public void OnClick(LocalVideoFolder localVideoFolder) {
                if (onAddOrderCallback != null) {
                    LocalOrder localOrder = onAddOrderCallback.callBack(localVideoFolder);
                    boolean addState = localOrdersDatabaseUtils.addLocalOrder(localOrder);
                    if (addState) {
                        localOrderAddVideoFolderAdapter.refresh(localVideoFolder);
                    }
                    onAddOrderCallback.onFavoriteIcon(addState);
                }
            }
        });

        video_add_recyclerView.setAdapter(localOrderAddVideoFolderAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_add_dialog_imageView_close:
                dismiss();
                break;
            case R.id.video_add_dialog_imageView_add:
                NewVideoFolderDialog newVideoFolderDialog = new NewVideoFolderDialog(context);
                newVideoFolderDialog.setOnConfirmListener(new NewVideoFolderDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(LocalVideoFolder localVideoFolder) {
                        boolean addState = localOrdersDatabaseUtils.addLocalVideoFolder(localVideoFolder);
                        if (addState) {
                            localOrderAddVideoFolderAdapter.append(localVideoFolder);
                        }
                    }
                });
                newVideoFolderDialog.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        if (localOrdersDatabaseUtils != null) {
            localOrdersDatabaseUtils.close();
        }
        super.dismiss();
    }
}
