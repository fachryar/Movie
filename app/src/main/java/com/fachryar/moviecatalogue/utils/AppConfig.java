package com.fachryar.moviecatalogue.utils;

import com.fachryar.moviecatalogue.BuildConfig;

public class AppConfig {
    public static final String API_KEY = BuildConfig.ApiKey;

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/w185";
    public static final String BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/w500";
    public static final String SORT_POPULARITY = "popularity.desc";

    public static final String FORMAT_YEAR_ONLY = "yyyy";
    public static final String FORMAT_DATE_ENGLISH = "MMMM dd, yyyy";
    public static final String FORMAT_DATE_INDONESIA = "dd MMMM, yyyy";
    public static final String FORMAT_DATE_MOVIEDB = "yyyy-MM-dd";

    public static final String AUTHORITY = "com.fachryar.moviecatalogue";
    public static final int CODE_FAV = 505;
    public static final String DB_NAME = "movie_database";
    public static final String TABLE_NAME = "favorite_table";

    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_TV_SHOW = "extra_tv_show";
    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_SEARCH = "extra_search";
    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_NOTIF = "extra_notif";

    public static final String CATEGORY_MOVIE = "Movie";
    public static final String CATEGORY_TV_SHOW = "TV Show";

    public static final String WIDGET_ACTION = "com.fachryar.moviecatalogue.WIDGET_ACTION";
    public static final String EXTRA_ITEM = "com.fachryar.moviecatalogue.EXTRA_ITEM";

    public static final String TYPE_REMINDER = "DailyReminder";
    public static final String TYPE_RELEASE = "ReleaseToday";
    public static final int ID_REMINDER = 111;
    public static String CHANNEL_REMINDER_ID = "channel_1";
    public static CharSequence CHANNEL_REMINDER_NAME = "reminder channel";
    public static final int ID_RELEASE = 222;
    public static String CHANNEL_RELEASE_ID = "channel_2";
    public static CharSequence CHANNEL_RELEASE_NAME = "release channel";
    public static final String GROUP_RELEASE = "group_release";

}
