package com.mianan.NetWork.CustomSubscriber;

import com.mianan.utils.base.BaseView;
import com.mianan.utils.view.LoadingDialog;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by FengChaoQun
 * on 2016/8/4
 * 带Progressbar的Subscriber
 */
public class ProgressSubsciber<T> extends Subscriber<T> {
    private BaseView baseView;
    private String loadingText = "加载中...";
    private SubscriberOnNext subscriberOnNext;          //OnNext
    private SubscriberOnComplete subscriberOnComplete;  //OnComplete
    private SubscriberOnError subscriberOnError;        //OnError
    private SubscriberOnStart subscriberOnStart;        //OnStart

    public ProgressSubsciber(SubscriberOnNext subscriberOnNext, BaseView baseView) {
        this.baseView = baseView;
        this.subscriberOnNext = subscriberOnNext;
        baseView.setOnLoadingDialogDiamiss(new LoadingDialog.onDismiss() {
            @Override
            public void doBeforDismiss() {
                unsubscribe();
            }
        });
    }

    @Override
    public void onCompleted() {
        baseView.hideLoadingDialog();
        if (subscriberOnComplete != null) {
            subscriberOnComplete.OnComplete();
        }
    }

    @Override
    public void onError(Throwable e) {
        baseView.hideLoadingDialog();
        if (e instanceof SocketTimeoutException) {
            baseView.showToast("请求超时");
        } else if (e instanceof ConnectException) {
            baseView.showToast("网络异常，请检查您的网络状态");
        } else if (e instanceof HttpException) {
            baseView.showToast("服务器异常");
        } else {
            baseView.showToast("请求错误");
        }
        if (subscriberOnError != null) {
            subscriberOnError.OnError(e);
        }
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        if (subscriberOnNext != null) {
            try {
                subscriberOnNext.onNext(t);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        baseView.showLoadingDialog(loadingText);
        if (subscriberOnStart != null) {
            subscriberOnStart.OnStart();
        }
    }

    public void setBaseView(BaseView baseView) {
        this.baseView = baseView;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public void setSubscriberOnNext(SubscriberOnNext subscriberOnNext) {
        this.subscriberOnNext = subscriberOnNext;
    }

    public void setSubscriberOnComplete(SubscriberOnComplete subscriberOnComplete) {
        this.subscriberOnComplete = subscriberOnComplete;
    }

    public void setSubscriberOnError(SubscriberOnError subscriberOnError) {
        this.subscriberOnError = subscriberOnError;
    }

    public void setSubscriberOnStart(SubscriberOnStart subscriberOnStart) {
        this.subscriberOnStart = subscriberOnStart;
    }
}
