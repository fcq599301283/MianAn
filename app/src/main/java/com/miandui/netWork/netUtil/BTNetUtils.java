package com.miandui.netWork.netUtil;

import android.util.Log;

import com.miandui.data.MarkAndTime;
import com.miandui.data.Record;
import com.miandui.data.UploadRecordBean;
import com.miandui.netWork.api.NetApiObservableFactory;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.callBack.SimpleCallback;
import com.miandui.netWork.customSubscriber.SubscriberFactory;
import com.miandui.netWork.customSubscriber.SubscriberOnNext;
import com.miandui.netWork.netCollection.BTNet;
import com.miandui.netWork.netCollection.BaseRequest;
import com.miandui.utils.TempUser;
import com.miandui.utils.normal.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2017/3/7
 */

public class BTNetUtils {

    private static boolean isUpLoad;

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

        //如果正在上传 则不重复上传
        if (isUpLoad) {
            return;
        }
        isUpLoad = true;

        final Realm realm = Realm.getDefaultInstance();
        final List<Record> records = realm.where(Record.class)
                .equalTo(NormalKey.userId, TempUser.getAccount())
                .equalTo(NormalKey.isUploaded, false)
                .findAll();
        if (records.isEmpty()) {
            if (callback != null) {
                callback.onSuccess(null);
            }
            isUpLoad=false;
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
                isUpLoad=false;
            }

            @Override
            public void onFail(String code, String msg) {
                realm.close();
                if (callback != null) {
                    callback.onFail(code, msg);
                }
                isUpLoad=false;
            }

            @Override
            public void onError(Throwable throwable) {
                realm.close();
                if (callback != null) {
                    callback.onError(throwable);
                }
                isUpLoad=false;
            }
        });
    }

    public static void getTodayMarkAndTime(final SimpleCallback simpleCallback) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
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

    public static synchronized void refreshMarkAndTimeBack(final SimpleCallback simpleCallback) {
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

    public static void getRecord(final SimpleCallback simpleCallback) {
        Map<String, String> map = new HashMap<>();
        map.put(NormalKey.identification, TempUser.getAccount());
        map.put(NormalKey.date_start, TimeUtils.getData(-6));
        map.put(NormalKey.date_end, TimeUtils.getTodayDate());
        BTNet.GetRecord(map, simpleCallback);
    }

    public static void signIn(final DefaultCallback callback){
        SubscriberOnNext<ResponseBody> onNext=new SubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException, IOException {
                JSONObject jsonObject=new JSONObject(e.string());
                switch (jsonObject.getInt(NormalKey.code)){
                    case 200:
                        callback.onSuccess(jsonObject);
                        break;
                    case 201:
                        callback.onSuccess(jsonObject);
                        callback.getBaseView().showToast("今日已经签到过");
                        break;
                    default:
                        callback.onFail(jsonObject.getString(NormalKey.code),
                                jsonObject.getString(NormalKey.msg));
                        break;
                }
            }
        };

        Map<String,String> map=new HashMap<>();
        map.put(NormalKey.identification,TempUser.getAccount());

        BaseRequest.toRequest(NetApiObservableFactory.getInstance().normalPostObservable(BaseUrl.SIGN_IN,map),
                SubscriberFactory.getProgressSubscriber(onNext,callback,callback.getBaseView()));
    }
}
