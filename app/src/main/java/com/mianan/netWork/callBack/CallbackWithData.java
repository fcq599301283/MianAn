package com.mianan.netWork.callBack;

/**
 * Created by FengChaoQun
 * on 2017/1/10
 */

public interface CallbackWithData<T> extends SimpleCallback {
    void onBackData(T t);
}
