package com.mianan.utils;

import android.util.Log;

import com.mianan.netWork.netUtil.BTNetUtils;
import com.mianan.data.DataUtil;
import com.mianan.data.Record;
import com.mianan.thread.CountThread;
import com.mianan.utils.normal.TimeUtils;

import java.util.Calendar;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 */

public class TimeCount {
    private boolean isScreenOn;

    private CountThread countThread;

    private Record record;
    private boolean isRecord;

    public static TimeCount getInstance() {
        return SingletonHolder.timeCount;
    }

    private static class SingletonHolder {
        private static final TimeCount timeCount = new TimeCount();
    }

    private TimeCount() {

    }

    public boolean isScreenOn() {
        return isScreenOn;
    }

    public void setScreenOn(boolean screenOn) {
        isScreenOn = screenOn;

        if (!isScreenOn) {
            startRecord();
        } else {
            endRecord();
        }
//        if (countThread == null || !countThread.isAlive()) {
//            countThread = new CountThread();
//            countThread.start();
//        }
//
//        if (screenOn) {
//            countThread.stopRecord();
//        } else {
//            if (LinkService.getInstance().getState() == LinkService.STATE_CONNECTED) {
//                countThread.startRecord();
//            }
//        }
    }

    public synchronized void startRecord() {
        if (isRecord) {
            return;
        }

        if (!isScreenOn && (LinkService.getInstance().isSingleMode() || LinkService.getInstance().getState() == LinkService.STATE_CONNECTED)) {
            isRecord = true;
            record = new Record();
            record.setStartTime(TimeUtils.currentTime());
            Calendar calendar = Calendar.getInstance();
            String date = "" + calendar.get(Calendar.YEAR)
                    + calendar.get(Calendar.MONTH)
                    + calendar.get(Calendar.DAY_OF_MONTH);
            record.setDate(Long.valueOf(date));
            record.setUploaded(false);
            record.setUserId(TempUser.getAccount());
            if (LinkService.getInstance().isSingleMode()) {
                record.setNumber(1);
                Log.d("Single Record", "start");
            } else {
                record.setNumber(2);
                Log.d("Normal Record", "start");
            }
        }

//        if (!isScreenOn && LinkService.getInstance().getState() == LinkService.STATE_CONNECTED) {
//            isRecord = true;
//            record = new Record();
//            record.setStartTime(TimeUtils.currentTime());
//            Calendar calendar = Calendar.getInstance();
//            String date = "" + calendar.get(Calendar.YEAR)
//                    + calendar.get(Calendar.MONTH)
//                    + calendar.get(Calendar.DAY_OF_MONTH);
//            record.setDate(Long.valueOf(date));
//            record.setUploaded(false);
//            record.setUserId(TempUser.getAccount());
//            record.setNumber(2);
//            Log.d("Normal Record", "start");
//        }
    }

    public synchronized void endRecord() {
        if (!isRecord) {
            return;
        }
        isRecord = false;
        record.setEndTime(TimeUtils.currentTime());
        DataUtil.saveRecord(record);
        Log.d(" Record", "end");
        BTNetUtils.refreshMarkAndTimeBack(null);
    }

    public synchronized void saveRecord() {
        if (!isRecord) {
            return;
        }
        record.setEndTime(TimeUtils.currentTime());
        DataUtil.saveRecord(record);
    }
}
