package com.miandui.netWork.customSubscriber;

import org.json.JSONException;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2017/1/9
 */

public class BackgroudSubscriber<T> extends Subscriber<T> {
    private SubscriberOnNext subscriberOnNext;
    private SubscriberOnComplete subscriberOnComplete;  //OnComplete
    private SubscriberOnError subscriberOnError;        //OnError
    private SubscriberOnStart subscriberOnStart;        //OnStart

    public BackgroudSubscriber(SubscriberOnNext subscriberOnNext) {
        this.subscriberOnNext = subscriberOnNext;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (subscriberOnStart != null) {
            subscriberOnStart.OnStart();
        }
    }

    @Override
    public void onCompleted() {
        if (subscriberOnComplete != null) {
            subscriberOnComplete.OnComplete();
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (subscriberOnError != null) {
            subscriberOnError.OnError(e);
        }
    }

    @Override
    public void onNext(T t) {
        try {
            subscriberOnNext.onNext(t);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSubscriberOnNext(SubscriberOnNext subscriberOnNext) {
        this.subscriberOnNext = subscriberOnNext;
    }

    public void setSubscriberOnComplete(SubscriberOnComplete subscriberOnComplete) {
        this.subscriberOnComplete = subscriberOnComplete;
    }

    public void setSubscriberOnStart(SubscriberOnStart subscriberOnStart) {
        this.subscriberOnStart = subscriberOnStart;
    }

    public void setSubscriberOnError(SubscriberOnError subscriberOnError) {
        this.subscriberOnError = subscriberOnError;
    }
}
