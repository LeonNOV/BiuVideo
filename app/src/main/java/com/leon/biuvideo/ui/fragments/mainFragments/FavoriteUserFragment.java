package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.FavoriteUserAdapter;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.util.List;

/**
 * MainActivity中的收藏片段
 */
public class FavoriteUserFragment extends BaseFragment {
    private RecyclerView favorite_recyclerView;
    private TextView favorite_textView_noDataStr;

    private FavoriteUserDatabaseUtils favoriteUserDatabaseUtils;
    private FavoriteUserAdapter favoriteUserAdapter;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_recycler_view;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        favorite_recyclerView = findView(R.id.recyclerView);
        favorite_textView_noDataStr = findView(R.id.textView_noDataStr);
    }

    @Override
    public void initValues() {
        if (favoriteUserDatabaseUtils == null) {
            SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.FavoriteUp);
            favoriteUserDatabaseUtils = (FavoriteUserDatabaseUtils) sqLiteHelperFactory.getInstance();
        }

        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        boolean isVisit = initValues.getBoolean("isVisit", true);

        List<Favorite> favorites = favoriteUserDatabaseUtils.queryFavorites(isVisit);

        if (favorites == null) {
            favorite_textView_noDataStr.setVisibility(View.VISIBLE);
            favorite_recyclerView.setVisibility(View.GONE);
        } else {
            //隐藏无数据提示，显示item数据
            favorite_textView_noDataStr.setVisibility(View.GONE);
            favorite_recyclerView.setVisibility(View.VISIBLE);

            favoriteUserAdapter = new FavoriteUserAdapter(favorites, context);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            favorite_recyclerView.setLayoutManager(layoutManager);
            favorite_recyclerView.setAdapter(favoriteUserAdapter);
        }
    }

    @Override
    public void onResume() {
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        boolean isVisit = initValues.getBoolean("isVisit", true);

        List<Favorite> favorites = favoriteUserDatabaseUtils.queryFavorites(isVisit);
        favoriteUserAdapter.refresh(favorites);

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (favoriteUserDatabaseUtils != null) {
            favoriteUserDatabaseUtils.close();
        }
        super.onDestroyView();
    }
}
