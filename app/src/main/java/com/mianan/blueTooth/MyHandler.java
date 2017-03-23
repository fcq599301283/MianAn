package com.mianan.blueTooth;

import android.os.Handler;
import android.os.Message;

import com.mianan.utils.TimeCount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class MyHandler extends Handler {

    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_CONNECT_FAIL = 4;
    public static final int SINGLE_MODEL = 5;         //单人模式
    public static final int ON_SLEEP_TIME = 6;   //睡眠模式

    public static final int LaunchAcceptError = 10;
    public static final int RunAcceptError = 11;
    public static final int LaunchConnectedError = 12;
    public static final int RunConnectedError = 13;
    public static final int connectLose = 14;
    public static final int LaunchConnectError = 15;

    public static final int connectNormal = 100;

    public static final String ok = "ok";
    public static final byte[] okByte = ok.getBytes();

    private List<OnStateChange> OnStateChanges = new ArrayList<>();

    public static MyHandler getInstance() {
        return MyHandler.SingletonHolder.myHandler;
    }

    private static class SingletonHolder {
        private static final MyHandler myHandler = new MyHandler();
    }

    private MyHandler() {
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        for (int i = 0; i < OnStateChanges.size(); i++) {
            OnStateChanges.get(i).onChange(msg);
        }
        switch (msg.what) {
            case STATE_CONNECTED:
                TimeCount.getInstance().startRecord();
                break;
            case connectLose:
                TimeCount.getInstance().endRecord();
                break;
            case LaunchConnectedError:
                TimeCount.getInstance().endRecord();
                break;
        }
    }

    public void register(OnStateChange OnStateChange, boolean register) {
        if (OnStateChange == null) {
            return;
        }
        if (register) {
            OnStateChanges.add(OnStateChange);
        } else {
            OnStateChanges.remove(OnStateChange);
        }
    }

    public interface OnStateChange {
        void onChange(Message msg);
    }
}
