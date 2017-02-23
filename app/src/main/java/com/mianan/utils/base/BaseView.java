package com.mianan.utils.base;

import android.app.Activity;


import com.mianan.utils.view.LoadingDialog;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/12/30
 */

public interface BaseView {

    void showToast(String msg);

    void showLoadingDialog(String msg);

    void showNormalDialog(String msg);

    void hideLoadingDialog();

    void setOnLoadingDialogDiamiss(LoadingDialog.onDismiss on);

    void setRightSlide(BaseActivity.RightSlide rightSlide);

    void clearRightSlide();

    void setEnableRightSlide(boolean enable);

    Realm getRelm();

    Activity getActivity();

    BaseView getBaseView();
}
