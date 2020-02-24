package com.fachryar.moviecatalogue.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.fachryar.moviecatalogue.R;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    private String NOTIF_REMINDER;
    private String NOTIF_RELEASE;

    private SwitchPreference reminderPreference;
    private SwitchPreference releasePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        init();
        setSwitch();
    }

    private void init() {
        NOTIF_REMINDER = getResources().getString(R.string.key_reminder);
        NOTIF_RELEASE = getResources().getString(R.string.key_release);

        reminderPreference = findPreference(NOTIF_REMINDER);
        releasePreference = findPreference(NOTIF_RELEASE);
    }

    private void setSwitch(){
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        reminderPreference.setChecked(sharedPreferences.getBoolean(NOTIF_REMINDER, false));
        releasePreference.setChecked(sharedPreferences.getBoolean(NOTIF_RELEASE, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(NOTIF_REMINDER)) {
            reminderPreference.setChecked(sharedPreferences.getBoolean(NOTIF_REMINDER, false));
        }

        if (key.equals(NOTIF_RELEASE)) {
            releasePreference.setChecked(sharedPreferences.getBoolean(NOTIF_RELEASE, false));
        }
    }
}
