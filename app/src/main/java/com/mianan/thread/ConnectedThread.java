package com.mianan.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.mianan.blueTooth.MyHandler;
import com.mianan.utils.LinkService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private LinkService linkService;
    private final long timeDivider = 5000;
    private long timeCounts = 0;

    public ConnectedThread(BluetoothSocket socket, LinkService linkService) {
        mmSocket = socket;
        this.linkService = linkService;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
            MyHandler.getInstance().obtainMessage(MyHandler.STATE_CONNECTED, socket.getRemoteDevice()).sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
            linkService.sendMessage(MyHandler.LaunchConnectedError);
            linkService.setState(MyHandler.STATE_NONE);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        // Keep listening to the InputStream while connected
        while (true) {
            try {
                write(MyHandler.okByte);
                // Read from the InputStream
                bytes = mmInStream.read(buffer);

                // Send the obtained bytes to the UI Activity
//                linkService.sendMessage(MyHandler.connectNormal);
                Log.d("link", "" + (timeCounts++));

                Thread.sleep(timeDivider);

            } catch (IOException e) {
                e.printStackTrace();
                linkService.sendMessage(MyHandler.connectLose);
                linkService.setState(MyHandler.STATE_NONE);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Write to the connected OutStream.
     *
     * @param buffer The bytes to write
     */
    public void write(byte[] buffer) {
        try {
            mmOutStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            linkService.sendMessage(MyHandler.connectLose);
            linkService.setState(MyHandler.STATE_NONE);
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
