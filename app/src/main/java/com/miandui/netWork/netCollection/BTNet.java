package com.miandui.netWork.netCollection;

import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.customSubscriber.SubscriberFactory;
import com.miandui.netWork.api.NetApiObservableFactory;
import com.miandui.netWork.callBack.SimpleCallback;
import com.miandui.netWork.netUtil.BaseUrl;
import com.miandui.data.UploadRecordBean;

import java.util.Map;

/**
 * Created by FengChaoQun
 * on 2017/3/7
 */

public class BTNet {

    public static void UploadRecord(UploadRecordBean jsonObject, SimpleCallback simpleCallback) {
        BaseRequest.toRequest(NetApiObservableFactory.getInstance().normalPostJsonObservable(BaseUrl.UPLOAD_RECORD, jsonObject),
                SubscriberFactory.getNormalBackSubscriber(simpleCallback));
    }

    public static void GetRecord(Map<String, String> map, SimpleCallback simpleCallback) {
        BaseRequest.NormalPostBackRequest(BaseUrl.GET_RECORD, map, simpleCallback);
    }

    public static void getTodayMarkAndTime(Map<String, String> map, SimpleCallback simpleCallback) {
        BaseRequest.NormalPostBackRequest(BaseUrl.GET_TODAY_RECORD, map, simpleCallback);
    }

    public static void signIn(Map<String, String> map, DefaultCallback callback) {
        BaseRequest.DefautPostRequest(BaseUrl.SIGN_IN, map, callback);
    }

}
