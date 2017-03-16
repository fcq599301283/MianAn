package com.mianan.netWork.customSubscriber;


import com.mianan.netWork.callBack.DefaultCallback;
import com.mianan.netWork.callBack.SimpleCallback;
import com.mianan.netWork.netUtil.NormalKey;
import com.mianan.utils.base.BaseView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by FengChaoQun
 * on 2017/1/8
 * 提供简单的Subscriber及回调
 * 方法名含Default的方法一律默认200为成功返回码
 */

public class SubscriberFactory {

    /**
     * description:获取一个SubscriberOnNext 返回码200为成功 其余的调用onFail
     *
     * @param simpleCallback 回调  处理OnSuccess OnFail
     */

    public static SubscriberOnNext<ResponseBody> getDefaultOnNext(final SimpleCallback simpleCallback) {
        return new SubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException, IOException {
                JSONObject jsonObject = new JSONObject(e.string());
                switch (jsonObject.getInt(NormalKey.code)) {
                    case NormalKey.CODE_200:
                        if (simpleCallback != null) {
                            simpleCallback.onSuccess(jsonObject);
                        }
                        break;
                    default:
                        if (simpleCallback != null) {
                            simpleCallback.onFail(jsonObject.getString(NormalKey.code), jsonObject.getString(NormalKey.msg));
                        }
                        break;
                }
            }
        };
    }

    /**
     * description:获取默认的SubscriberOnNext 默认返回码200为成功
     *
     * @param defaultCallback 仅处理OnSuccess 其余情况不处理
     */

    public static SubscriberOnNext<ResponseBody> getDefaultBackOnNext(final SimpleCallback defaultCallback) {
        return new SubscriberOnNext<ResponseBody>() {
            @Override
            public void onNext(ResponseBody e) throws JSONException, IOException {
                JSONObject jsonObject = new JSONObject(e.string());
                switch (jsonObject.getInt(NormalKey.code)) {
                    case NormalKey.CODE_200:
                        if (defaultCallback != null) {
                            defaultCallback.onSuccess(jsonObject);
                        }
                        break;
                }
            }
        };
    }


    /**
     * description:通过SimpleCallback获取一个SubscriberOnError
     *
     * @param callback 处理OnError
     */

    public static SubscriberOnError getOnError(final SimpleCallback callback) {
        return new SubscriberOnError() {
            @Override
            public void OnError(Throwable t) {
                callback.onError(t);
            }
        };
    }

    /**
     * description:用SubscriberOnNext直接构造一个ProgressSubsciber 不主动处理异常
     */

    public static ProgressSubsciber<ResponseBody> getProgressSubscriber(SubscriberOnNext<ResponseBody> onNext, BaseView baseView) {
        return new ProgressSubsciber<>(onNext, baseView);
    }

    /**
     * description:用SimpleCallback构造一个默认200为成功码的ProgressSubsciber
     *
     * @param callback 处理各种情况
     */

    public static ProgressSubsciber<ResponseBody> getDefaultProgressSubscriber(SimpleCallback callback, BaseView baseView) {
        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(getDefaultOnNext(callback), baseView);
        subsciber.setSubscriberOnError(getOnError(callback));
        return subsciber;
    }

    /**
     * description:获得一个默认200为返回码的ProgressSubsciber
     *
     * @param callback 仅处理OnSuccess 其余的情况默认处理
     */

    public static ProgressSubsciber<ResponseBody> getDefaultProgressSubscriber(DefaultCallback callback) {
        return new ProgressSubsciber<>(getDefaultOnNext(callback), callback.getBaseView());
    }

    /**
     * description:用SimpleCallback和SubscriberOnNext构造一个ProgressSubsciber
     *
     * @param callback 处理各种情况
     */

    public static ProgressSubsciber<ResponseBody> getProgressSubscriber(SubscriberOnNext<ResponseBody> onNext, SimpleCallback callback, BaseView baseView) {
        ProgressSubsciber<ResponseBody> subsciber = new ProgressSubsciber<>(onNext, baseView);
        subsciber.setSubscriberOnError(getOnError(callback));
        return subsciber;
    }


    /**
     * description:获取普通的BackgroudSubscriber 默认200为返回码 其余不作处理
     *
     * @param
     */

    public static BackgroudSubscriber<ResponseBody> getDefaultBackSubscriber(DefaultCallback callback) {
        return new BackgroudSubscriber<>(getDefaultBackOnNext(callback));
    }

    /**
     * description:获取BackgroudSubscriber 默认200为成功码  处理所有情况
     */

    public static BackgroudSubscriber<ResponseBody> getNormalBackSubscriber(SimpleCallback simpleCallback) {
        BackgroudSubscriber<ResponseBody> BackgroudSubscriber = new BackgroudSubscriber<>(getDefaultOnNext(simpleCallback));
        BackgroudSubscriber.setSubscriberOnError(getOnError(simpleCallback));
        return BackgroudSubscriber;
    }

}
