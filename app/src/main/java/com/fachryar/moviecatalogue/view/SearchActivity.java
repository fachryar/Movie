package com.fachryar.moviecatalogue.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.tv.TvContract;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.adapter.MovieRecyclerAdapter;
import com.fachryar.moviecatalogue.adapter.TvShowRecyclerAdapter;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.room.Favorite;
import com.fachryar.moviecatalogue.utils.AppConfig;
import com.fachryar.moviecatalogue.viewmodel.MoviesViewModel;
import com.fachryar.moviecatalogue.viewmodel.TvShowViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvResult;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.rvSearch);
        tvResult = findViewById(R.id.tvSearch);
        progressBar = findViewById(R.id.pbSearch);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showProgressBar();
        setActionBar();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (extras.getString(AppConfig.EXTRA_CATEGORY).equals(AppConfig.CATEGORY_MOVIE)){
                loadMovieResult(extras.getString(AppConfig.EXTRA_SEARCH));
            } else if (extras.getString(AppConfig.EXTRA_CATEGORY).equals(AppConfig.CATEGORY_TV_SHOW)){
                loadTvResult(extras.getString(AppConfig.EXTRA_SEARCH));
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null){
            final MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
            searchMenuItem.expandActionView();
            searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return false;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    finish();
                    recyclerView.removeAllViewsInLayout();
                    return true;
                }
            });

            final SearchView searchView = (SearchView) (menu.findItem(R.id.menu_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.clearFocus();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                searchEditText.setTextAppearance(R.style.white_16);
            }

            Bundle extras = getIntent().getExtras();
            searchView.setQuery(extras.getString(AppConfig.EXTRA_SEARCH), false);

            if (extras.getString(AppConfig.EXTRA_CATEGORY).equals(AppConfig.CATEGORY_MOVIE)){
                searchView.setQueryHint(getResources().getString(R.string.search_movie));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        loadMovieResult(query);
                        searchView.clearFocus();
                        showProgressBar();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            } else if (extras.getString(AppConfig.EXTRA_CATEGORY).equals(AppConfig.CATEGORY_TV_SHOW)){
                searchView.setQueryHint(getResources().getString(R.string.search_tv));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        loadTvResult(query);
                        searchView.clearFocus();
                        showProgressBar();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }
        }

        return true;
    }


    private void loadMovieResult(String query){
        showProgressBar();
        final MovieRecyclerAdapter adapter = new MovieRecyclerAdapter();
        MoviesViewModel moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesViewModel.getMoviesSearchResult(query).observe(this, new Observer<ArrayList<Movies>>() {
            @Override
            public void onChanged(ArrayList<Movies> movies) {
                if (movies.size() != 0){
                    adapter.setListMovies(movies);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickCallback(new MovieRecyclerAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(Movies movie) {
                            Intent i = new Intent(SearchActivity.this, DetailActivity.class);
                            i.putExtra(AppConfig.EXTRA_CATEGORY, AppConfig.CATEGORY_MOVIE);
                            i.putExtra(AppConfig.EXTRA_MOVIE, movie);
                            startActivity(i);
                        }
                    });
                    tvResult.setText(getResources().getString(R.string.result, getItemCount(movies.size())));
                }
                else {
                    recyclerView.removeAllViewsInLayout();
                    recyclerView.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    tvResult.setText(getResources().getString(R.string.no_result));
                }
                hideProgressBar();
            }
        });
    }

    private void loadTvResult(String query){
        showProgressBar();
        final TvShowRecyclerAdapter adapter = new TvShowRecyclerAdapter();
        TvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getTvSearchResult(query).observe(this, new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                if (tvShows.size() != 0){
                    adapter.setListTvShow(tvShows);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickCallback(new TvShowRecyclerAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(TvShow tvShow) {
                            Intent i = new Intent(SearchActivity.this, DetailActivity.class);
                            i.putExtra(AppConfig.EXTRA_CATEGORY, AppConfig.CATEGORY_TV_SHOW);
                            i.putExtra(AppConfig.EXTRA_TV_SHOW, tvShow);
                            startActivity(i);
                        }
                    });

                    tvResult.setText(getResources().getString(R.string.result, getItemCount(tvShows.size())));
                }
                else {
                    recyclerView.removeAllViewsInLayout();
                    recyclerView.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    tvResult.setText(getResources().getString(R.string.no_result));
                }
                hideProgressBar();
            }
        });
    }


    private int getItemCount(int size){
        final int limit = 15;
        if (size > limit){
            return limit;
        } else {
            return size;
        }
    }

    private void setActionBar(){
        AppCompatTextView textActionBar = new AppCompatTextView(getApplicationContext());
        textActionBar.setText("");
        textActionBar.setTextAppearance(this, R.style.white_16);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        getSupportActionBar().setCustomView(textActionBar, layoutParams);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lil_black)));
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}
