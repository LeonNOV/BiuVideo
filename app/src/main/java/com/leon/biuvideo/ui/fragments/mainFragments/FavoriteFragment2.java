package com.leon.biuvideo.ui.fragments.mainFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.FavoriteAdapter;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.fragments.BaseFragment;
import com.leon.biuvideo.ui.fragments.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.dataBaseUtils.Tables;

import java.util.List;

/**
 * MainActivity中的收藏片段
 */
public class FavoriteFragment2 extends BaseFragment {
    private RecyclerView favorite_recyclerView;
    private TextView favorite_textView_noDataStr;

    private List<Favorite> favorites;
    private FavoriteAdapter favoriteAdapter;
    private FavoriteDatabaseUtils favoriteDatabaseUtils;
    private boolean isVisit;
    
    @Override
    public int setLayout() {
        return R.layout.main_fragment_recycler_view;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.FavoriteUp);
        favoriteDatabaseUtils = (FavoriteDatabaseUtils) sqLiteHelperFactory.getInstance();

        favorite_recyclerView = findView(R.id.recyclerView);
        favorite_textView_noDataStr = findView(R.id.textView_noDataStr);
    }

    @Override
    public void initValues() {
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        isVisit = initValues.getBoolean("isVisit", true);

        if (isVisit) {
            favorites = favoriteDatabaseUtils.queryFavoritesByVisit();
        } else {
            favorites = favoriteDatabaseUtils.queryFavorites();
        }

        favoriteAdapter = new FavoriteAdapter(favorites, context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        favorite_recyclerView.setLayoutManager(layoutManager);
        favorite_recyclerView.setAdapter(favoriteAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        isVisit = initValues.getBoolean("isVisit", true);

        //处理Favorite数据
        if (isVisit) {
            favorites = favoriteDatabaseUtils.queryFavoritesByVisit();
        } else {
            favorites = favoriteDatabaseUtils.queryFavorites();
        }

        if (favorites.size() > 0) {
            //隐藏无数据提示，显示item数据
            favorite_textView_noDataStr.setVisibility(View.INVISIBLE);
            favorite_recyclerView.setVisibility(View.VISIBLE);

            favoriteAdapter.refresh(favorites);
        } else {
            favorite_textView_noDataStr.setVisibility(View.VISIBLE);
            favorite_recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoriteDatabaseUtils.close();
    }
}
