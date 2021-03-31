package com.leon.biuvideo.adapters.discover.searchResultAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBiliUser;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.Role;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/31
 * @Desc 用户搜索结果适配器
 */
public class SearchResultBiliUserAdapter extends BaseAdapter<SearchResultBiliUser> {
    private final List<SearchResultBiliUser> searchResultBiliUserList;

    public SearchResultBiliUserAdapter(List<SearchResultBiliUser> beans, Context context) {
        super(beans, context);

        this.searchResultBiliUserList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_item_bili_user;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SearchResultBiliUser searchResultBiliUser = searchResultBiliUserList.get(position);

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

                    }
                });
        TextView searchResultItemBiliUserOperation = holder.findById(R.id.search_result_item_bili_user_operation);
        String opText = searchResultBiliUser.userStatus == 0 ? "关注" : searchResultBiliUser.userStatus == 2 ? "已关注" : "已互粉";
        searchResultItemBiliUserOperation.setText(opText);
        searchResultItemBiliUserOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
