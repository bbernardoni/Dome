package com.bbernardoni.dome;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;


public class DayFrag extends Fragment {

    RelativeLayout dayViewLayout;
    CalEntries calEntries;

    public DayFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar ab = ((AppCompatActivity) getContext()).getSupportActionBar();
        assert ab != null;
        ab.setTitle(R.string.title_day);

        dayViewLayout = (RelativeLayout)container.findViewById(R.id.day_view_layout);

        ViewTreeObserver vto = dayViewLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    //noinspection deprecation
                    dayViewLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    dayViewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = dayViewLayout.getMeasuredWidth();
                calEntries = ((MainActivity) getContext()).mCalEntries;
                ArrayList<EventEntry> events = calEntries.getEventsOnDay(cal);
                for (int k = 0; k < events.size(); k++) {

                }
            }
        });

        return inflater.inflate(R.layout.fragment_day, container, false);

    }
/*
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

<TextView
    android:text="Event 1"
    android:layout_marginTop="100dp"
    android:layout_marginStart="0dp"
    android:layout_marginLeft="0dp"
    android:background="@drawable/rounded_corner"
    android:layout_width="wrap_content"
    android:layout_height="50dp" />

<TextView
    android:text="Event 2"
    android:layout_width="wrap_content"
    android:layout_height="50dp" />
 */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}