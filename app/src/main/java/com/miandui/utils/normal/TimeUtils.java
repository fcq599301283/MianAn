package com.miandui.utils.normal;

import java.text.ParseException;
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

    public static long translateHHMMSStoSecond(String time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long translateHHMMSStoSecond2(String time) {
        String hms[] = time.split(":");
        if (hms.length != 3) {
            return 0;
        }
        return (Integer.valueOf(hms[0]) * 3600 + Integer.valueOf(hms[1]) * 60 + Integer.valueOf(hms[2]));
    }

    public static float translateHHMMSStoHours(String time) {
        long seconds = translateHHMMSStoSecond2(time);
        return seconds / 3600f;
    }

    public static String getDataByMMHHSS(long time) {
        String hour = "" + time / 3600;
        String minute = "" + time % 3600 / 60;
        String second = "" + time % 3600 % 60;
        return hour + ":" + minute + ":" + second;
    }

    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        return getData(calendar);
//        String year = String.valueOf(calendar.get(Calendar.YEAR));
//        String month = calendar.get(Calendar.MONTH) + 1 >= 10 ?
//                String.valueOf(calendar.get(Calendar.MONTH) + 1) :
//                "0" + (calendar.get(Calendar.MONTH) + 1);
//        String day = calendar.get(Calendar.DAY_OF_MONTH) >= 10 ?
//                String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) :
//                "0" + (calendar.get(Calendar.DAY_OF_MONTH));
//
//        return year + "-" + month + "-" + day;
    }

    public static String getData(int dayAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dayAgo);
        return getData(calendar);
    }

    public static String getData(Calendar calendar) {
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
