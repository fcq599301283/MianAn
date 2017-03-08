package com.mianan.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mianan.MainActivity;
import com.mianan.R;
import com.mianan.utils.LinkService;

/**
 * Created by FengChaoQun
 * on 2017/3/7
 */

public class MyService extends Service {

    public static final String TAG = "MyService";

    private Notification.Builder builder;
    private Notification notification;
    public static final int NotificationId = 1;

    private final IBinder iBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), NotificationId, intent, 0);

        builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle("面对")
                .setContentText("正在为您积分,请不要关闭")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.logo_toolbar);
        notification = builder.build();
    }

    public void refreshNotification(String text) {
        builder.setContentText(text);
        startForeground(NotificationId, builder.build());
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
        Log.d(TAG, "stop");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NotificationId, notification);
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public void stop() {
        stopSelf();
    }

    public void startListen() {
        if (LinkService.getInstance().getState() == LinkService.STATE_CONNECT_FAIL ||
                LinkService.getInstance().getState() == LinkService.STATE_NONE) {
            LinkService.getInstance().reset();
        }
    }

    public class MyBinder extends Binder {

        public MyService getMyService() {
            return MyService.this;
        }

    }
}
