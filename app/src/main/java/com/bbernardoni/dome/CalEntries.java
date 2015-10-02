package com.bbernardoni.dome;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CalEntries {

    ArrayList<CalendarEntry> calEntries = new ArrayList<>();
    ArrayList<EventEntry> eventEntries = new ArrayList<>();
    Context context;

    CalEntries(Context _context){
        context = _context;
    }

    public void initEvents(){
        eventEntries.clear();
        Cursor cursor;
        ContentResolver cr = context.getContentResolver();
        //String selection = CalendarContract.Calendars.ACCOUNT_TYPE + " = ?";
        //String[] selectionArgs = new String[] {"com.google"};
        String[] returnColumns = new String[] {
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_TIMEZONE,
                CalendarContract.Events.ALL_DAY,
                CalendarContract.Events.RRULE,
                CalendarContract.Events.DISPLAY_COLOR,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END,
                CalendarContract.Instances.EVENT_ID
        };

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2011, 9, 23, 8, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 10, 24, 8, 0);
        long endMillis = endTime.getTimeInMillis();

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        cursor = cr.query(builder.build(), returnColumns, null, null, null);

        while (cursor.moveToNext()){
            int calID = cursor.getInt(0);
            String title = cursor.getString(1);
            String eventLocation = cursor.getString(2);
            String description = cursor.getString(3);
            long dtstart = cursor.getLong(4);
            long dtend = cursor.getLong(5);
            String eventTimezone = cursor.getString(6);
            int allDay = cursor.getInt(7);
            String rrule = cursor.getString(8);
            int dispColor = cursor.getInt(9);
            long begin = cursor.getLong(10);
            long end = cursor.getLong(11);
            long eventID = cursor.getLong(12);

            eventEntries.add(new EventEntry(calID,title,eventLocation,description,
                    dtstart,dtend,eventTimezone,allDay,rrule,dispColor,begin,end,eventID));
        }
        cursor.close();

        // display all sorted entries
        Collections.sort(eventEntries);
    }

    public void initCalendars(){
        calEntries.clear();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        // Get Calendars from content provider
        Cursor cursor;
        ContentResolver cr = context.getContentResolver();
        String selection = CalendarContract.Calendars.ACCOUNT_TYPE + " = ?";
        String[] selectionArgs = new String[] {"com.google"};
        String[] returnColumns = new String[] {
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };

        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, returnColumns, selection, selectionArgs, null);
        while (cursor.moveToNext()){
            int calID = cursor.getInt(0);
            String displayName = cursor.getString(1);
            String accountName = cursor.getString(2);

            // Add entries to calEntries
            boolean enabled = sharedPref.getBoolean(SettingsFrag.KEY_PREF_CALID_BASE+calID, false);
            calEntries.add(new CalendarEntry(calID,displayName,accountName,enabled));
        }
        cursor.close();

        // display all sorted entries
        Collections.sort(calEntries);
    }

    public ArrayList<EventEntry> getEventsOnDay(Calendar day) {
        ArrayList<EventEntry> retEvents = new ArrayList<>();

        for (int i = 0; i < eventEntries.size(); i++) {
            EventEntry entry = eventEntries.get(i);
            Calendar eventDay = Calendar.getInstance();
            eventDay.setTimeInMillis(entry.begin);

            if(findByID(entry.calID).enabled &&
                    day.get(Calendar.YEAR) == eventDay.get(Calendar.YEAR) &&
                    day.get(Calendar.DAY_OF_YEAR) == eventDay.get(Calendar.DAY_OF_YEAR)){
                retEvents.add(entry);
            }
        }
        return retEvents;
    }

    public CalendarEntry findByID(int ID){
        for (int i = 0; i < calEntries.size(); i++) {
            if(calEntries.get(i).calID == ID){
                return calEntries.get(i);
            }
        }
        return null;
    }
}
