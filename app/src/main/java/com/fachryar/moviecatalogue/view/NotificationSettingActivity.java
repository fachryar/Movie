package com.fachryar.moviecatalogue.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.model.Movies;
import com.fachryar.moviecatalogue.utils.AlarmReceiver;
import com.fachryar.moviecatalogue.utils.AppConfig;
import com.fachryar.moviecatalogue.viewmodel.MoviesViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationSettingActivity extends AppCompatActivity {
    Boolean isReminderOn, isReleaseOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);
        setActionBar();
        getSupportFragmentManager().beginTransaction().add(R.id.setting_holder, new PreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            checkNotifPref();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBar(){
        AppCompatTextView textActionBar = new AppCompatTextView(getApplicationContext());
        textActionBar.setText(R.string.notification_setting);
        textActionBar.setTextAppearance(this, R.style.white_16);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        if (getSupportActionBar() != null){
            getSupportActionBar().setCustomView(textActionBar, layoutParams);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void checkNotifPref(){
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.contains(getResources().getString(R.string.key_reminder))){
            isReminderOn = sharedPreferences.getBoolean(getResources().getString(R.string.key_reminder), false);
            isReleaseOn = sharedPreferences.getBoolean(getResources().getString(R.string.key_release), false);

            if (isReminderOn){
                alarmReceiver.setDailyReminder(this);
            } else {
                alarmReceiver.cancelAlarm(this, AppConfig.TYPE_REMINDER);
            }

            if (isReleaseOn){
                alarmReceiver.setReleaseToday(this);
            } else {
                alarmReceiver.cancelAlarm(this, AppConfig.TYPE_RELEASE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        checkNotifPref();
        finish();
    }
}
