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
                CheckBoxPreference checkBoxPref = new CheckBoxPreference(getContext());
                checkBoxPref.setTitle(accountName);
                checkBoxPref.setSummary(displayName);
                checkBoxPref.setDefaultValue(true);
                checkBoxPref.setKey("pref_key_calID_"+calID);
                calendarsCat.addPreference(checkBoxPref);
            }
        }
        cursor.close();
    }

}