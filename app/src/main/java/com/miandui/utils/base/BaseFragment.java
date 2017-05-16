package com.miandui.utils.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.miandui.R;
import com.miandui.utils.AppContracts;
import com.miandui.utils.view.LoadingDialog;

import io.realm.Realm;

/**
 * Created by FengChaoQun
 * on 2016/12/30
 */

public class BaseFragment extends Fragment implements BaseView {
    public static final String TAG = AppContracts.TAG + "-Fragment";
    protected BaseActivity baseActivity;
    private BaseActivity.RightSlide rightSlide;
    protected View rootView;
    protected Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();

        realm = Realm.getDefaultInstance();
        rightSlide = new BaseActivity.RightSlide() {
            @Override
            public boolean rightSlide() {
                getFragmentManager().popBackStack();
                return true;
            }
        };

        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                R.anim.slide_in_left, R.anim.slide_out_left).commit();
    }

    protected View getRootView(int rootViewId) {
        return View.inflate(baseActivity, rootViewId, null);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRightSlide(rightSlide);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setRightSlide(rightSlide);
        }
    }

    @Override
    public void showToast(String msg) {
        baseActivity.showToast(msg);
    }

    @Override
    public void showLoadingDialog(String msg) {
        baseActivity.showLoadingDialog(msg);
    }

    @Override
    public void showNormalDialog(String msg) {
        baseActivity.showNormalDialog(msg);
    }

    @Override
    public void hideLoadingDialog() {
        baseActivity.hideLoadingDialog();
    }

    @Override
    public void setOnLoadingDialogDiamiss(LoadingDialog.onDismiss on) {
        baseActivity.setOnLoadingDialogDiamiss(on);
    }

    @Override
    public void setRightSlide(BaseActivity.RightSlide rightSlide) {
        this.rightSlide = rightSlide;
        baseActivity.setRightSlide(rightSlide);
    }

    @Override
    public void clearRightSlide() {
        rightSlide = null;
        baseActivity.clearRightSlide();
    }

    @Override
    public void setEnableRightSlide(boolean enable) {
        if (!enable) {
            clearRightSlide();
        }
        baseActivity.setEnableRightSlide(enable);
    }

    @Override
    public Realm getRelm() {
        return realm;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    public void setCloseActivity() {
        clearRightSlide();
        setRightSlide(new BaseActivity.RightSlide() {
            @Override
            public boolean rightSlide() {
                baseActivity.finish();
                return true;
            }
        });
    }
}
