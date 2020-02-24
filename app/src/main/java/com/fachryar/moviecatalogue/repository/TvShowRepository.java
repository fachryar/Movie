package com.fachryar.moviecatalogue.repository;

import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.model.TvShow;
import com.fachryar.moviecatalogue.model.TvResult;
import com.fachryar.moviecatalogue.utils.AppConfig;

import java.util.ArrayList;
import java.util.Locale;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowRepository {
    private static TvShowRepository instance;
    private ArrayList<TvShow> tvList = new ArrayList<>();
    private MutableLiveData<TvShow> mutableLiveDataDetail = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TvShow>> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<TvShow>> mutableLiveDataSearch = new MutableLiveData<>();

    public static TvShowRepository getInstance(){
        if (instance == null){
            instance = new TvShowRepository();
        } return instance;
    }

    public MutableLiveData<ArrayList<TvShow>> getDataResult(){
        String language = String.valueOf(Locale.getDefault());

        if (language.equals("in_ID")){
            language = "id";
        } else {
            language = "en-US";
        }

        final ApiService apiService = RetrofitClass.getApiService();
        Call<TvResult> call = apiService.getPopularTvShows(AppConfig.API_KEY, language);
        call.enqueue(new Callback<TvResult>() {
            @Override
            public void onResponse(Call<TvResult> call, Response<TvResult> response) {
                TvResult result = response.body();

                if (result != null && result.getTvResults() != null){
                    tvList = (ArrayList<TvShow>) result.getTvResults();
                    mutableLiveData.setValue(tvList);
                }
            }

            @Override
            public void onFailure(Call<TvResult> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<ArrayList<TvShow>> getSearchResult(String query){
        String language = String.valueOf(Locale.getDefault());

        if (language.equals("in_ID")){
            language = "id";
        } else {
            language = "en-US";
        }

        final ApiService apiService = RetrofitClass.getApiService();
        Call<TvResult> call = apiService.getSearchResultTvShows(AppConfig.API_KEY, language, query);
        call.enqueue(new Callback<TvResult>() {
            @Override
            public void onResponse(Call<TvResult> call, Response<TvResult> response) {
                TvResult result = response.body();

                if (result != null && result.getTvResults() != null){
                    tvList = (ArrayList<TvShow>) result.getTvResults();
                    mutableLiveDataSearch.setValue(tvList);
                }
            }

            @Override
            public void onFailure(Call<TvResult> call, Throwable t) {

            }
        });

        return mutableLiveDataSearch;
    }

    public MutableLiveData<TvShow> getDataDetailTvShow(int id){
        String language = String.valueOf(Locale.getDefault());

        if (language.equals("in_ID")){
            language = "id";
        } else {
            language = "en-US";
        }

        final ApiService apiService = RetrofitClass.getApiService();
        Call<TvShow> call = apiService.getTvShowById(String.valueOf(id), AppConfig.API_KEY, language);
        call.enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                TvShow tvShow = response.body();
                mutableLiveDataDetail.setValue(tvShow);
            }

            @Override
            public void onFailure(Call<TvShow> call, Throwable t) {

            }
        });
        return mutableLiveDataDetail;
    }
}
