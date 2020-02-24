package com.fachryar.moviecatalogue.repository;

import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.model.Result;
import com.fachryar.moviecatalogue.model.TvResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie/popular")
    Call<Result> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/{id}")
    Call<Movies> getMovieById(@Path("id") String id, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("tv/popular")
    Call<TvResult> getPopularTvShows(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("tv/{id}")
    Call<TvShow> getTvShowById(@Path("id") String id, @Query("api_key") String apiKey, @Query("language") String language);

    @GET("search/movie")
    Call<Result> getSearchResultMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query);

    @GET("search/tv")
    Call<TvResult> getSearchResultTvShows(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query);

    @GET("discover/movie")
    Call<Result> getTodayMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("sort_by") String sort
            , @Query("primary_release_date.gte") String date1, @Query("primary_release_date.lte") String date2);

}
