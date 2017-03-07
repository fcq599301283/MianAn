package com.mianan.data;

import java.util.List;

/**
 * Created by FengChaoQun
 * on 2017/3/7
 */

public class UploadRecordBean {

    /**
     * identification : 17768345313
     * timing : [{"date_start":"2017-03-07","date_end":"2017-03-07","time_start":"22:32:25","time_end":"22:32:57"}]
     */

    private String identification;
    private List<TimingBean> timing;

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public List<TimingBean> getTiming() {
        return timing;
    }

    public void setTiming(List<TimingBean> timing) {
        this.timing = timing;
    }

    public static class TimingBean {
        /**
         * date_start : 2017-03-07
         * date_end : 2017-03-07
         * time_start : 22:32:25
         * time_end : 22:32:57
         */

        private String date_start;
        private String date_end;
        private String time_start;
        private String time_end;

        public String getDate_start() {
            return date_start;
        }

        public void setDate_start(String date_start) {
            this.date_start = date_start;
        }

        public String getDate_end() {
            return date_end;
        }

        public void setDate_end(String date_end) {
            this.date_end = date_end;
        }

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }
    }
}
