package com.mianan.NetWork.api;

import com.mianan.NetWork.RetrofitFactory;

import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by FengChaoQun
 * on 2017/1/9
 */

public class NetApiObservableFactory {
    private NormalPostAPI normalPostAPI = RetrofitFactory.getRetrofit().create(NormalPostAPI.class);

    private NetApiObservableFactory() {

    }

    public static NetApiObservableFactory getInstance() {
        return NetApiObservableFactory.InstanceHolder.Instance;
    }

    public Observable<ResponseBody> normalPostObservable(String url, Map<String, String> data) {
        return normalPostAPI.post(url, data);
    }

    private static class InstanceHolder {
        private static NetApiObservableFactory Instance = new NetApiObservableFactory();
    }

}
