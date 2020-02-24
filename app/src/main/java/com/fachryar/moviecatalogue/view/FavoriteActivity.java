package com.fachryar.moviecatalogue.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fachryar.moviecatalogue.widget.FavoriteWidget;
import com.fachryar.moviecatalogue.adapter.FavoriteRecyclerAdapter;
import com.fachryar.moviecatalogue.viewmodel.FavoriteViewModel;
import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.room.Favorite;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.utils.AppConfig;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private FavoriteViewModel favoriteViewModel;
    private RecyclerView recyclerView;
    private FavoriteRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private TextView textEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        textEmpty = findViewById(R.id.textEmpty);
        recyclerView = findViewById(R.id.rv_fav);
        progressBar = findViewById(R.id.progressBar3);

        showProgressBar();
        setActionBar();
        initRecyclerView();
        loadRecyclerValue();

        adapter.setOnItemClickCallback(new FavoriteRecyclerAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Favorite favorite) {
                if (favorite.getType().equals(AppConfig.CATEGORY_MOVIE)){
                    Movies movie = new Movies();
                    movie = favoriteViewModel.inputMoviesFromFavorite(favorite, movie);

                    Intent i = new Intent(FavoriteActivity.this, DetailActivity.class);
                    i.putExtra(AppConfig.EXTRA_CATEGORY, AppConfig.CATEGORY_MOVIE);
                    i.putExtra(AppConfig.EXTRA_MOVIE, movie);
                    startActivity(i);
                }
                else {
                    TvShow tvShow = new TvShow();
                    tvShow = favoriteViewModel.inputTvShowFromFavorite(favorite, tvShow);

                    Intent i = new Intent(FavoriteActivity.this, DetailActivity.class);
                    i.putExtra(AppConfig.EXTRA_CATEGORY, AppConfig.CATEGORY_TV_SHOW);
                    i.putExtra(AppConfig.EXTRA_TV_SHOW, tvShow);
                    startActivity(i);
                }
                updateWidget();
            }
        });

    }

    private void initRecyclerView() {
        adapter = new FavoriteRecyclerAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));
    }

    private void loadRecyclerValue() {
        showProgressBar();
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        favoriteViewModel.getListFavorite().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                if (favorites.size() > 0){
                    textEmpty.setVisibility(View.GONE);
                    adapter.setFavorites(favorites);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textEmpty.setVisibility(View.VISIBLE);
                }
                hideProgressBar();
            }
        });
    }

    private void setActionBar(){
        AppCompatTextView textActionBar = new AppCompatTextView(getApplicationContext());
        textActionBar.setText(getResources().getString(R.string.favorite_list));
        textActionBar.setTextAppearance(this, R.style.white_16);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT;

        getSupportActionBar().setCustomView(textActionBar, layoutParams);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lil_black)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_16);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            updateWidget();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateWidget();
        finish();
    }

    public void updateWidget(){
        Intent intent = new Intent(this, FavoriteWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), FavoriteWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
    }
}
