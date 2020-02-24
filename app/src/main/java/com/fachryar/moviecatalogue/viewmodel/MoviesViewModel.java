package com.fachryar.moviecatalogue.viewmodel;

import com.fachryar.moviecatalogue.model.Genre;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.repository.MoviesRepository;

import java.util.ArrayList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MoviesViewModel extends ViewModel {
    private MoviesRepository moviesRepository = MoviesRepository.getInstance();
    private MutableLiveData<ArrayList<Movies>> mMovie;

    public void init(){
        if (mMovie != null){
            return;
        }
        mMovie = moviesRepository.getDataResult();
    }

    public String getGenre(Movies movie){
        String genre = "";

        ArrayList<Genre> genreList = movie.getListGenre();

        for (int i = 0; i < genreList.size(); i++){
            if (genre.equals("")){
                genre = genreList.get(i).getGenre();
            } else {
                genre = genre + ", " + genreList.get(i).getGenre();
            }
        }
        return genre;
    }

    public LiveData<ArrayList<Movies>> getMovies(){
       return moviesRepository.getDataResult();
    }

    public LiveData<Movies> getDetail(int id) { return moviesRepository.getDataDetailMovie(id); }

    public LiveData<ArrayList<Movies>> getMoviesSearchResult(String query) { return moviesRepository.getSearchResult(query); }

    public LiveData<ArrayList<Movies>> getTodayMovies(String releaseDate) { return  moviesRepository.getTodayMovies(releaseDate);}

}
