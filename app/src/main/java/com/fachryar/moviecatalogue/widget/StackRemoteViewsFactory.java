package com.fachryar.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.room.Favorite;
import com.fachryar.moviecatalogue.room.FavoriteDao;
import com.fachryar.moviecatalogue.room.FavoriteDatabase;
import com.fachryar.moviecatalogue.utils.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Bitmap> widgetItem = new ArrayList<>();
    private FavoriteDao favoriteDao;
    private Context mContext;

    StackRemoteViewsFactory(Context context){
        mContext = context;
        FavoriteDatabase database = FavoriteDatabase.getInstance(mContext);
        favoriteDao = database.favoriteDao();
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        widgetItem.clear();

        final long identityToken = Binder.clearCallingIdentity();

        List<Favorite> favoriteList = favoriteDao.getAllFavorite2();
        if (favoriteList.size() > 0){
            for (int i = 0; i < favoriteList.size(); i++){
                try {
                    widgetItem.add(BitmapFactory.decodeStream((InputStream) new URL (AppConfig.BASE_POSTER_URL
                            + favoriteList.get(i).getPoster()).getContent()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() { return widgetItem.size(); }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        if (widgetItem.size() != 0){
            rv.setImageViewBitmap(R.id.widgetImage, widgetItem.get(position));

            Bundle extras = new Bundle();
            extras.putInt(AppConfig.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);

            rv.setOnClickFillInIntent(R.id.widgetImage, fillInIntent);
            return rv;
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
