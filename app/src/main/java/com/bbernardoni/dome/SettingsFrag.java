package com.bbernardoni.dome;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.support.v4.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class SettingsFrag extends PreferenceFragment {

    public static final String KEY_PREF_CALID_BASE = "pref_key_calID_";

    public SettingsFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = ((AppCompatActivity) getContext()).getSupportActionBar();
        assert ab != null;
        ab.setTitle(R.string.action_settings);

        addPreferencesFromResource(R.xml.preferences);
        PreferenceCategory calendarsCat = (PreferenceCategory)getPreferenceManager().findPreference("pref_key_calendars");

        // display all sorted entries
        ArrayList<CalendarEntry> calEntries = ((MainActivity)getContext()).mCalEntries.calEntries;
        for(int i=0; i<calEntries.size(); i++){
            CheckBoxPreference checkBoxPref = new CheckBoxPreference(getContext());
            checkBoxPref.setTitle(calEntries.get(i).accountName);
            checkBoxPref.setSummary(calEntries.get(i).displayName);
            checkBoxPref.setDefaultValue(true);
            checkBoxPref.setKey(KEY_PREF_CALID_BASE + calEntries.get(i).calID);
            calendarsCat.addPreference(checkBoxPref);
        }
    }

}