package com.mianan.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.mianan.blueTooth.MyHandler;
import com.mianan.thread.AcceptThread;
import com.mianan.thread.ConnectThread;
import com.mianan.thread.ConnectedThread;
import com.mianan.utils.normal.ToastUtils;

import java.util.UUID;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class LinkService {
    public static final String TAG = "LinkService";
    public static final UUID uuid = UUID.fromString("2017-2017-2017-2017-0307");
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private int mState;
    private static final boolean D = true;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    public static final int STATE_CONNECT_FAIL = 4;

    public static final int MESSAGE_STATE_CHANGE = 100;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    private BluetoothDevice connecttedDevice;

    private boolean isSingleMode;
    private boolean isOnSleepTime;

    public static LinkService getInstance() {
        return SingletonHolder.linkService;
    }

    private static class SingletonHolder {
        private static final LinkService linkService = new LinkService(MyHandler.getInstance());
    }


    private LinkService(Handler mHandler) {
        this.mHandler = mHandler;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }

    public synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        if (isSingleMode || isOnSleepTime) {
            return;
        }

        if (state == STATE_NONE || state == STATE_CONNECT_FAIL) {
            reset();
        }

        // Give the new state to the Handler so the UI Activity can update
//        mHandler.obtainMessage(state).sendToTarget();
    }

    public synchronized int getState() {
        return mState;
    }

    public synchronized void reset() {
        isSingleMode = false;

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread == null) {
            acceptThread = new AcceptThread(this);
            acceptThread.start();
        }

        setState(MyHandler.STATE_LISTEN);
        sendMessage(MyHandler.STATE_LISTEN);
        connecttedDevice = null;
    }

    public synchronized void safeReset() {
        if (!isSingleMode) {
            reset();
        }
    }

    public synchronized void starSingleModel() {

        isSingleMode = true;

        //如果是在睡眠模式 则不进行操作
        if (isOnSleepTime) {
            return;
        }

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        setState(MyHandler.SINGLE_MODEL);
        sendMessage(MyHandler.SINGLE_MODEL);
        connecttedDevice = null;
    }

    public synchronized void setOnSleepTime(boolean is) {
        isOnSleepTime = is;
        if (is) {
            //如果已经是睡眠模式 则不用处理
            if (getState() == MyHandler.ON_SLEEP_TIME) {
                return;
            }
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
            if (connectedThread != null) {
                connectedThread.cancel();
                connectedThread = null;
            }
            if (acceptThread != null) {
                acceptThread.cancel();
                acceptThread = null;
            }

            setState(MyHandler.ON_SLEEP_TIME);
            sendMessage(MyHandler.ON_SLEEP_TIME);
            connecttedDevice = null;
        } else {
            //如果是睡眠模式 则重新唤醒
            if (getState() == MyHandler.ON_SLEEP_TIME) {
                if (isSingleMode()) {
                    starSingleModel();
                    TimeCount.getInstance().startRecord();
                } else {
                    safeReset();
                }
            }
        }
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice bluetoothDevice) {
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        connectedThread = new ConnectedThread(socket, this);
        connectedThread.start();
        setState(STATE_CONNECTED);
        connecttedDevice = bluetoothDevice;
    }

    public synchronized void resetConnect() {
        connectThread = null;
    }

    public synchronized void connect(BluetoothDevice device) {

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Start the thread to connect with the given device
        connectThread = new ConnectThread(device, this);
        connectThread.start();
        setState(STATE_CONNECTING);
    }

    public synchronized void safeConnet(BluetoothDevice device, Context context) {
        if (isSingleMode) {
            ToastUtils.showShort(context, "单人模式下不能连接设备");
        } else {
            connect(device);
        }
    }

    public synchronized void sendMessage(int message) {
        mHandler.obtainMessage(message).sendToTarget();
    }

    public boolean isSingleMode() {
        return isSingleMode;
    }

    public boolean isOnSleepTime() {
        return isOnSleepTime;
    }

    public void setSingleMode(boolean singleMode) {
        isSingleMode = singleMode;
        if (isOnSleepTime()) {
            return;
        }
        if (singleMode) {
            starSingleModel();
        } else {
            reset();
        }
    }
}
