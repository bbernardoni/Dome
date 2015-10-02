package com.bbernardoni.dome;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Calendar;

public class MonthFrag extends Fragment {

    private LinearLayout monthLayout;
    private MonthScroller monthScroller;

    //Goto date

    public MonthFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_month, container, false);
        monthLayout = (LinearLayout)view.findViewById(R.id.monthLayout);
        monthScroller = (MonthScroller)view.findViewById(R.id.monthScroller);

        ViewTreeObserver vto = monthLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    //noinspection deprecation
                    monthLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    monthLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int height = monthLayout.getMeasuredHeight() / 6;
                monthScroller.setWeekHeight(height);
                for (int i = 0; i < 16; i++) {
                    WeekView weekView = new WeekView(getActivity());
                    weekView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                    monthLayout.addView(weekView);
                }
                monthScroller.setOverScrollMode(View.OVER_SCROLL_NEVER);
                monthScroller.post(new Runnable() {
                    @Override
                    public void run() {
                        monthScroller.scrollTo(0, monthScroller.getWeekHeight() * 5);
                    }
                });

                CalEntries calEntries = ((MainActivity) getContext()).mCalEntries;
                monthScroller.setCalEntries(calEntries);
                monthScroller.setMiddleMonth(Calendar.getInstance());
            }
        });

        Button btn = (Button)view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusable(false);
            }
        });
        return view;
    }

}