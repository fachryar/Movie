package com.fachryar.moviecatalogue.viewmodel;

import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.model.Genre;
import com.fachryar.moviecatalogue.repository.TvShowRepository;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TvShowViewModel extends ViewModel {
    private TvShowRepository tvShowRepository = TvShowRepository.getInstance();
    private MutableLiveData<ArrayList<TvShow>> mTV;

    public void init(){
        if (mTV != null){
            return;
        }
        mTV = tvShowRepository.getDataResult();
    }

    public String getGenre(TvShow tvShow){
        String genre = "";

        ArrayList<Genre> genreList = tvShow.getListGenre();

        for (int i = 0; i < genreList.size(); i++){
            if (genre.equals("")){
                genre = genreList.get(i).getGenre();
            } else {
                genre = genre + ", " + genreList.get(i).getGenre();
            }
        }
        return genre;
    }

    public LiveData<ArrayList<TvShow>> getTvShows(){
        return tvShowRepository.getDataResult();
    }

    public LiveData<TvShow> getDetail(int id) { return tvShowRepository.getDataDetailTvShow(id); }

    public LiveData<ArrayList<TvShow>> getTvSearchResult(String query) { return tvShowRepository.getSearchResult(query); }

}
