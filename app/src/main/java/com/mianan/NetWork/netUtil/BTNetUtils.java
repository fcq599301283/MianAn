package com.mianan.NetWork.netUtil;

import android.util.Log;

import com.mianan.NetWork.NetCollection.BTNet;
import com.mianan.NetWork.callBack.SimpleCallback;
import com.mianan.data.Record;
import com.mianan.data.UploadRecordBean;
import com.mianan.utils.TempUser;
import com.mianan.utils.normal.TimeUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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
                .equalTo("isUploaded", false)
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

    public static void getTodayMark(SimpleCallback simpleCallback) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
//        Calendar calendar = Calendar.getInstance();
//        String data = "" + calendar.get(Calendar.YEAR)
//                + "-" + (calendar.get(Calendar.MONTH) + 1)
//                + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        map.put(NormalKey.date_start, TimeUtils.getTodayDate());
        map.put(NormalKey.date_end,  TimeUtils.getTodayDate());
        BTNet.GetRecord(map, simpleCallback);
    }
}
