package com.mianan.NetWork.netUtil;

import android.util.Log;

import com.mianan.NetWork.NetCollection.BTNet;
import com.mianan.NetWork.callBack.SimpleCallback;
import com.mianan.data.MarkAndTime;
import com.mianan.data.Record;
import com.mianan.data.UploadRecordBean;
import com.mianan.utils.TempUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2017/3/7
 */

public class BTNetUtils {

    public static void uploadRecord() {
        final Realm realm = Realm.getDefaultInstance();
        final List<Record> records = realm.where(Record.class)
                .equalTo(NormalKey.userId, TempUser.getAccount())
                .equalTo(NormalKey.isUploaded, false)
                .findAll();
        if (records.isEmpty()) {
            Log.d("Record", "is empty");
            return;
        }
        UploadRecordBean uploadRecordBean = new UploadRecordBean();
        uploadRecordBean.setIdentification(TempUser.getAccount());
        List<UploadRecordBean.TimingBean> timingBeanList = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            timingBeanList.add(records.get(i).getUploadTimingBean());
        }
        uploadRecordBean.setTiming(timingBeanList);
        BTNet.UploadRecord(uploadRecordBean, new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (int i = 0; i < records.size(); i++) {
                            records.get(i).setUploaded(true);
                        }
                    }
                });
                realm.close();
            }

            @Override
            public void onFail(String code, String msg) {
                realm.close();
            }

            @Override
            public void onError(Throwable throwable) {
                realm.close();
            }
        });
    }

    public static void uploadRecord(final SimpleCallback callback) {
        final Realm realm = Realm.getDefaultInstance();
        final List<Record> records = realm.where(Record.class)
                .equalTo(NormalKey.userId, TempUser.getAccount())
                .equalTo(NormalKey.isUploaded, false)
                .findAll();
        if (records.isEmpty()) {
            if (callback != null) {
                callback.onSuccess(null);
            }
            return;
        }
        UploadRecordBean uploadRecordBean = new UploadRecordBean();
        uploadRecordBean.setIdentification(TempUser.getAccount());
        List<UploadRecordBean.TimingBean> timingBeanList = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            timingBeanList.add(records.get(i).getUploadTimingBean());
        }
        uploadRecordBean.setTiming(timingBeanList);
        BTNet.UploadRecord(uploadRecordBean, new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (int i = 0; i < records.size(); i++) {
                            records.get(i).setUploaded(true);
                        }
                    }
                });
                realm.close();
                if (callback != null) {
                    callback.onSuccess(jsonObject);
                }
            }

            @Override
            public void onFail(String code, String msg) {
                realm.close();
                if (callback != null) {
                    callback.onFail(code, msg);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                realm.close();
                if (callback != null) {
                    callback.onError(throwable);
                }
            }
        });
    }

    public static void getTodayMarkAndTime(final SimpleCallback simpleCallback) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
//        map.put(NormalKey.date_start, TimeUtils.getTodayDate());
//        map.put(NormalKey.date_end, TimeUtils.getTodayDate());
        BTNet.getTodayMarkAndTime(map, new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    MarkAndTime markAndTime = new MarkAndTime();
                    String totalMark = jsonObject.getJSONObject(NormalKey.content).getString("mark_total");
                    String totalTime = jsonObject.getJSONObject(NormalKey.content).getString("time_total");
                    String todayMark = jsonObject.getJSONObject(NormalKey.content).getString("mark_today");
                    String todayTime = jsonObject.getJSONObject(NormalKey.content).getString("time_today");
                    markAndTime.setTotalMark(totalMark);
                    markAndTime.setTotalTime(totalTime);
                    markAndTime.setTodayMark(todayMark);
                    markAndTime.setTodayTime(todayTime);
                    TempUser.setTodayMarkAndTime(markAndTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (simpleCallback != null) {
                    simpleCallback.onSuccess(jsonObject);
                }
            }

            @Override
            public void onFail(String code, String msg) {
                if (simpleCallback != null) {
                    simpleCallback.onFail(code, msg);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (simpleCallback != null) {
                    simpleCallback.onError(throwable);
                }
            }
        });
    }


    public static void refreshMarkAndTimeBack(final SimpleCallback simpleCallback) {
        uploadRecord(new SimpleCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                getTodayMarkAndTime(simpleCallback);
            }

            @Override
            public void onFail(String code, String msg) {
                if (simpleCallback != null) {
                    simpleCallback.onFail(code, msg);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (simpleCallback != null) {
                    simpleCallback.onError(throwable);
                }
            }
        });
    }
}
