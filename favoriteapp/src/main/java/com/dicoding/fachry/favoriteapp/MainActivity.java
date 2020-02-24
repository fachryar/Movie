package com.dicoding.fachry.favoriteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    List<Favorite> movieList;
    List<Favorite> tvList;
    private ProgressBar progressBar;
    private RecyclerView recyclerView1, recyclerView2;
    private FavoriteRecyclerAdapter adapter1, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView1 = findViewById(R.id.rvFavMovie);
        recyclerView2 = findViewById(R.id.rvFavTv);
        progressBar = findViewById(R.id.progressBar);

        adapter1 = new FavoriteRecyclerAdapter();
        adapter2 = new FavoriteRecyclerAdapter();
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        setActionBar();
        LoaderManager.getInstance(this).initLoader(AppConfig.FAVORITE_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        showProgressBar();
        if (id == AppConfig.FAVORITE_ID){
            return new CursorLoader(this, AppConfig.CONTENT_URI, null, null, null, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null){
            movieList = getFavoriteMovie(cursor);
            tvList = getFavoriteTv(cursor);

            adapter1.setListFavorites(movieList);
            recyclerView1.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();

            adapter2.setListFavorites(tvList);
            recyclerView2.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();

            hideProgressBar();
        } else {
            Toast.makeText(this, "Favorite not found!", Toast.LENGTH_SHORT).show();
            hideProgressBar();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        movieList = getFavoriteMovie(null);
        tvList = getFavoriteTv(null);
    }

    private List<Favorite> getFavoriteMovie(Cursor cursor){
        List<Favorite> favoriteListCursor = new ArrayList<>();
        int i = 0;
        String type;

        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            do {
                type = cursor.getString(cursor.getColumnIndex(AppConfig.COLUMN_TYPE));
                if (type.equalsIgnoreCase(AppConfig.MOVIE)){
                    Favorite favorite = new Favorite();
                    favorite.setTitle(cursor.getString(cursor.getColumnIndex(AppConfig.COLUMN_TITLE)));
                    favorite.setType(cursor.getString(cursor.getColumnIndex(AppConfig.COLUMN_TYPE)));
                    favorite.setPoster(cursor.getString(cursor.getColumnIndex(AppConfig.COLUMN_POSTER)));
                    favoriteListCursor.add(favorite);
                }
                cursor.moveToNext();
                i++;
            } while (!cursor.isAfterLast());
        }
        return favoriteListCursor;
    }

    private List<Favorite> getFavoriteTv(Cursor cursor){
        List<Favorite> favoriteListCursor = new ArrayList<>();
        int i = 0;
        String type;

        cursor.moveToFirst();

        if (cursor.getCount() > 0){
            do {
                type = cursor.getString(cursor.getColumnIndex(AppConfig.COLUMN_TYPE));
                if (type.equalsIgnoreCase(AppConfig.TV_SHOW)){
                    Favorite favorite = new Favorite();
                    favorite.setTitle(cursor.getString(cursor.getColumnIndex(AppConfig.COLUMN_TITLE)));
                    favorite.setType(cursor.getString(cursor.getColumnIndex(AppConfig.COLUMN_TYPE)));
                    favorite.setPoster(cursor.getString(cursor.getColumnIndex(AppConfig.COLUMN_POSTER)));
                    favoriteListCursor.add(favorite);
                }
                cursor.moveToNext();
                i++;
            } while (!cursor.isAfterLast());
        }
        return favoriteListCursor;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieList != null) {
            movieList.clear();
        }
        if (tvList != null) {
            tvList.clear();
        }
        LoaderManager.getInstance(this).restartLoader(AppConfig.FAVORITE_ID, null, this);
    }

    private void setActionBar(){
        AppCompatTextView textActionBar = new AppCompatTextView(getApplicationContext());
        textActionBar.setText(R.string.app_name);
        textActionBar.setTextAppearance(this, R.style.white_16);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        if (getSupportActionBar() != null){
            getSupportActionBar().setCustomView(textActionBar, layoutParams);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lil_black)));
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
