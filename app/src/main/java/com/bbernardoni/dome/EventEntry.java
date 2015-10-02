package com.bbernardoni.dome;

import android.support.annotation.NonNull;

public class EventEntry implements Comparable<EventEntry>{
    int calID = 0;
    String title = "";
    String eventLocation = "";
    String description = "";
    long dtstart = 0;
    long dtend = 0;
    String eventTimezone = "";
    int allDay = 0;
    String rrule = "";
    int dispColor = 0;
    long begin = 0;
    long end = 0;
    long eventID = 0;

    EventEntry(int _calID, String _title, String _eventLocation, String _description,
               long _dtstart, long _dtend, String _eventTimezone, int _allDay,
               String _rrule, int _dispColor, long _begin, long _end, long _eventID){
        calID = _calID;
        title = _title;
        eventLocation = _eventLocation;
        description = _description;
        dtstart = _dtstart;
        dtend = _dtend;
        eventTimezone = _eventTimezone;
        allDay = _allDay;
        rrule = _rrule;
        dispColor = _dispColor;
        begin = _begin;
        end = _end;
        eventID = _eventID;
    }

    @Override
    public int compareTo(@NonNull EventEntry another) {
        if((this.rrule == null) != (another.rrule == null))
            return (this.rrule == null)? -1: 1;
        if(this.allDay != another.allDay)
            return (this.allDay == 1)? -1: 1;
        if(this.begin != another.begin)
            return (this.begin < another.begin)? -1: 1;
        if(this.end != another.end)
            return (this.end < another.end)? -1: 1;
        return 0;
    }
}
