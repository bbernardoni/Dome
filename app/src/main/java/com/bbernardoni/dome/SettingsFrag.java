package com.bbernardoni.dome;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.provider.CalendarContract;
import android.support.v4.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class SettingsFrag extends PreferenceFragment {

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

        // Get Calendars from content provider
        ArrayList<CalendarEntry> calEntries = new ArrayList<>();
        String[] returnColumns = new String[] {
                CalendarContract.Calendars._ID,                     //0
                CalendarContract.Calendars.ACCOUNT_NAME,            //1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   //2
                CalendarContract.Calendars.ACCOUNT_TYPE             //3
        };
        Cursor cursor;
        ContentResolver cr = getContext().getContentResolver();
        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, returnColumns, null, null, null);
        while (cursor.moveToNext()){
            long calID = cursor.getLong(0);
            String displayName = cursor.getString(1);
            String accountName = cursor.getString(2);
            String accountType = cursor.getString(3);
            if(accountType.equals("com.google")){
                // Add entries to calEntries
                calEntries.add(new CalendarEntry(calID,displayName,accountName));
            }
        }
        cursor.close();

        // display all sorted entries
        Collections.sort(calEntries);
        for(int i=0; i<calEntries.size(); i++){
            CheckBoxPreference checkBoxPref = new CheckBoxPreference(getContext());
            checkBoxPref.setTitle(calEntries.get(i).accountName);
            checkBoxPref.setSummary(calEntries.get(i).displayName);
            checkBoxPref.setDefaultValue(true);
            checkBoxPref.setKey("pref_key_calID_"+calEntries.get(i).calID);
            calendarsCat.addPreference(checkBoxPref);
        }
    }

}