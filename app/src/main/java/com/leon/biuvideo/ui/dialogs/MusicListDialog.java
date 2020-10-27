package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.AboutDialogAdapter;
import com.leon.biuvideo.beans.AboutBean;
import com.leon.biuvideo.beans.musicBeans.MusicPlayList;

import java.util.List;

public class MusicListDialog extends AlertDialog {
    private List<MusicPlayList> musicPlayLists;
    private Context context;

    public MusicListDialog(@NonNull Context context, List<MusicPlayList> musicPlayLists, Context context1) {
        super(context);
        this.musicPlayLists = musicPlayLists;
        this.context = context1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_dialog);

        initView();
        initEvent();
    }

    private void initView() {
        aboutDialog_recyclerView = findViewById(R.id.aboutDialog_recyclerView);
        aboutDialog_button = findViewById(R.id.aboutDialog_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        AboutDialogAdapter dialogAdapter = new AboutDialogAdapter(aboutBeans, context);

        aboutDialog_recyclerView.setLayoutManager(layoutManager);
        aboutDialog_recyclerView.setAdapter(dialogAdapter);
    }

    private void initEvent() {
        aboutDialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onCloseClick();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }

    public interface OnClickBottomListener {
        void onCloseClick();
    }

    public AboutDialog.OnClickBottomListener onClickBottomListener;

    public void setOnClickBottomListener(AboutDialog.OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
    }
}
