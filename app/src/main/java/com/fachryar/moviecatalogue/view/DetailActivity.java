package com.fachryar.moviecatalogue.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fachryar.moviecatalogue.widget.FavoriteWidget;
import com.fachryar.moviecatalogue.room.Favorite;
import com.fachryar.moviecatalogue.viewmodel.FavoriteViewModel;
import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.utils.AppConfig;
import com.fachryar.moviecatalogue.utils.DateFormat;
import com.fachryar.moviecatalogue.viewmodel.MoviesViewModel;
import com.fachryar.moviecatalogue.viewmodel.TvShowViewModel;

import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    ImageView imgDetailMovies, imgBackdrop;
    ImageButton btnFavorite;
    TextView tvTitle, tvDetail1, tvDetail2, tvRating,
            tvSubTitle, tvDetail3, tvOverview;
    TextView textDetail1, textDetail2, textDetail3;
    ProgressBar progressBar;

    Boolean isFavorite = false;
    String category;

    TvShowViewModel tvShowViewModel;
    MoviesViewModel moviesViewModel;
    FavoriteViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        transparentStatusBar();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        imgDetailMovies = findViewById(R.id.imgDetailPoster);
        imgBackdrop = findViewById(R.id.imgBgPoster);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvSubTitle = findViewById(R.id.tvDetailSubTitle);
        tvRating = findViewById(R.id.tvDetailRating);
        tvDetail1 = findViewById(R.id.tvDetail1);
        tvDetail2 = findViewById(R.id.tvDetail2);
        tvDetail3 = findViewById(R.id.tvDetail3);
        tvOverview = findViewById(R.id.tvDetailDescription);

        textDetail1 = findViewById(R.id.textDetail1);
        textDetail2 = findViewById(R.id.textDetail2);
        textDetail3 = findViewById(R.id.textDetail3);

        progressBar = findViewById(R.id.progressBar2);

        showProgressBar();

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        // Check Category & Observe Data
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            if (extras.getString(AppConfig.EXTRA_CATEGORY).equals(AppConfig.CATEGORY_MOVIE)){
                final Movies movie = getIntent().getParcelableExtra(AppConfig.EXTRA_MOVIE);
                category = getResources().getString(R.string.movie);

                if (movie != null){
                    favoriteViewModel.getListFavorite().observe(this, new Observer<List<Favorite>>() {
                        @Override
                        public void onChanged(List<Favorite> favorites) {
                            isFavorite = favoriteViewModel.checkIsFavorite(favorites, movie.getId());
                            btnFavorite.setBackground(isFavorite
                                    ? getResources().getDrawable(R.drawable.ic_fav_on) : getResources().getDrawable(R.drawable.ic_fav_off));
                        }
                    });
                }
                moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
                loadDetailMovie();

            } else if (extras.getString(AppConfig.EXTRA_CATEGORY).equals(AppConfig.CATEGORY_TV_SHOW)){
                final TvShow tvShow = getIntent().getParcelableExtra(AppConfig.EXTRA_TV_SHOW);
                category = getResources().getString(R.string.tv_show);

                if (tvShow != null){
                    favoriteViewModel.getListFavorite().observe(this, new Observer<List<Favorite>>() {
                        @Override
                        public void onChanged(List<Favorite> favorites) {
                            isFavorite = favoriteViewModel.checkIsFavorite(favorites, tvShow.getId());
                            btnFavorite.setBackground(isFavorite
                                    ? getResources().getDrawable(R.drawable.ic_fav_on) : getResources().getDrawable(R.drawable.ic_fav_off));
                        }
                    });
                }
                tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
                loadDetailTv();
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideProgressBar();
                finish();
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteViewModel.getListFavorite().observe(DetailActivity.this, new Observer<List<Favorite>>() {
                    @Override
                    public void onChanged(List<Favorite> favorites) {
                        favoriteViewModel.getListFavorite().removeObservers(DetailActivity.this);
                    }
                });
                showAlertDialog(isFavorite, category);
            }
        });
    }

    private void loadDetailMovie(){
        showProgressBar();
        final Movies movie = getIntent().getParcelableExtra(AppConfig.EXTRA_MOVIE);

        if (movie != null){
            loadImage(movie.getPoster(), movie.getBackdrop());
            loadRvValue(movie.getTitle(), movie.getOverview(), movie.getRatings());

            moviesViewModel.getDetail(movie.getId()).observe(this, new Observer<Movies>() {
                @Override
                public void onChanged(Movies moviesDetail) {
                    tvSubTitle.setText(moviesViewModel.getGenre(moviesDetail));
                    textDetail1.setText(getResources().getString(R.string.release_date));
                    textDetail2.setText(getResources().getString(R.string.runtime));
                    textDetail3.setText(getResources().getString(R.string.status));
                    tvDetail1.setText(DateFormat.getDate(moviesDetail.getRelease_date(), Locale.getDefault().toString()));
                    tvDetail2.setText(getResources().getString(R.string.minutes, moviesDetail.getRuntime()));
                    tvDetail3.setText(moviesDetail.getStatus());
                    tvOverview.setText(moviesDetail.getOverview().isEmpty() ? getResources().getString(R.string.overview_unavailable) : moviesDetail.getOverview());

                    hideProgressBar();
                }
            });
        }
    }

    private void loadDetailTv(){
        showProgressBar();
        final TvShow tvShow = getIntent().getParcelableExtra(AppConfig.EXTRA_TV_SHOW);

        if (tvShow != null){
            loadImage(tvShow.getPoster(), tvShow.getBackdrop());
            loadRvValue(tvShow.getShow_name(), tvShow.getOverview(), tvShow.getRatings());

            tvShowViewModel.getDetail(tvShow.getId()).observe(this, new Observer<TvShow>() {
                @Override
                public void onChanged(TvShow tvShowDetail) {
                    tvSubTitle.setText(tvShowViewModel.getGenre(tvShowDetail));
                    textDetail1.setText(getResources().getString(R.string.first_air_date));
                    textDetail2.setText(getResources().getString(R.string.episodes));
                    textDetail3.setText(getResources().getString(R.string.status));
                    if (tvShowDetail.getFirst_release() != null){
                        tvDetail1.setText(DateFormat.getDate(tvShowDetail.getFirst_release(), Locale.getDefault().toString()));
                    }
                    tvDetail2.setText(getResources().getString(R.string.episode_sesason,
                            tvShowDetail.getEpisode(), tvShowDetail.getSeason()));
                    tvDetail3.setText(tvShowDetail.getStatus());
                    tvOverview.setText(tvShowDetail.getOverview().isEmpty() ? getResources().getString(R.string.overview_unavailable) : tvShowDetail.getOverview());

                    hideProgressBar();
                }
            });
        }
    }


    private void loadRvValue(String title, String overview, Double rating){
        if (rating >= 7.0){
            tvRating.setBackgroundResource(R.drawable.ic_green);
        } else if (rating <= 4.0){
            tvRating.setBackgroundResource(R.drawable.ic_red);
        } else {
            tvRating.setBackgroundResource(R.drawable.ic_yellow);
        }

        tvOverview.setText(overview.isEmpty() ? getResources().getString(R.string.overview_unavailable) : overview);

        tvTitle.setText(title);
        tvRating.setText(String.format(Locale.getDefault(),"%.0f%%", rating*10));
    }

    private void loadImage(String poster, String backdrop){
        Glide.with(this).load(AppConfig.BASE_POSTER_URL + poster)
                .into(imgDetailMovies);
        Glide.with(this).load(AppConfig.BASE_BACKDROP_URL + backdrop)
                .into(imgBackdrop);
    }

    private void showAlertDialog(final Boolean isFav, String cat){
        String dialogTitle;

        final Boolean isMovie = (cat.equals(getResources().getString(R.string.movie)));

        dialogTitle = (isFav ? getResources().getString(R.string.remove_favorite) : getResources().getString(R.string.add_favorite));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialogTitle);
        builder.setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Favorite favorite = new Favorite();

                        // If Already Favorite
                        if (isFav){

                            // Movie
                            if (isMovie){
                                final Movies movie = getIntent().getParcelableExtra(AppConfig.EXTRA_MOVIE);

                                favoriteViewModel.getListFavorite().observe(DetailActivity.this, new Observer<List<Favorite>>() {
                                    @Override
                                    public void onChanged(List<Favorite> listFavorites) {
                                        int index = favoriteViewModel.getFavoriteIndex(listFavorites, movie.getId());

                                        if (index < listFavorites.size()){
                                            favoriteViewModel.delete(listFavorites.get(index));
                                            Toast.makeText(DetailActivity.this, getResources().getString(R.string.successfully_remove_fav, movie.getTitle()), Toast.LENGTH_SHORT).show();
                                        }

                                        favoriteViewModel.getListFavorite().removeObservers(DetailActivity.this);
                                    }
                                });
                            }

                            // Tv Show
                            else {
                                final TvShow tvShow = getIntent().getParcelableExtra(AppConfig.EXTRA_TV_SHOW);

                                favoriteViewModel.getListFavorite().observe(DetailActivity.this, new Observer<List<Favorite>>() {
                                    @Override
                                    public void onChanged(List<Favorite> listFavorites) {
                                        int index = favoriteViewModel.getFavoriteIndex(listFavorites, tvShow.getId());

                                        if (index < listFavorites.size()){
                                            favoriteViewModel.delete(listFavorites.get(index));
                                            Toast.makeText(DetailActivity.this, getResources().getString(R.string.successfully_remove_fav, tvShow.getShow_name()), Toast.LENGTH_SHORT).show();
                                        }

                                        favoriteViewModel.getListFavorite().removeObservers(DetailActivity.this);
                                    }
                                });
                            }
                            btnFavorite.setBackground(getResources().getDrawable(R.drawable.ic_fav_off));
                            isFavorite = false;
                        }

                        // If Not Favorite
                        else {

                            //Movie
                            if (isMovie){
                                final Movies movie = getIntent().getParcelableExtra(AppConfig.EXTRA_MOVIE);

                                favoriteViewModel.getListFavorite().observe(DetailActivity.this, new Observer<List<Favorite>>() {
                                    @Override
                                    public void onChanged(List<Favorite> listFavorites) {
                                        Favorite favoriteIn = new Favorite();

                                        if (!favoriteViewModel.isExist(listFavorites, movie.getId())){
                                            favoriteIn = favoriteViewModel.inputFavoriteMovie(favoriteIn, movie);
                                            favoriteViewModel.insert(favoriteIn);
                                            Toast.makeText(DetailActivity.this, getResources().getString(R.string.successfully_add_fav, movie.getTitle()), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(DetailActivity.this, getResources().getString(R.string.duplicate_movie_found, movie.getTitle()), Toast.LENGTH_SHORT).show();
                                        }

                                        favoriteViewModel.getListFavorite().removeObservers(DetailActivity.this);
                                    }
                                });
                            }

                            // TvShow
                            else {
                                final TvShow tvShow = getIntent().getParcelableExtra(AppConfig.EXTRA_TV_SHOW);

                                favoriteViewModel.getListFavorite().observe(DetailActivity.this, new Observer<List<Favorite>>() {
                                    @Override
                                    public void onChanged(List<Favorite> listFavorites) {
                                        Favorite favoriteIn = new Favorite();

                                        if (!favoriteViewModel.isExist(listFavorites, tvShow.getId())){
                                            favoriteIn = favoriteViewModel.inputFavoriteTvShow(favoriteIn, tvShow);
                                            favoriteViewModel.insert(favoriteIn);
                                            Toast.makeText(DetailActivity.this, getResources().getString(R.string.successfully_add_fav, tvShow.getShow_name()), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(DetailActivity.this, getResources().getString(R.string.duplicate_movie_found, tvShow.getShow_name()), Toast.LENGTH_SHORT).show();
                                        }

                                        favoriteViewModel.getListFavorite().removeObservers(DetailActivity.this);
                                    }
                                });
                            }
                            btnFavorite.setBackground(getResources().getDrawable(R.drawable.ic_fav_on));
                            isFavorite = true;
                        }
                        updateWidget();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateWidget(){
        Intent intent = new Intent(this, FavoriteWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), FavoriteWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        Context context = getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void transparentStatusBar(){
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

}
