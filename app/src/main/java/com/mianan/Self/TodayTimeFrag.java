package com.mianan.Self;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mianan.NetWork.callBack.SimpleCallback;
import com.mianan.NetWork.netUtil.BTNetUtils;
import com.mianan.R;
import com.mianan.data.MarkAndTime;
import com.mianan.utils.TempUser;
import com.mianan.utils.base.BaseFragment;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by FengChaoQun
 * on 2017/2/23
 */

public class TodayTimeFrag extends BaseFragment {
    @Bind(R.id.chronometer)
    Chronometer chronometer;
    //    @Bind(R.id.today_time)
//    TextView todayTime;
    @Bind(R.id.today_grade)
    TextView todayGrade;
    private TempUser.onMarkChange onMarkChange;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(R.layout.frag_today_time);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        registerObservers(false);
    }

    private void initView() {
        chronometer.setText(TempUser.getMarkAndTime().getTodayTime());
//        todayTime.setText("今日累计时间:" + TempUser.getTodayTime());
        todayGrade.setText("今日累计积分:" + TempUser.getMarkAndTime().getTodayMark() + "分");
        registerObservers(true);
    }

    public void setTodayTime(long totalTime) {
        if (chronometer == null) {
            return;
        }
        if (totalTime == 0) {
//            chronometer.setBase(SystemClock.elapsedRealtime());
//            todayTime.setText("今日累计时间:0");
        } else {
            chronometer.setBase(totalTime / 1000);
            chronometer.setText(TodayTimeFrag.FormatMiss(totalTime / 1000));
//            todayTime.setText("今日累计时间:" + TodayTimeFrag.FormatMiss(totalTime / 1000));
        }
    }

    public void setTodayMark(String mark) {
        if (todayGrade == null) {
            return;
        }
        todayGrade.setText("今日累计积分:" + mark + "分");
    }

    public static String FormatMiss(long miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    private void registerObservers(boolean register) {
        if (onMarkChange == null) {
            onMarkChange = new TempUser.onMarkChange() {
                @Override
                public void onChange(MarkAndTime markAndTime) {
                    chronometer.setText(markAndTime.getTodayTime());
//                    todayTime.setText("今日累计时间:" + time);
                    todayGrade.setText("今日累计积分:" + markAndTime.getTodayMark() + "分");
                }
            };
        }
        TempUser.registerOnMarkChangeObserver(onMarkChange, register);
    }

    public void onRefresh() {
        BTNetUtils.refreshMarkAndTimeBack(new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
            }

            @Override
            public void onFail(String code, String msg) {
                showToast(msg);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast("加载异常");
            }
        });

        Log.d("refresh", "refreshMarkAndTimeBack");
    }

}
