package com.bbernardoni.dome;

public class CalendarEntry implements Comparable<CalendarEntry>{
    long calID = 0;
    String displayName = "";
    String accountName = "";

    CalendarEntry(long _CalID, String _DisplayName, String _AccountName){
        calID = _CalID;
        displayName = _DisplayName;
        accountName = _AccountName;
    }

    @Override
    public int compareTo(CalendarEntry another) {
        int ret = this.displayName.compareTo(another.displayName);
        if(ret != 0)
            return ret;
        ret = this.accountName.compareTo(another.accountName);
        if(ret != 0)
            return ret;
        return 0;
    }
}
