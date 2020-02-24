package com.fachryar.moviecatalogue.viewmodel;

import android.app.Application;

import com.fachryar.moviecatalogue.repository.FavoriteRepository;
import com.fachryar.moviecatalogue.room.Favorite;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.utils.AppConfig;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FavoriteViewModel extends AndroidViewModel {
    private FavoriteRepository favoriteRepository;
    private LiveData<List<Favorite>> listFavorite;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteRepository = new FavoriteRepository(application);
        listFavorite = favoriteRepository.getListFavorite();
    }

    public Boolean checkIsFavorite(List<Favorite> favorites, int id){
        Boolean isFavorite = false;
        if (favorites.size() < 1){
            isFavorite = false;
        } else {
            int i = 0;
            while (i < favorites.size() && !isFavorite){
                if (id == favorites.get(i).getMovieId()){
                    isFavorite = true;
                } else {
                    isFavorite = false;
                }
                i++;
            }
        }
        return isFavorite;
    }

    public int getFavoriteIndex(List<Favorite> favorites, int id){
        int i = 0;
        while (i < favorites.size() && id != favorites.get(i).getMovieId()){
            i++;
        }
        return i;
    }

    public Boolean isExist(List<Favorite> favorites, int id){
        Boolean isDuplicate = false;
        if (favorites.size() < 1){
            isDuplicate = false;
        } else {
            do{
                for (int i=0; i < favorites.size(); i++){
                    if (id == favorites.get(i).getMovieId()){
                        isDuplicate = true;
                        break;
                    }
                }
            } while (isDuplicate = false);
        }

        return isDuplicate;
    }

    public Favorite inputFavoriteMovie(Favorite favorite, Movies movie){
        favorite.setMovieId(movie.getId());
        favorite.setTitle(movie.getTitle());
        favorite.setType(AppConfig.CATEGORY_MOVIE);
        favorite.setOverview(movie.getOverview());
        favorite.setPoster(movie.getPoster());
        favorite.setBackdrop(movie.getBackdrop());
        favorite.setRating(movie.getRatings());

        return favorite;
    }

    public Favorite inputFavoriteTvShow(Favorite favorite, TvShow tvShow){
        favorite.setMovieId(tvShow.getId());
        favorite.setTitle(tvShow.getShow_name());
        favorite.setType(AppConfig.CATEGORY_TV_SHOW);
        favorite.setOverview(tvShow.getOverview());
        favorite.setPoster(tvShow.getPoster());
        favorite.setBackdrop(tvShow.getBackdrop());
        favorite.setRating(tvShow.getRatings());

        return favorite;
    }

    public Movies inputMoviesFromFavorite(Favorite favorite, Movies movie){
        movie.setId(favorite.getMovieId());
        movie.setTitle(favorite.getTitle());
        movie.setOverview(favorite.getOverview());
        movie.setPoster(favorite.getPoster());
        movie.setBackdrop(favorite.getBackdrop());
        movie.setRatings(favorite.getRating());

        return movie;
    }

    public TvShow inputTvShowFromFavorite(Favorite favorite, TvShow tvShow){
        tvShow.setId(favorite.getMovieId());
        tvShow.setShow_name(favorite.getTitle());
        tvShow.setOverview(favorite.getOverview());
        tvShow.setPoster(favorite.getPoster());
        tvShow.setBackdrop(favorite.getBackdrop());
        tvShow.setRatings(favorite.getRating());

        return tvShow;
    }

    public void insert(Favorite favorite) {
        favoriteRepository.insert(favorite);
    }

    public void delete(Favorite favorite){
        favoriteRepository.delete(favorite);
    }

    public LiveData<List<Favorite>> getListFavorite() {
        return listFavorite;
    }
}
