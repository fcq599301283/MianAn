package com.mianan.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.mianan.BlueTooth.MyHandler;
import com.mianan.thread.AcceptThread;
import com.mianan.thread.ConnectThread;
import com.mianan.thread.ConnectedThread;

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
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        setState(MyHandler.STATE_LISTEN);
        connecttedDevice = null;

        if (acceptThread == null) {
            acceptThread = new AcceptThread(this);
            acceptThread.start();
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

    public synchronized void sendMessage(int message) {
        mHandler.obtainMessage(message).sendToTarget();
    }
}
