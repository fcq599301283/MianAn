package com.mianan.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.mianan.blueTooth.MyHandler;
import com.mianan.utils.BTUtils;
import com.mianan.utils.LinkService;

import java.io.IOException;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private LinkService linkService;

    public ConnectThread(BluetoothDevice device, LinkService linkService) {
        mmDevice = device;
        this.linkService = linkService;
        BluetoothSocket tmp = null;

        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            tmp = device.createRfcommSocketToServiceRecord(
                    LinkService.uuid);

        } catch (IOException e) {
            e.printStackTrace();
            linkService.sendMessage(MyHandler.LaunchConnectError);
        }
        mmSocket = tmp;
    }

    public void run() {
        setName("ConnectThread");

        // Always cancel discovery because it will slow down a connection
        BTUtils.bluetoothAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            linkService.setState(LinkService.STATE_CONNECTING);
            linkService.sendMessage(MyHandler.STATE_CONNECTING);
            mmSocket.connect();
        } catch (IOException e) {
            // Close the socket
            try {
                mmSocket.close();
            } catch (IOException e2) {
                e.printStackTrace();
            }
            linkService.setState(LinkService.STATE_CONNECT_FAIL);
            linkService.sendMessage(MyHandler.STATE_CONNECT_FAIL);
            return;
        }

        // Reset the ConnectThread because we're done
        synchronized (this) {
            linkService.resetConnect();
        }

        // Start the connected thread
        linkService.setState(LinkService.STATE_CONNECTED);
        linkService.connected(mmSocket, mmDevice);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
