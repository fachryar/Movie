package com.fachryar.moviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.view.Menu;

import com.fachryar.moviecatalogue.room.Favorite;
import com.fachryar.moviecatalogue.room.FavoriteDao;
import com.fachryar.moviecatalogue.room.FavoriteDatabase;
import com.fachryar.moviecatalogue.utils.AppConfig;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FavoriteProvider extends ContentProvider {
    public static final Uri URI_FAVORITE = Uri.parse(
            "content://" + AppConfig.AUTHORITY + "/" + AppConfig.TABLE_NAME);

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AppConfig.AUTHORITY, AppConfig.TABLE_NAME, AppConfig.CODE_FAV);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final int code = MATCHER.match(uri);
        if (code == AppConfig.CODE_FAV){
            final Context context = getContext();
            if (context == null){
                return null;
            }

            FavoriteDao favoriteDao = FavoriteDatabase.getInstance(context).favoriteDao();
            final Cursor cursor;
            cursor = favoriteDao.selectAll();
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (MATCHER.match(uri) == AppConfig.CODE_FAV) {
            return "vnd.android.cursor.dir/" + AppConfig.AUTHORITY + "." + AppConfig.TABLE_NAME;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
