package com.bbernardoni.dome;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DayFrag extends Fragment {

    public DayFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar ab = ((AppCompatActivity) getContext()).getSupportActionBar();
        assert ab != null;
        ab.setTitle(R.string.title_day);

        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}