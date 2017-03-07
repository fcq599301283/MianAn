package com.mianan.utils.normal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by FengChaoQun
 * on 2017/1/11
 */

public class TimeUtils {
    /**
     * 2016/12/14 21:52
     * description:获取系统的当前时间
     */
    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public static String getYYYYMMDD(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date(time);
        return format.format(d1);
    }

    public static String getHHMMSS(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date d1 = new Date(time);
        return format.format(d1);
    }

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = calendar.get(Calendar.MONTH) + 1 >= 10 ?
                String.valueOf(calendar.get(Calendar.MONTH) + 1) :
                "0" + (calendar.get(Calendar.MONTH) + 1);
        String day = calendar.get(Calendar.DAY_OF_MONTH) >= 10 ?
                String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) :
                "0" + (calendar.get(Calendar.DAY_OF_MONTH));

        return year + "-" + month + "-" + day;
    }
}
