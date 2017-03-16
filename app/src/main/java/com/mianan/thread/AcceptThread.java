package com.mianan.thread;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.mianan.blueTooth.MyHandler;
import com.mianan.utils.BTUtils;
import com.mianan.utils.LinkService;

import java.io.IOException;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class AcceptThread extends Thread {
    public static final String TAG = "AcceptThread";
    // The local server socket
    private final BluetoothServerSocket mmServerSocket;
    private LinkService linkService;

    public AcceptThread(LinkService linkService) {
        this.linkService = linkService;

        BluetoothServerSocket tmp = null;

        // Create a new listening server socket
        try {

            tmp = BTUtils.bluetoothAdapter.listenUsingRfcommWithServiceRecord(TAG,
                    LinkService.uuid);

        } catch (IOException e) {
            e.printStackTrace();
            linkService.sendMessage(MyHandler.LaunchAcceptError);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        setName("AcceptThread");

        BluetoothSocket socket = null;

        // Listen to the server socket if we're not connected
        while (linkService.getState() != LinkService.STATE_CONNECTED) {
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                linkService.sendMessage(MyHandler.RunAcceptError);
                break;
            }

            // If a connection was accepted
            if (socket != null) {
                synchronized (this) {
                    switch (linkService.getState()) {
                        case LinkService.STATE_LISTEN:
                        case LinkService.STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            linkService.connected(socket, socket.getRemoteDevice());
                            break;
                        case LinkService.STATE_NONE:
                        case LinkService.STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                    }
                }
            }
        }

    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
