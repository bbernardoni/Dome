package com.bbernardoni.dome;

import android.support.annotation.NonNull;

public class CalendarEntry implements Comparable<CalendarEntry>{
    int calID = 0;
    String displayName = "";
    String accountName = "";
    boolean enabled = false;

    CalendarEntry(int _CalID, String _DisplayName, String _AccountName, boolean _enabled){
        calID = _CalID;
        displayName = _DisplayName;
        accountName = _AccountName;
        enabled = _enabled;
    }

    @Override
    public int compareTo(@NonNull CalendarEntry another) {
        int ret = this.displayName.compareTo(another.displayName);
        if(ret != 0)
            return ret;
        ret = this.accountName.compareTo(another.accountName);
        if(ret != 0)
            return ret;
        return 0;
    }
}
