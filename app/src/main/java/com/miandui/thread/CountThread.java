package com.miandui.thread;

import android.util.Log;

import com.miandui.data.DataUtil;
import com.miandui.data.Record;
import com.miandui.utils.normal.TimeUtils;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class CountThread extends Thread {
    private boolean needRecord;
    private boolean shut;
    private int i;
    private Record record;

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                Thread.sleep(1000);
                if (needRecord) {
                    Log.d("Sleep", "---" + i++);
                }
                if (shut) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startRecord() {
        Log.d("CountThread", "---start count");
        needRecord = true;
        initRecord();
    }

    public void stopRecord() {
        Log.d("CountThread", "---stop count");
        needRecord = false;
        saveRecord();
    }

    private void initRecord() {
        record = new Record();
        record.setStartTime(TimeUtils.currentTime());
    }

    private void saveRecord() {
        record.setEndTime(TimeUtils.currentTime());
        DataUtil.saveRecord(record);
        record = null;
    }

    public void cancle() {
        Log.d("TestThread", "---cancled");
        shut = true;
    }
}
