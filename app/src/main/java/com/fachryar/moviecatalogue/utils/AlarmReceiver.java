package com.fachryar.moviecatalogue.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.model.Result;
import com.fachryar.moviecatalogue.repository.ApiService;
import com.fachryar.moviecatalogue.repository.RetrofitClass;
import com.fachryar.moviecatalogue.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmReceiver extends BroadcastReceiver{
    private ArrayList<Movies> movieList = new ArrayList<>();

    public AlarmReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        String currentDate = new SimpleDateFormat(AppConfig.FORMAT_DATE_MOVIEDB, Locale.getDefault()).format(new Date());
        String type = intent.getStringExtra(AppConfig.EXTRA_TYPE);

        if (type != null){
            if (type.equalsIgnoreCase(AppConfig.TYPE_REMINDER)){
                showNotificationReminder(context);
            } else {
                getTodayMovies(currentDate, context);
            }
        }
    }

    private void showNotificationReminder(Context context){
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, AppConfig.CHANNEL_REMINDER_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(context.getResources().getString(R.string.missing_you))
                .setSound(sound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(AppConfig.CHANNEL_REMINDER_ID, AppConfig.CHANNEL_REMINDER_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            nBuilder.setChannelId(AppConfig.CHANNEL_REMINDER_ID);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = nBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (notificationManager != null){
            notificationManager.notify(AppConfig.ID_REMINDER, notification);
        }
    }

    private void showNotificationRelease (Context context, ArrayList<Movies> movies){
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nBuilder;

        if (movieList.size() == 1){
            nBuilder = new NotificationCompat.Builder(context, AppConfig.CHANNEL_RELEASE_ID)
                    .setContentTitle(context.getResources().getString(R.string.release_today_head))
                    .setContentText(movieList.get(0).getTitle())
                    .setSmallIcon(R.drawable.ic_movie)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else if (movieList.size() == 2) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine(movieList.get(0).getTitle())
                    .addLine(movieList.get(1).getTitle())
                    .setBigContentTitle(context.getResources().getString(R.string.release_today_head))
                    .setSummaryText(context.getResources().getString(R.string.release_today));
            nBuilder = new NotificationCompat.Builder(context, AppConfig.CHANNEL_RELEASE_ID)
                    .setContentTitle(context.getResources().getString(R.string.release_today))
                    .setContentText(movieList.get(0).getTitle())
                    .setSmallIcon(R.drawable.ic_movie)
                    .setGroup(AppConfig.GROUP_RELEASE)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .addLine(movieList.get(0).getTitle())
                    .addLine(movieList.get(1).getTitle())
                    .addLine(movieList.get(2).getTitle())
                    .setBigContentTitle(context.getResources().getString(R.string.release_today_head))
                    .setSummaryText(context.getResources().getString(R.string.release_today));
            nBuilder = new NotificationCompat.Builder(context, AppConfig.CHANNEL_RELEASE_ID)
                    .setContentTitle(context.getResources().getString(R.string.release_today))
                    .setContentText(movieList.get(0).getTitle())
                    .setSmallIcon(R.drawable.ic_movie)
                    .setGroup(AppConfig.GROUP_RELEASE)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(AppConfig.CHANNEL_RELEASE_ID, AppConfig.CHANNEL_RELEASE_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            nBuilder.setChannelId(AppConfig.CHANNEL_RELEASE_ID);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = nBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (notificationManager != null){
            notificationManager.notify(AppConfig.ID_RELEASE, notification);
        }
    }

    public void setDailyReminder(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AppConfig.EXTRA_TYPE, AppConfig.TYPE_REMINDER);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 7);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AppConfig.ID_REMINDER, intent, 0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void setReleaseToday(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AppConfig.EXTRA_TYPE, AppConfig.TYPE_RELEASE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 8);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AppConfig.ID_RELEASE, intent, 0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void getTodayMovies(String releaseDate, final Context context){
        prepareRequest(releaseDate, new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result != null && result.getMovieResults() != null) {
                    movieList = result.getMovieResults();
                    System.out.println(movieList.get(0).getTitle());
                    showNotificationRelease(context, movieList);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }

    public void prepareRequest (String releaseDate, Callback<Result> callback){
        String language = String.valueOf(Locale.getDefault());

        if (language.equals("in_ID")){
            language = "id";
        } else {
            language = "en-US";
        }

        final ApiService apiService = RetrofitClass.getApiService();
        Call<Result> call = apiService.getTodayMovies(AppConfig.API_KEY, language, AppConfig.SORT_POPULARITY, releaseDate, releaseDate);
        call.enqueue(callback);
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(AppConfig.TYPE_REMINDER) ? AppConfig.ID_REMINDER : AppConfig.ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}