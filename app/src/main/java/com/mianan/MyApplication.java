package com.mianan;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

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
}
