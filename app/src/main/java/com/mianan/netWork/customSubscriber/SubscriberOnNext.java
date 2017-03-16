package com.mianan.netWork.customSubscriber;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by FengChaoQun
 * on 2016/8/4
 */
public interface SubscriberOnNext<T> {
    void onNext(T e) throws JSONException, IOException;
}
