package com.miandui.netWork.callBack;

import org.json.JSONObject;

/**
 * Created by FengChaoQun
 * on 2017/1/10
 */

public abstract class JustCareBackData<T> implements CallbackWithData<T> {

    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void onFail(String code, String msg) {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
