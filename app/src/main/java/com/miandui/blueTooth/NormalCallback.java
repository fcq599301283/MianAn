package com.miandui.blueTooth;

import android.os.Message;

import com.miandui.utils.base.BaseView;

/**
 * Created by FengChaoQun
 * on 2017/3/7
 */

public class NormalCallback implements MyHandler.OnStateChange {
    private BaseView baseView;

    public NormalCallback(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onChange(Message msg) {
        switch (msg.what) {
            case MyHandler.STATE_CONNECTING:
                baseView.showLoadingDialog("连接中");
                break;
            case MyHandler.LaunchConnectError:
                baseView.showNormalDialog("启动连接失败");
                break;
            case MyHandler.LaunchConnectedError:
                baseView.showNormalDialog("建立连接失败");
                break;
            case MyHandler.STATE_CONNECTED:
                baseView.hideLoadingDialog();
                baseView.showNormalDialog("连接成功");
                break;
            case MyHandler.STATE_CONNECT_FAIL:
                baseView.hideLoadingDialog();
                baseView.showNormalDialog("连接失败");
                break;
            case MyHandler.connectLose:
                baseView.showToast("失去连接");
                break;
            case MyHandler.LaunchAcceptError:
                baseView.showToast("启动接受连接组件失败");
                break;
//            case MyHandler.RunAcceptError:
//                baseView.showToast("运行接受组件失败");
//                break;
        }
    }
}
