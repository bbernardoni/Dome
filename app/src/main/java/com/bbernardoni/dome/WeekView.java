package com.bbernardoni.dome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class WeekView extends LinearLayout{

    public WeekView(Context context) {
        super(context);
        initializeViews(context);
    }

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.week_view, this);
    }

    // Will probably need to handle listeners
}
