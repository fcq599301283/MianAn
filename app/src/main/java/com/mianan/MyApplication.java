package com.mianan;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.mianan.blueTooth.MyHandler;
import com.mianan.broadcastReciever.BTBroadcastReceiver;
import com.mianan.broadcastReciever.ScreenBroadcaset;
import com.mianan.broadcastReciever.TimeBroadcastReceiver;
import com.mianan.utils.LinkService;
import com.mianan.utils.TimeCount;
import com.mianan.utils.normal.SystemUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by FengChaoQun
 * on 2017/1/2
 */

public class MyApplication extends Application {
    public Activity currentActivity;
    private static MyApplication instance;
    private Map<String, Activity> mList = new HashMap<String, Activity>();
    private int activityCount = 0;
    private BTBroadcastReceiver btBroadcastReceiver = new BTBroadcastReceiver();
    private ScreenBroadcaset screenBroadcaset = new ScreenBroadcaset();


    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //      初始化数据库
        RealmConfiguration config = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

         /*
        **describe:集中监视Activity生命周期
        */
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                addActivity(activity);
                Log.d(activity.getClass().getSimpleName(), "created");
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(activity.getClass().getSimpleName(), "resumed");
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                removeActivity(activity);
                activityCount--;
                Log.d(activity.getClass().getSimpleName(), "destroyed");
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        this.registerReceiver(btBroadcastReceiver, filter);

        IntentFilter screenStatusIF = new IntentFilter();
        screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
        screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(screenBroadcaset, screenStatusIF);


        TimeCount.getInstance().setScreenOn(true);
        MyHandler.getInstance();
        LinkService.getInstance();

        if (inMainProcess()) {
            Bugly.init(getApplicationContext(), "7cff1963e6", BuildConfig.DEBUG);
            Beta.upgradeCheckPeriod = 30 * 60 * 1000;
        }
    }

    public void addActivity(Activity activity) {
        mList.put(activity.toString(), activity);
        activityCount++;
    }

    /*
    **describe:移除Activity
    */
    public void removeActivity(Activity activity) {
        mList.remove(activity.toString());
    }

    /*
    **describe:完全退出APP
    */
    public void exit() {
        try {
            for (Map.Entry<String, Activity> entry : mList.entrySet()) {
                entry.getValue().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /*
        **describe:finish某个activity
        */
    public void finish_activity(String tag) {
        if (mList.containsKey(tag)) {
            mList.get(tag).finish();
        }
    }

    //  判断是否处于主线程
    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }
}
