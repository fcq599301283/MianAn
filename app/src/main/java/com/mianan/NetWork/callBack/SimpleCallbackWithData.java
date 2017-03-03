package com.mianan.NetWork.callBack;


import com.mianan.utils.AppContracts;
import com.mianan.utils.base.BaseView;

/**
 * Created by FengChaoQun
 * on 2017/1/10
 */

public abstract class SimpleCallbackWithData<T> implements CallbackWithData<T> {
    private BaseView baseView;

    public SimpleCallbackWithData(BaseView baseView) {
        this.baseView = baseView;
    }


    @Override
    public void onFail(String code, String msg) {
        baseView.showToast(msg);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        baseView.showToast(AppContracts.REFRESH_FAIL);
    }

    public BaseView getBaseView() {
        return baseView;
    }
}
