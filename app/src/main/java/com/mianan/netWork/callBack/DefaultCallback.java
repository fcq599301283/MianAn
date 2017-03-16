package com.mianan.netWork.callBack;


import com.mianan.utils.base.BaseView;

/**
 * Created by FengChaoQun
 * on 2017/1/8
 */

public abstract class DefaultCallback implements SimpleCallback {
    private BaseView baseView;

    public DefaultCallback(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onFail(String code, String msg) {
        baseView.showNormalDialog(msg);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    public BaseView getBaseView() {
        return baseView;
    }
}
