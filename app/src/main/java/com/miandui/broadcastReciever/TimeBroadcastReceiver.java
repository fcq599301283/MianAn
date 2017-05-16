package com.miandui.broadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.miandui.utils.LinkService;

import java.util.Calendar;

/**
 * Created by FengChaoQun
 * on 2017/3/21
 */
public class TimeBroadcastReceiver extends BroadcastReceiver {
    private final int divider = 8 * 60;

    public TimeBroadcastReceiver() {
        calculateCurrentModel();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            calculateCurrentModel();
        }
    }

    private void calculateCurrentModel() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int totalMinute = hour * 60 + minute;
        if (totalMinute < divider) {
            onSleepTime();
        } else {
            notOnSleepTime();
        }
    }

    private void onSleepTime() {
        LinkService.getInstance().setOnSleepTime(true);
        Log.d("TimeBroadcastReceiver", "onSleepTime:true");
    }

    private void notOnSleepTime() {
        LinkService.getInstance().setOnSleepTime(false);
        Log.d("TimeBroadcastReceiver", "onSleepTime:false");
    }
}
