package com.mianan.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mianan.blueTooth.MyHandler;
import com.mianan.MainActivity;
import com.mianan.R;
import com.mianan.broadcastReciever.TimeBroadcastReceiver;
import com.mianan.thread.SaveDataThread;
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
    private MyHandler.OnStateChange onStateChange;
    private SaveDataThread saveDataThread;

    private final IBinder iBinder = new MyBinder();
    private TimeBroadcastReceiver timeBroadcastReceiver;

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
                .setContentText("等待连接...")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.logo_toolbar);
        notification = builder.build();

        timeBroadcastReceiver = new TimeBroadcastReceiver();
        IntentFilter timeChange = new IntentFilter();
        timeChange.addAction(Intent.ACTION_TIME_TICK);
        this.registerReceiver(timeBroadcastReceiver, timeChange);

        registerObservers(true);
    }

    public void refreshNotification(String text) {
        builder.setContentText(text);
        startForeground(NotificationId, builder.build());
        Log.d(TAG, text);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        registerObservers(false);
        this.unregisterReceiver(timeBroadcastReceiver);
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

    private void registerObservers(boolean register) {
        if (onStateChange == null) {
            onStateChange = new MyHandler.OnStateChange() {
                @Override
                public void onChange(Message msg) {
                    switch (msg.what) {
                        case MyHandler.STATE_CONNECTED:
                            BluetoothDevice device = (BluetoothDevice) msg.obj;
                            refreshNotification("已经与" + device.getName() + "连接,息屏即可开始积分");
                            startSaveDataThread();
                            break;
                        case MyHandler.SINGLE_MODEL:
                            refreshNotification("单人模式积分中...");
                            startSaveDataThread();
                            break;
                        case MyHandler.STATE_LISTEN:
                            refreshNotification("等待连接...");
                            endSaveDataThread();
                            break;
                        case MyHandler.ON_SLEEP_TIME:
                            refreshNotification("睡眠时间,不计入积分");
                            endSaveDataThread();
                            break;
                        case MyHandler.STATE_NONE:
                            refreshNotification("请开启蓝牙或单人模式开始积分");
                            break;
                    }
                    Log.d(TAG, "msg:" + msg);
                }
            };
        }

        MyHandler.getInstance().register(onStateChange, register);
    }

    private void registerBroadcast() {

    }

    private synchronized void startSaveDataThread() {
        if (saveDataThread == null) {
            saveDataThread = new SaveDataThread();
            saveDataThread.start();
        }
    }

    private synchronized void endSaveDataThread() {
        if (saveDataThread != null) {
            saveDataThread.cancle();
            saveDataThread = null;
        }
    }

    public class MyBinder extends Binder {

        public MyService getMyService() {
            return MyService.this;
        }

    }
}
