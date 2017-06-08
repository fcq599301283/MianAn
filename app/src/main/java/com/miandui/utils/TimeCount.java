package com.miandui.utils;

import android.util.Log;

import com.miandui.blueTooth.MyHandler;
import com.miandui.data.DataUtil;
import com.miandui.data.Record;
import com.miandui.netWork.netUtil.BTNetUtils;
import com.miandui.utils.normal.TimeUtils;

import java.util.Calendar;

/**
 * Created by FengChaoQun
 * on 2017/3/6
 * 记录息屏时间工具类
 */

public class TimeCount {
    private boolean isScreenOn;
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

        //如果是睡眠时间 则不执行任何操作
        if (LinkService.getInstance().isOnSleepTime()) {
            return;
        }
        if (!isScreenOn) {
            startRecord();
        } else {
            endRecord();
        }
    }

    public synchronized void startRecord() {
        if (isRecord) {
            return;
        }

        if (!isScreenOn && (LinkService.getInstance().isSingleMode() || MyHandler.getInstance().getCurrentState() == MyHandler.STATE_CONNECTED)) {
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
