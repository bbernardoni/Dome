package com.bbernardoni.dome;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            default:
            case 0:
                fragment = new MonthFrag();
                break;
            case 1:
                fragment = new WeekFrag();
                break;
            case 2:
                fragment = new DayFrag();
                break;
            case 3:
                fragment = new SettingsFrag();
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            listCalendars();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void listCalendars(){
        String[] returnColumns = new String[] {
                CalendarContract.Calendars._ID,                     //0
                CalendarContract.Calendars.ACCOUNT_NAME,            //1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   //2
                CalendarContract.Calendars.ACCOUNT_TYPE             //3
        };

        Cursor cursor;
        ContentResolver cr = getContentResolver();

        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, returnColumns, null, null, null);

        while (cursor.moveToNext()){
            long calID = cursor.getLong(0);
            String displayName = cursor.getString(1);
            String accountName = cursor.getString(2);
            String accountType = cursor.getString(3);
            Log.i("listCals", "ID="+calID + " DN="+displayName + " AN="+accountName + " AT="+accountType);
        }
        cursor.close();
    }

}
