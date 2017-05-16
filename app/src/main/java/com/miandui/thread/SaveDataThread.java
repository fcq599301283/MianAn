package com.miandui.thread;

import android.util.Log;

import com.miandui.utils.TimeCount;

/**
 * Created by FengChaoQun
 * on 2017/3/17
 */
public class SaveDataThread extends Thread {

    private boolean isShutDown;
    private long sleepTime = 10 * 1000;
    private long saveCount;

    @Override
    public void run() {
        super.run();
        while (!isShutDown) {
            try {
                Thread.sleep(sleepTime);
                TimeCount.getInstance().saveRecord();
                saveCount++;
                Log.d("SaveDataThread", "saveCount:" + saveCount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancle() {
        isShutDown = true;
    }
}
