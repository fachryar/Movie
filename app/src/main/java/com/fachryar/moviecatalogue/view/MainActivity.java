package com.fachryar.moviecatalogue.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fachryar.moviecatalogue.R;
import com.fachryar.moviecatalogue.adapter.SectionsPagerAdapter;
import com.fachryar.moviecatalogue.utils.AlarmReceiver;
import com.fachryar.moviecatalogue.utils.AppConfig;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity{
    Boolean isReminderOn, isReleaseOn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        setActionBar();

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_language:
                Intent language = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(language);
                return true;
            case R.id.menu_favorite:
                Intent favorite = new Intent(this, FavoriteActivity.class);
                startActivity(favorite);
                return true;
            case R.id.menu_notification:
                Intent notif = new Intent(this, NotificationSettingActivity.class);
                startActivity(notif);
            default:
                return true;
        }
    }

    private void setActionBar(){
        AppCompatTextView textActionBar = new AppCompatTextView(getApplicationContext());
        textActionBar.setText(R.string.app_name);
        textActionBar.setTextAppearance(this, R.style.white_16);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        if (getSupportActionBar() != null){
            getSupportActionBar().setCustomView(textActionBar, layoutParams);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lil_black)));
        }
    }
}
