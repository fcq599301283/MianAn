package com.mianan.Self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mianan.R;
import com.mianan.utils.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class TodayTimeFrag extends BaseFragment {
    @Bind(R.id.chronometer)
    Chronometer chronometer;
    @Bind(R.id.today_time)
    TextView todayTime;
    @Bind(R.id.today_grade)
    TextView todayGrade;
    private int baseTime = 3600;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_today_time);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chronometer.setFormat(""); // Self dont show
        chronometer.setBase(baseTime); // set base value
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            long value = -1;

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                baseTime++;
                chronometer.setText(FormatMiss(baseTime)); // overriding text show
            }
        });

        chronometer.start();

    }

    public static String FormatMiss(int miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
