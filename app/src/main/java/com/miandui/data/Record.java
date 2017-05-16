package com.miandui.data;

import com.miandui.utils.normal.TimeUtils;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FengChaoQun
 * on 2017/3/7
 */

public class Record extends RealmObject {
    @PrimaryKey
    private long startTime;
    private long endTime;
    private long date;
    private String userId;
    private boolean isUploaded;
    private int number;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getTotalTime() {
        return endTime - startTime > 0 ? endTime - startTime : 0;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public UploadRecordBean.TimingBean getUploadTimingBean() {
        UploadRecordBean.TimingBean timingBean = new UploadRecordBean.TimingBean();
        timingBean.setDate_start(TimeUtils.getYYYYMMDD(startTime));
        timingBean.setDate_end(TimeUtils.getYYYYMMDD(endTime));
        timingBean.setTime_start(TimeUtils.getHHMMSS(startTime));
        timingBean.setTime_end(TimeUtils.getHHMMSS(endTime));
        timingBean.setNumber(getNumber());
        return timingBean;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Record{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", date=" + date +
                ", userId=" + userId +
                ", isUploaded=" + isUploaded +
                '}';
    }
}
