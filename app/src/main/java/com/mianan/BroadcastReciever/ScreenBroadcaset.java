package com.mianan.BroadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mianan.utils.TimeCount;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class ScreenBroadcaset extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            TimeCount.getInstance().setScreenOn(true);
            Log.d("Screen", "is on");
        }
        // 屏幕休眠
        else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            TimeCount.getInstance().setScreenOn(false);
            Log.d("Screen", "is off");
        }
    }
}
