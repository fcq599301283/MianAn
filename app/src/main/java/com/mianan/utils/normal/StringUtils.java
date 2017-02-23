package com.mianan.utils.normal;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by FengChaoQun
 * on 2016/12/31
 */

public class StringUtils {

    public static boolean isNotEmpty(TextView... textViews) {
        for (TextView textView : textViews) {
            if (TextUtils.isEmpty(textView.getText().toString())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String getMonth(int month) {
        if (month < 10) {
            return "0" + (month + 1);
        } else {
            return month + 1 + "";
        }
    }
}
