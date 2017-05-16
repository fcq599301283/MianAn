package com.miandui.netWork.netCollection;

import com.miandui.netWork.customSubscriber.SubscriberFactory;
import com.miandui.netWork.api.NetApiObservableFactory;
import com.miandui.netWork.callBack.DefaultCallback;
import com.miandui.netWork.callBack.SimpleCallback;
import com.miandui.utils.base.BaseView;

import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by FengChaoQun
 * on 2017/1/9
 * 发起常用的网络请求
 */

public final class BaseRequest {

    /**
     * description:默认200为成功返回码  其余返回码和异常返回上级处理
     */

    public static void NormalPostRequest(String url, Map<String, String> data, BaseView baseView, SimpleCallback simpleCallback) {
        Observable<ResponseBody> observable = NetApiObservableFactory.getInstance().normalPostObservable(url, data);
        toRequest(observable, SubscriberFactory.getDefaultProgressSubscriber(simpleCallback, baseView));
    }

    /**
     * description:默认200为成功返回码 其余返回码Toast 异常不做处理
     */

    public static void DefautPostRequest(String url, Map<String, String> data, DefaultCallback callback) {
        toRequest(NetApiObservableFactory.getInstance().normalPostObservable(url, data),
                SubscriberFactory.getDefaultProgressSubscriber(callback));
    }

    /**
     * description:不显示progressbar 默认200为成功返回码 其余情况不处理
     */

    public static void DefautPostBackRequest(String url, Map<String, String> data, DefaultCallback callback) {
        toRequest(NetApiObservableFactory.getInstance().normalPostObservable(url, data),
                SubscriberFactory.getDefaultBackSubscriber(callback));
    }

    /**
     * description:不显示progressbar 默认200为成功返回码 其余情况返回上级处理
     */

    public static void NormalPostBackRequest(String url, Map<String, String> data, SimpleCallback callback) {
        toRequest(NetApiObservableFactory.getInstance().normalPostObservable(url, data),
                SubscriberFactory.getNormalBackSubscriber(callback));
    }

    public static <T> void toRequest(Observable<T> o, Subscriber<T> s) {
        toSubscribe(o, s);
    }

    private static <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
