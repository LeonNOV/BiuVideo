package com.leon.biuvideo.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.FavoriteAdapter;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.dataBaseUtils.Tables;

import java.util.List;

/**
 * MainActivity中的收藏片段
 */
public class FavoriteFragment extends Fragment {
    private RecyclerView favorite_recyclerView;
    private TextView favorite_textView_noDataStr;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Context context;

    private View view;

    private List<Favorite> favorites;
    private FavoriteAdapter favoriteAdapter;
    private FavoriteDatabaseUtils favoriteDatabaseUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment_recycler_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initValue();
    }

    private void initView() {
        context = getActivity();
        view = getView();

        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.FavoriteUp);
        favoriteDatabaseUtils = (FavoriteDatabaseUtils) sqLiteHelperFactory.getInstance();

        favorite_recyclerView = view.findViewById(R.id.recyclerView);
        favorite_textView_noDataStr = view.findViewById(R.id.textView_noDataStr);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);
    }

    private void initValue() {
        favorites = favoriteDatabaseUtils.queryFavorites();
        favoriteAdapter = new FavoriteAdapter(favorites, context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        favorite_recyclerView.setLayoutManager(layoutManager);
        favorite_recyclerView.setAdapter(favoriteAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        //处理Favorite数据
        favorites = favoriteDatabaseUtils.queryFavorites();

        Fuck.blue("FavoriteFragment:onResume");

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
