package com.mianan.utils;

import android.text.TextUtils;
import android.util.Log;

import com.mianan.MyApplication;
import com.mianan.netWork.netUtil.NormalKey;
import com.mianan.data.DataCopy;
import com.mianan.data.MarkAndTime;
import com.mianan.data.UserInfo;
import com.mianan.utils.normal.SPUtils;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2017/1/8
 */

public class TempUser {
    private static String account;
    private static UserInfo userInfo;
    private static String password;
    private static MarkAndTime markAndTime = new MarkAndTime();
    private static ArrayList<onPersonInfoChange> onPersonInfoChanges = new ArrayList<>();
    private static ArrayList<onMarkChange> onMarkChanges = new ArrayList<>();

    public static String getAccount() {
        if (TextUtils.isEmpty(account)) {
            String lastAccount = (String) SPUtils.get(MyApplication.getInstance(), SPUtils.ACCOUNT, SPUtils.DEFAULT_STRING);
            if (!TextUtils.isEmpty(lastAccount) && !lastAccount.equals(SPUtils.DEFAULT_STRING)) {
                account = lastAccount;
            }
        }
        return account;
    }

    public static void setAccount(String account) {
        TempUser.account = account;
        SPUtils.put(MyApplication.getInstance(), SPUtils.ACCOUNT, account);
    }


    public static void setUserInfo(UserInfo userInfo) {
        TempUser.userInfo = userInfo;
    }

    public static UserInfo getUserInfo() {
        if (userInfo == null) {
            reloadUserInfo();
        }
        return userInfo;
    }

    public static void reloadUserInfo() {
        Realm realm = Realm.getDefaultInstance();
        UserInfo i = realm.where(UserInfo.class).equalTo(NormalKey.identification, getAccount()).findFirst();
        if (i != null) {
            userInfo = DataCopy.copy(i);
        }
        realm.close();
//        setAccount(userInfo.getIdentification());
        for (onPersonInfoChange onPersonInfoChange : onPersonInfoChanges) {
            onPersonInfoChange.onChange(userInfo);
        }

    }

    public static MarkAndTime getMarkAndTime() {
        return markAndTime;
    }

    public static String getPassword() {
        if (password == null) {
            password = (String) SPUtils.get(MyApplication.getInstance(), SPUtils.PASSWORD, SPUtils.DEFAULT_STRING);
        }
        if (SPUtils.DEFAULT_STRING.equals(password)) {
            return null;
        }
        return password;
    }

    public static void setPassword(String password) {
        TempUser.password = password;
        SPUtils.put(MyApplication.getInstance(), SPUtils.PASSWORD, password);
        Log.d("TempUser", "" + password);
    }

    public static void setTodayMarkAndTime(MarkAndTime markAndTime) {
        TempUser.markAndTime = markAndTime;
        for (onMarkChange onMarkChange : onMarkChanges) {
            onMarkChange.onChange(TempUser.markAndTime);
        }
    }

    private static void addOnPersonInfoChangeObserver(onPersonInfoChange onPersonInfoChange) {
        if (onPersonInfoChange != null) {
            onPersonInfoChanges.add(onPersonInfoChange);
        }
    }

    private static void removeOnPersonInfoChangeOberser(onPersonInfoChange onPersonInfoChange) {
        if (onPersonInfoChange != null) {
            onPersonInfoChanges.remove(onPersonInfoChange);
        }
    }

    public static void registerOnPersonInfoChangeObservers(onPersonInfoChange onPersonInfoChange, boolean register) {
        if (onPersonInfoChange == null) {
            return;
        }
        if (register) {
            addOnPersonInfoChangeObserver(onPersonInfoChange);
        } else {
            removeOnPersonInfoChangeOberser(onPersonInfoChange);
        }
    }

    private static void addOnMarkChangeObserver(onMarkChange onMarkChange) {
        if (onMarkChange != null) {
            onMarkChanges.add(onMarkChange);
        }
    }

    private static void removeOnMarkChangeObserver(onMarkChange onMarkChange) {
        if (onMarkChange != null) {
            onMarkChanges.remove(onMarkChange);
        }
    }

    public static void registerOnMarkChangeObserver(onMarkChange onMarkChange, boolean register) {
        if (onMarkChange == null) {
            return;
        }
        if (register) {
            addOnMarkChangeObserver(onMarkChange);
        } else {
            removeOnMarkChangeObserver(onMarkChange);
        }
    }

    public interface onPersonInfoChange {
        void onChange(UserInfo userInfo);
    }

    public interface onMarkChange {
        void onChange(MarkAndTime markAndTime);
    }
}
