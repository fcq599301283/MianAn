package com.miandui.netWork.api;

import com.miandui.netWork.RetrofitFactory;
import com.miandui.data.UploadRecordBean;

import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2017/1/9
 */

public class NetApiObservableFactory {
    private NormalPostAPI normalPostAPI = RetrofitFactory.getRetrofit().create(NormalPostAPI.class);
    private NormalPostJsonAPI normalPostJsonAPI = RetrofitFactory.getRetrofit().create(NormalPostJsonAPI.class);

    private NetApiObservableFactory() {

    }

    public static NetApiObservableFactory getInstance() {
        return NetApiObservableFactory.InstanceHolder.Instance;
    }

    public Observable<ResponseBody> normalPostObservable(String url, Map<String, String> data) {
        return normalPostAPI.post(url, data);
    }

    public Observable<ResponseBody> normalPostJsonObservable(String url, UploadRecordBean data) {
        return normalPostJsonAPI.post(url, data);
    }

    private static class InstanceHolder {
        private static NetApiObservableFactory Instance = new NetApiObservableFactory();
    }

}
