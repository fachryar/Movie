package com.fachryar.moviecatalogue.repository;

import com.fachryar.moviecatalogue.utils.AppConfig;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.model.Result;

import java.util.ArrayList;
import java.util.Locale;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository {
    private static MoviesRepository instance;
    private ArrayList<Movies> movieList = new ArrayList<>();
    private MutableLiveData<Movies> mutableLiveDataDetail = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movies>> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movies>> mutableLiveDataSearch = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Movies>> mutableLiveDataReleaseToday = new MutableLiveData<>();


    public static MoviesRepository getInstance(){
        if (instance == null){
            instance = new MoviesRepository();
        } return instance;
    }


    public MutableLiveData<ArrayList<Movies>> getDataResult(){
        String language = String.valueOf(Locale.getDefault());

        if (language.equals("in_ID")){
            language = "id";
        } else {
            language = "en-US";
        }

        final ApiService apiService = RetrofitClass.getApiService();
        Call<Result> call = apiService.getPopularMovies(AppConfig.API_KEY, language);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();

                if (result != null && result.getMovieResults() != null){
                    movieList = (ArrayList<Movies>) result.getMovieResults();
                    mutableLiveData.setValue(movieList);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }


    public MutableLiveData<ArrayList<Movies>> getSearchResult(String query){
        String language = String.valueOf(Locale.getDefault());

        if (language.equals("in_ID")){
            language = "id";
        } else {
            language = "en-US";
        }

        final ApiService apiService = RetrofitClass.getApiService();
        Call<Result> call = apiService.getSearchResultMovies(AppConfig.API_KEY, language, query);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();

                if (result != null && result.getMovieResults() != null){
                    movieList = (ArrayList<Movies>) result.getMovieResults();
                    mutableLiveDataSearch.setValue(movieList);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });

        return mutableLiveDataSearch;
    }


    public MutableLiveData<Movies> getDataDetailMovie(int id){
        String language = String.valueOf(Locale.getDefault());

        if (language.equals("in_ID")){
            language = "id";
        } else {
            language = "en-US";
        }

        final ApiService apiService = RetrofitClass.getApiService();
        Call<Movies> call = apiService.getMovieById(String.valueOf(id), AppConfig.API_KEY, language);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Movies movie = response.body();
                mutableLiveDataDetail.setValue(movie);
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }
        });
        return mutableLiveDataDetail;
    }

    public MutableLiveData<ArrayList<Movies>> getTodayMovies(String releaseDate){
        String language = String.valueOf(Locale.getDefault());

        if (language.equals("in_ID")){
            language = "id";
        } else {
            language = "en-US";
        }

        final ApiService apiService = RetrofitClass.getApiService();
        Call<Result> call = apiService.getTodayMovies(AppConfig.API_KEY, language, AppConfig.SORT_POPULARITY, releaseDate, releaseDate);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();

                if (result != null && result.getMovieResults() != null){
                    movieList = (ArrayList<Movies>) result.getMovieResults();
                    mutableLiveDataReleaseToday.setValue(movieList);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });

        return mutableLiveDataReleaseToday;
    }
}
