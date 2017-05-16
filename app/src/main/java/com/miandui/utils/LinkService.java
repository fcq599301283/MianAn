package com.miandui.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.miandui.blueTooth.MyHandler;
import com.miandui.thread.AcceptThread;
import com.miandui.thread.ConnectThread;
import com.miandui.thread.ConnectedThread;
import com.miandui.utils.normal.ToastUtils;

import java.util.UUID;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class LinkService {
    public static final String TAG = "LinkService";
    public static final UUID uuid = UUID.fromString("1995-0925-2838-2017-0307");
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    private BluetoothDevice connecttedDevice;

    private boolean isBTModel;
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
    }

    /**
     * description:开启蓝牙模式
     * 1.如果是睡眠模式 则不进行操作
     * 2.如果是单人模式 则不进行操作
     * 3.如果蓝牙不可用 则不进行操作
     * 4.关闭已有的所有连接 重新开始监听
     * 5.设置当前状态为等待连接
     */

    private synchronized void startBTModel() {

        if (isOnSleepTime() || isSingleMode() || mAdapter == null) {
            Log.d(TAG, "launch startBTModel fail:the current state forbidden launch BTModel");
            return;
        }

        //关闭已有连接
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        //开启监听
        if (acceptThread == null) {
            acceptThread = new AcceptThread(this);
            acceptThread.start();
        }

        sendMessage(MyHandler.STATE_LISTEN);
        connecttedDevice = null;

        Log.d(TAG, "open BTModel successful");
    }

    /**
     * description:关闭蓝牙模式
     * 1.如果蓝牙不可用 则不进行操作
     * 2.关闭所有的已有连接 停止监听
     * 3.闭关系统蓝牙
     */

    private synchronized void closeBTModel() {
        if (mAdapter == null) {
            Log.d(TAG, "close BTModel fail:the system bluetooth is null");
            return;
        }

        closeBTConnected();

        resetModel();

        mAdapter.disable();

        Log.d(TAG, "close BTModel successful");
    }

    public synchronized void setBTModel(boolean btModel) {
        this.isBTModel = btModel;
        if (btModel) {
            startBTModel();
        } else {
            closeBTModel();
        }
    }

    public boolean isBTModel() {
        return isBTModel;
    }

    /**
     * description:开启单人模式
     * 1.如果是睡眠模式 则不进行操作
     * 2.关闭所有的已有连接
     * 3.设置当前状态为单人模式
     */

    public synchronized void startSingleModel() {

        //如果是在睡眠模式 则不进行操作
        if (isOnSleepTime()) {
            Log.d(TAG, "launch singleModel fail:sleepModel forbidden launch singleModel");
            return;
        }

        closeBTConnected();

        sendMessage(MyHandler.SINGLE_MODEL);

        Log.d(TAG, "launch singleModel successful");
    }

    /**
     * description:关闭单人模式
     * 1.如果是睡眠模式 则不进行操作
     * 2.重置模式
     */

    private synchronized void closeSingleModel() {
        //如果是在睡眠模式 则不进行操作
        if (isOnSleepTime()) {
            Log.d(TAG, "close singleModel fail:sleepModel forbidden launch singleModel");
            return;
        }

        resetModel();

        Log.d(TAG, "close singleModel successful");
    }

    public void setSingleMode(boolean singleMode) {
        isSingleMode = singleMode;
        if (singleMode) {
            startSingleModel();
        } else {
            closeSingleModel();
        }
    }

    public boolean isSingleMode() {
        return isSingleMode;
    }

    /**
     * description:开启睡眠模式
     * 1.如果已经是睡眠模式 则不用处理
     * 2.关闭已有连接
     * 3.设置当前状态为单人模式
     */
    private synchronized void startSleepModel() {

        //如果已经是睡眠模式 则不用处理
        if (MyHandler.getInstance().getCurrentState() == MyHandler.ON_SLEEP_TIME) {
            return;
        }

        closeBTConnected();

        sendMessage(MyHandler.ON_SLEEP_TIME);
    }

    /**
     * description:关闭睡眠模式
     * 1.如果本来就不是睡眠 则不进行操作
     * 2.重置模式
     */

    private synchronized void closeSleepModel() {

        if (MyHandler.getInstance().getCurrentState() != MyHandler.ON_SLEEP_TIME) {
            return;
        }

        resetModel();
    }


    public synchronized void setOnSleepTime(boolean is) {
        isOnSleepTime = is;
        if (is) {
            startSleepModel();
        } else {
            closeSleepModel();
        }
    }

    /**
     * description:重置模式 按优先级开启
     * 1.如果是睡眠模式 则开启睡眠模式
     * 2.如果是单人模式 则开启单人模式
     * 3.如果是蓝牙模式 则开启蓝牙模式
     * 4.如果以上都不是 则开启默认模式
     */

    private synchronized void resetModel() {
        if (isOnSleepTime()) {
            if (MyHandler.getInstance().getCurrentState() != MyHandler.ON_SLEEP_TIME) {
                startSleepModel();
            }
        } else if (isSingleMode()) {
            if (MyHandler.getInstance().getCurrentState() != MyHandler.SINGLE_MODEL) {
                startSingleModel();
            }
        } else if (isBTModel()) {
            if (MyHandler.getInstance().getCurrentState() != MyHandler.STATE_LISTEN) {
                startBTModel();
            }
        } else {
            startDefaultModel();
        }
    }

    private synchronized void startDefaultModel() {
        sendMessage(MyHandler.STATE_NONE);
    }

    /**
     * description:关闭已有的连接 取消监听 将已连接设备置空
     */

    private synchronized void closeBTConnected() {
        //关闭已有的连接
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        //取消监听
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        connecttedDevice = null;
    }


    /**
     * description:和其余蓝牙建立连接
     * 关闭已有的连接
     */

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice bluetoothDevice) {

        closeBTConnected();

        connectedThread = new ConnectedThread(socket, this);
        connectedThread.start();
        connecttedDevice = bluetoothDevice;
        MyHandler.getInstance().obtainMessage(MyHandler.STATE_CONNECTED, socket.getRemoteDevice()).sendToTarget();
    }

    public synchronized void resetConnect() {
        connectThread = null;
    }

    private synchronized void connect(BluetoothDevice device) {

        // Cancel any thread attempting to make a connection
        if (MyHandler.getInstance().getCurrentState() == MyHandler.STATE_CONNECTING) {
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
        sendMessage(MyHandler.STATE_CONNECTING);
    }

    public synchronized void Connect(BluetoothDevice device, Context context) {
        if (isSingleMode) {
            ToastUtils.showShort(context, "单人模式下不能连接设备");
        } else {
            connect(device);
        }
    }

    public synchronized void sendMessage(int message) {
        mHandler.obtainMessage(message).sendToTarget();
    }

    public boolean isOnSleepTime() {
        return isOnSleepTime;
    }

}
