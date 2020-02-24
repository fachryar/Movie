package com.fachryar.moviecatalogue.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.fachryar.moviecatalogue.room.Favorite;
import com.fachryar.moviecatalogue.room.FavoriteDao;
import com.fachryar.moviecatalogue.room.FavoriteDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FavoriteRepository {
    private FavoriteDao favoriteDao;
    private LiveData<List<Favorite>> listFavorite;

    public FavoriteRepository(Application application) {
        FavoriteDatabase database = FavoriteDatabase.getInstance(application);
        favoriteDao = database.favoriteDao();
        listFavorite = favoriteDao.getAllFavorite();
    }

    public void insert(Favorite favorite){
        new InsertFavoriteAsyncTask(favoriteDao).execute(favorite);
    }

    public void delete(Favorite favorite){
        new DeleteFavoriteAsyncTask(favoriteDao).execute(favorite);
    }

    public LiveData<List<Favorite>> getListFavorite() {
        return listFavorite;
    }

    private static class InsertFavoriteAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao favoriteDao;

        private InsertFavoriteAsyncTask(FavoriteDao favoriteDao){
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.insert(favorites[0]);
            return null;
        }
    }

    private static class DeleteFavoriteAsyncTask extends AsyncTask<Favorite, Void, Void> {
        private FavoriteDao favoriteDao;

        private DeleteFavoriteAsyncTask(FavoriteDao favoriteDao){
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {
            favoriteDao.delete(favorites[0]);
            return null;
        }
    }
}
