package com.fachryar.moviecatalogue.room;

import android.database.Cursor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface FavoriteDao {

    @Insert
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("SELECT * FROM favorite_table ORDER BY id")
    LiveData<List<Favorite>> getAllFavorite();

    @Query("SELECT * FROM favorite_table ORDER BY id")
    List<Favorite> getAllFavorite2();

    @Query("SELECT * FROM favorite_table ORDER BY id")
    Cursor selectAll();
}