package com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBiliUser;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.Role;

/**
 * @Author Leon
 * @Time 2021/3/31
 * @Desc 用户搜索结果适配器
 */
public class SearchResultBiliUserAdapter extends BaseAdapter<SearchResultBiliUser> {
    private final MainActivity mainActivity;

    public SearchResultBiliUserAdapter(MainActivity mainActivity, Context context) {
        super(context);
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_bili_user_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SearchResultBiliUser searchResultBiliUser = getAllData().get(position);

        ImageView searchResultItemBiliUserVerifyMark = holder.findById(R.id.search_result_item_bili_user_verify_mark);

        if (searchResultBiliUser.role == Role.NONE) {
            searchResultItemBiliUserVerifyMark.setVisibility(View.GONE);
        } else {
            switch (searchResultBiliUser.role) {
                case PERSON:
                    searchResultItemBiliUserVerifyMark.setVisibility(View.VISIBLE);
                    searchResultItemBiliUserVerifyMark.setImageResource(R.drawable.ic_person_verify);
                    break;
                case OFFICIAL:
                    searchResultItemBiliUserVerifyMark.setVisibility(View.VISIBLE);
                    searchResultItemBiliUserVerifyMark.setImageResource(R.drawable.ic_official_verify);
                    break;
                default:
                    searchResultItemBiliUserVerifyMark.setVisibility(View.GONE);
                    break;
            }
        }

        holder
                .setImage(R.id.search_result_item_bili_user_face, searchResultBiliUser.userFace, ImagePixelSize.FACE)
                .setText(R.id.search_result_item_bili_user_name, searchResultBiliUser.userName)
                .setText(R.id.search_result_item_bili_user_sign, searchResultBiliUser.userSign == null ? context.getString(R.string.default_sign) : searchResultBiliUser.userSign)
                .setOnClickListener(R.id.search_result_item_bili_user_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPublicFragment(mainActivity, FragmentType.BILI_USER, searchResultBiliUser.mid);
                    }
                });
        TextView searchResultItemBiliUserOperation = holder.findById(R.id.search_result_item_bili_user_operation);
        String opText = searchResultBiliUser.userStatus == 0 ? "关注" : searchResultBiliUser.userStatus == 2 ? "已关注" : "已互粉";
        searchResultItemBiliUserOperation.setText(opText);
        searchResultItemBiliUserOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleSnackBar.make(v, context.getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG).show();
            }
        });
    }
}
