package com.miandui.netWork.callBack;

import org.json.JSONObject;

/**
 * Created by FengChaoQun
 * on 2017/1/7
 */

public interface SimpleCallback {
    void onSuccess(JSONObject jsonObject);

    void onFail(String code, String msg);

    void onError(Throwable throwable);
}
