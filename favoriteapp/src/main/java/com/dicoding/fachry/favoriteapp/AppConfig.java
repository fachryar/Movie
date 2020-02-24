package com.dicoding.fachry.favoriteapp;

import android.net.Uri;

public class AppConfig {
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/w185";

    public static final String DB_NAME = "movie_database";
    public static final String SCHEME = "content";
    public static final String AUTHORITY = "com.fachryar.moviecatalogue";
    public static final String FAVORITE_TABLE = "favorite_table";
    public static final String MOVIE = "Movie";
    public static final String TV_SHOW = "TV Show";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_POSTER = "poster";
    public static final int FAVORITE_ID = 505;

    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(AppConfig.SCHEME)
            .authority(AppConfig.AUTHORITY)
            .appendPath(AppConfig.FAVORITE_TABLE)
            .build();

}
