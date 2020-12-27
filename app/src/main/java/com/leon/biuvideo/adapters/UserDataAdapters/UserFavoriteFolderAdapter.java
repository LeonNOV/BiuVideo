package com.leon.biuvideo.adapters.UserDataAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.userBeans.UserFolder;
import com.leon.biuvideo.utils.Fuck;

import java.util.List;

public class UserFavoriteFolderAdapter extends BaseAdapter<UserFolder> {
    private final List<UserFolder> userFolders;

    public UserFavoriteFolderAdapter(List<UserFolder> userFolders, Context context) {
        super(userFolders, context);
        this.userFolders = userFolders;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.item_favorite_video_folder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        UserFolder userFolder = userFolders.get(position);

        holder
                .setText(R.id.item_favorite_video_textView_folderName, userFolder.title)
                .setText(R.id.item_favorite_video_textView_folderTotal, String.valueOf(userFolder.total))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fuck.blue("click " + position + "------" + userFolder.id + "-----" + userFolder.title);
                        if (onClickFolderListener != null) {
                            onClickFolderListener.OnClick(userFolder.id);
                        }
                    }
                });
    }

    private OnClickFolderListener onClickFolderListener;

    public interface OnClickFolderListener {
        void OnClick(long folderId);
    }

    public void setOnClickFolderListener(OnClickFolderListener onClickFolderListener) {
        this.onClickFolderListener = onClickFolderListener;
    }
}
