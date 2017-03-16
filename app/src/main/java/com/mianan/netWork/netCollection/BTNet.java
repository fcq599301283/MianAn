package com.mianan.netWork.netCollection;

import com.mianan.netWork.customSubscriber.SubscriberFactory;
import com.mianan.netWork.api.NetApiObservableFactory;
import com.mianan.netWork.callBack.SimpleCallback;
import com.mianan.netWork.netUtil.BaseUrl;
import com.mianan.data.UploadRecordBean;

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

}
