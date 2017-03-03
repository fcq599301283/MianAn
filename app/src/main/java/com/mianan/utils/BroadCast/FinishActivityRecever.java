package com.mianan.utils.BroadCast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by FengChaoQun
 * on 2016/10/15
 * 广播  关闭activity
 */

public class FinishActivityRecever extends BroadcastReceiver {
    public static final String FINISH = "FINISH";
    private Activity activity;

    public FinishActivityRecever(Activity activity) {
        this.activity = activity;
    }

    public static void sendFinishBroadcast(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(FINISH);
        activity.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        activity.finish();
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FINISH);
        activity.registerReceiver(this, intentFilter);
    }

    public void unregister() {
        activity.unregisterReceiver(this);
    }
}
