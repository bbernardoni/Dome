package com.bbernardoni.dome;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class MonthScroller extends ScrollView {

    private static final int SWIPE_MIN_DISTANCE = 10;
    private static final int SWIPE_THRESHOLD_VELOCITY = 1000;

    private GestureDetector mGestureDetector;
    private float actionDownY = 0;
    private int weekHeight = 0;
    private int numberOfTVs = 0;
    private static final int WEEK_TV_HEIGHT = 45;
    private static final int WEEK_TV_OFFSET = 61;

    private Calendar today;
    private Calendar focused = null;

    CalEntries calEntries;
    Context mContext;

    public MonthScroller(Context context) {
        super(context);
        init(context);
    }

    public MonthScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonthScroller(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            actionDownY = e.getY();
        }
        return super.onInterceptTouchEvent(e);
    }

    private void init(Context context) {
        mContext = context;
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //If the user swipes
                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    int scrollY = v.getScrollY();
                    if (scrollY < weekHeight * 5 / 2)
                        prevMonth();
                    else if (scrollY > weekHeight * 15 / 2)
                        nextMonth();
                    else
                        ((ScrollView) v).smoothScrollTo(0, weekHeight * 5);
                    return true;
                } else {
                    return false;
                }
            }
        });
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float startY = actionDownY;
                float endY = 0;
                if (e1!=null){
                    startY = e1.getY();
                }
                if (e2!=null){
                    endY = e2.getY();
                }

                //down to up
                if(startY - endY > SWIPE_MIN_DISTANCE){
                    if(-velocityY > SWIPE_THRESHOLD_VELOCITY) {
                        nextMonth();
                        return true;
                    }else if(velocityY > SWIPE_THRESHOLD_VELOCITY){
                        smoothScrollTo(0, weekHeight * 5);
                        return true;
                    }
                }
                //up to down
                else if (endY - startY > SWIPE_MIN_DISTANCE) {
                    if(velocityY > SWIPE_THRESHOLD_VELOCITY) {
                        prevMonth();
                        return true;
                    }else if(-velocityY > SWIPE_THRESHOLD_VELOCITY){
                        smoothScrollTo(0, weekHeight * 5);
                        return true;
                    }
                }
                return false;
            }
        });
        today = Calendar.getInstance();
        focused = (Calendar)today.clone();
        focused.set(Calendar.DAY_OF_MONTH, 1);
    }

    public void setWeekHeight(int height){
        weekHeight = height;
        numberOfTVs = (weekHeight-WEEK_TV_OFFSET)/WEEK_TV_HEIGHT;
    }

    public int getWeekHeight(){
        return weekHeight;
    }

    public void setCalEntries(CalEntries _calEntries){
        calEntries = _calEntries;
    }

    private void nextMonth() {
        int curWeek = focused.get(Calendar.WEEK_OF_YEAR);
        focused.add(Calendar.MONTH, 1);
        int weeksSpanned;
        if(curWeek>47){
            if(focused.get(Calendar.DAY_OF_WEEK) > Calendar.TUESDAY)
                weeksSpanned = 4;
            else
                weeksSpanned = 5;
        }else{
            weeksSpanned = focused.get(Calendar.WEEK_OF_YEAR) - curWeek;
        }
        smoothScrollTo(0, weekHeight * (5 + weeksSpanned));
        postDelayed(new Runnable() {
            public void run() {
                scrollTo(0, weekHeight * 5);
                setMiddleMonth((Calendar) focused.clone());
            }
        }, 270);
    }

    private void prevMonth() {
        int curWeek = focused.get(Calendar.WEEK_OF_YEAR);
        focused.add(Calendar.MONTH, -1);
        int weeksSpanned;
        if(curWeek < 4){
            if(focused.get(Calendar.DAY_OF_WEEK) < Calendar.THURSDAY)
                weeksSpanned = 4;
            else
                weeksSpanned = 5;
        }else{
            weeksSpanned = curWeek - focused.get(Calendar.WEEK_OF_YEAR);
        }
        smoothScrollTo(0, weekHeight * (5 - weeksSpanned));
        postDelayed(new Runnable() {
            public void run() {
                scrollTo(0, weekHeight * 5);
                setMiddleMonth((Calendar) focused.clone());
            }
        }, 270);
    }

    public void setMiddleMonth(Calendar cal){
        setActionBarTitle(cal);
        int month = cal.get(Calendar.MONTH);

        cal.set(Calendar.WEEK_OF_MONTH, 1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.WEEK_OF_YEAR, -5);
        for (int i = 0; i < 16; i++) {
            WeekView weekView = (WeekView) ((LinearLayout)getChildAt(0)).getChildAt(i);
            for (int j = 0; j < 7; j++) {
                int day = cal.get(Calendar.DAY_OF_MONTH);
                LinearLayout btnLayout = (LinearLayout)weekView.getChildAt(j);
                RelativeLayout btnDayLayout = (RelativeLayout)btnLayout.getChildAt(0);
                TextView btnDay = (TextView)btnDayLayout.getChildAt(0);
                if(today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                        today.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)) {
                    btnDay.setTextColor(Color.BLUE);
                }else if(month == cal.get(Calendar.MONTH)){
                    btnDay.setTextColor(Color.BLACK);
                }else{
                    btnDay.setTextColor(Color.GRAY);
                }
                btnDay.setText("" + day);

                // add event textview
                btnLayout.removeViews(1, btnLayout.getChildCount()-1);
                ArrayList<EventEntry> events = calEntries.getEventsOnDay(cal);
                for (int k = 0; k < events.size() && k < numberOfTVs; k++) {
                    TextView btnEvent = new TextView(mContext);
                    btnEvent.setSingleLine();
                    btnEvent.setText(events.get(k).title);
                    btnEvent.setTextSize(10.0f);
                    btnEvent.setTypeface(Typeface.DEFAULT_BOLD);
                    btnEvent.setTextColor(Color.WHITE);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    llp.setMargins(0, 0, 0, 2);
                    btnEvent.setLayoutParams(llp);
                    btnEvent.setBackgroundResource(R.drawable.rounded_corner);
                    ((GradientDrawable)btnEvent.getBackground()).setColor(events.get(k).dispColor);
                    btnLayout.addView(btnEvent);
                }
                // display +N if there are more events that are not displayed
                if(numberOfTVs < events.size()){
                    TextView tvPlusN;
                    if(btnDayLayout.getChildCount()>1){
                        tvPlusN = (TextView)btnDayLayout.getChildAt(1);
                    }else{
                        tvPlusN = new TextView(mContext);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        tvPlusN.setLayoutParams(params);
                        tvPlusN.setTextSize(12.0f);
                        tvPlusN.setTypeface(Typeface.DEFAULT_BOLD);
                        tvPlusN.setTextColor(Color.WHITE);
                        tvPlusN.setBackgroundResource(R.drawable.rounded_corner);
                        btnDayLayout.addView(tvPlusN);
                    }
                    ((GradientDrawable)tvPlusN.getBackground()).setColor(btnDay.getCurrentTextColor());
                    tvPlusN.setText("+" + (events.size() - numberOfTVs));
                }else if(btnDayLayout.getChildCount()>1){
                    btnDayLayout.removeViews(1, btnDayLayout.getChildCount()-1);
                }

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
    }

    public void setActionBarTitle(Calendar cal){
        String title;
        int year = cal.get(Calendar.YEAR);
        if(year == today.get(Calendar.YEAR)){
            title = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        }else{
            title = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)+" "+year;
        }
        ActionBar ab = ((AppCompatActivity) getContext()).getSupportActionBar();
        assert ab != null;
        ab.setTitle(title);
    }
}